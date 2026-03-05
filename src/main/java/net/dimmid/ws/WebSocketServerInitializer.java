package net.dimmid.ws;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.channel.ChannelPipeline;
import net.dimmid.MainAppContext;
import net.dimmid.ws.handler.WebSocketAuthHandler;
import net.dimmid.ws.handler.WebSocketFrameHandler;
import net.dimmid.ws.service.WebClientMessageService;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketAuthHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));
        pipeline.addLast(new WebSocketFrameHandler(
                (WebClientMessageService) MainAppContext.getObject("webClientMessageService").orElseThrow())
        );
    }
}
