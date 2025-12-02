package net.dimmid.ws;

import io.netty.channel.*;
import net.dimmid.ws.service.WebClientMessageService;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<Object> {

    private final WebClientMessageService webClientMessageService;

    public WebSocketFrameHandler(WebClientMessageService webClientMessageService) {
        this.webClientMessageService = webClientMessageService;
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        webClientMessageService.closeChannel(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("Error for channel: " + ctx.channel() + ", cause: " + cause);
        webClientMessageService.closeChannel(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        webClientMessageService.addMessageToQueue(ctx, msg);
    }
}
