package net.dimmid.ws.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.dimmid.entity.PlayerEvent;
import net.dimmid.service.IGameStateService;
import net.dimmid.util.JsonUtil;
import net.dimmid.ws.entity.User;
import net.dimmid.ws.util.WSAttributes;
import net.dimmid.ws.util.WSJsonUtil;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class WebClientMessageService {
    private final BlockingQueue<PlayerEvent> eventInputQueue;
    private final ConcurrentHashMap<Channel, User> userChannels;
    private final IGameStateService gameStateService;

    public WebClientMessageService(BlockingQueue<PlayerEvent> eventInputQueue,
                                   IGameStateService gameStateService) {
        this.eventInputQueue = eventInputQueue;
        this.userChannels = new ConcurrentHashMap<>();
        this.gameStateService = gameStateService;
    }

    public void closeChannel(ChannelHandlerContext ctx) throws JsonProcessingException, InterruptedException {
        Channel channel = ctx.channel();
        channel.close();
        removeUserFromGameStateService(userChannels.get(channel));
        userChannels.remove(channel);
    }

    public void addMessageToQueue(ChannelHandlerContext ctx, Object message) throws InterruptedException, JsonProcessingException {
        processMessage(ctx, message);

    }

    public void broadcastMessagesToClient(Map<String, String> playersData) throws JsonProcessingException, InterruptedException {
        for (Channel channel : userChannels.keySet()) {
            User user = userChannels.get(channel);
            String userData = playersData.get(user.userId());
            if (userData != null) {
                writeDataToUserChannels(channel, userData);
            }
        }
        flushAllData();
    }

    private void processMessage(ChannelHandlerContext ctx, Object msg) throws InterruptedException, JsonProcessingException {
        if (!(msg instanceof TextWebSocketFrame)) return;
        String json = ((TextWebSocketFrame) msg).text();
        Channel channel = ctx.channel();
        if (!userChannels.containsKey(channel)) {
            User user = ctx.channel().attr(WSAttributes.USER).get();
            if (user == null) {
                ctx.writeAndFlush(new TextWebSocketFrame("Authentication failed"));
                return;
            }
            registerUser(channel, user);
            String user_id = user.userId();
            gameStateService.addUser(user_id);
//            json = String.format(GET_USER, user_id);
        }
        json = addUserIdToMessage(json,
                ctx.channel().attr(WSAttributes.USER).get().userId());
        PlayerEvent event = convertMessageToPlayerEvent(json);
        eventInputQueue.put(event);
    }

    private String addUserIdToMessage(String json, String userId) {
        Map<String, Object> jsonMap = WSJsonUtil.fromJson(json);
        jsonMap.put("user_id", userId);
        return WSJsonUtil.toJsonObj(jsonMap).orElse("");
    }

    private void registerUser(Channel channel, User user) {
        if (channel != null && channel.isActive() && user != null) {
            userChannels.put(channel, user);
        }
    }

    private PlayerEvent convertMessageToPlayerEvent(String message) throws JsonProcessingException {
        return JsonUtil.jsonToPlayerEvent(message);
    }

    private void writeDataToUserChannels(Channel channel, String userData) throws JsonProcessingException, InterruptedException {
        if (!channel.isActive()) {
            removeUserFromGameStateService(userChannels.get(channel));
            userChannels.remove(channel);
            return;
        }

        channel.write(new TextWebSocketFrame(userData))
                .addListener(future -> {
                    if (!future.isSuccess()) {
                        Throwable cause = future.cause();
                        channel.close();
                        userChannels.remove(channel);
                    }
                });
    }

    private void removeUserFromGameStateService(User user) throws JsonProcessingException, InterruptedException {
        if (user != null) {
            gameStateService.removeUser(user.userId());
            eventInputQueue.put(convertMessageToPlayerEvent(WSJsonUtil.buildLogoutMessage(user)));
        }
    }

    private void flushAllData() {
        for (Channel channel : userChannels.keySet()) {
            if (channel.isActive()) {
                channel.flush();
            } else {
                userChannels.remove(channel);
            }
        }
//        isUserSentEvent.clear();
    }
}
