package net.dimmid.ws.handler;

import io.jsonwebtoken.JwtException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.dimmid.ws.entity.User;
import net.dimmid.ws.util.JwtUtil;
import net.dimmid.ws.util.WSAttributes;
import net.dimmid.ws.util.WSJsonUtil;

import java.util.Map;


public class WebSocketAuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        try {
            String token = getAuthToken(request);
            if (token == null) {
                sendUnauthorized(ctx, "JWT token is invalid or expired");
            }
            String userId = JwtUtil.validateAndGetUserId(token);
            User user = new User(userId);
            ctx.channel().attr(WSAttributes.USER).set(user);
            ctx.fireChannelRead(request.retain());
        } catch (JwtException ex) {
            sendUnauthorized(ctx, "JWT token is invalid or expired");
        }

    }

    private String getAuthToken(FullHttpRequest request) {
        String token = request.headers().get(HttpHeaderNames.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private void sendUnauthorized(ChannelHandlerContext ctx, String message) {
        try {
            Map<String, String> responseMap = Map.of(
                    "code", "Websocket auth handler",
                    "message", message,
                    "status_code", "401"
            );
            String res = WSJsonUtil.toJson(responseMap).orElseThrow(() -> new JwtException("Response is null"));
            sendResponse(ctx, res);
        } catch (JwtException ex) {
            sendResponse(ctx, "{\"code\": \"Websocket auth handler\"," +
                    " \"message\": \"JWT token is invalid or expired\"," +
                    " \"status_code\": \"401\"}");
        }
    }

    private void sendResponse(ChannelHandlerContext ctx, String message) {
        ByteBuf content = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.UNAUTHORIZED,
                content
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
