package us.hemdgang.autoreward;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//	ByteBuf in = (ByteBuf) msg;
	
//	try {
//	    System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
	    
	    ctx.write(msg);
	    ctx.flush();
//	} finally {
//	    ReferenceCountUtil.release(msg);
//	}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	cause.printStackTrace();
	ctx.close();
    }
}
