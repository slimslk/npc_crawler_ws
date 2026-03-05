package net.dimmid.ws.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.dimmid.ws.service.WebClientMessageService;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<Object> {

    private final WebClientMessageService webClientMessageService;

    public WebSocketFrameHandler(WebClientMessageService webClientMessageService) {
        this.webClientMessageService = webClientMessageService;
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        webClientMessageService.closeChannel(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws JsonProcessingException, InterruptedException {
        System.err.println("Error for channel: " + ctx.channel() + ", cause: " + cause);
        webClientMessageService.closeChannel(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame) {
            webClientMessageService.addMessageToQueue(ctx, msg);
        }
    }
}
