package netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2015/11/1 0001.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg)throws Exception{
        System.out.println("Server received:" +msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)throws Exception{
        System.out.println("is "+EchoServerHandler.class.getName() + "to channelReadComplete");
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(
                ChannelFutureListener.CLOSE
        );
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        System.out.println("is "+EchoServerHandler.class.getName() + "to channelReadComplete");
        cause.printStackTrace();
        ctx.close();
    }

}
