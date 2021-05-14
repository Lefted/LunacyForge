package us.hemdgang.autoreward.labyconnect;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private ClientConnection clientConnection;

    public ClientChannelInitializer(ClientConnection clientConnection) {
	this.clientConnection = clientConnection;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
	this.clientConnection.setNioSocketChannel(ch);
	ch.pipeline().addLast("timeout", (ChannelHandler) new ReadTimeoutHandler(120L, TimeUnit.SECONDS)).addLast("splitter",
	    (ChannelHandler) new PacketPrepender()).addLast("decoder", (ChannelHandler) new PacketDecoder()).addLast("prepender",
		(ChannelHandler) new PacketSplitter()).addLast("encoder", (ChannelHandler) new PacketEncoder()).addLast(new ChannelHandler[] {
			(ChannelHandler) this.getClientConnection() });
	;

    }

    public ClientConnection getClientConnection() {
	return this.clientConnection;
    }
}
