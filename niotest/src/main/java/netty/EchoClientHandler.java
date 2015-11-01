package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by Administrator on 2015/11/1 0001.
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    /**
     *
     * @param ctx
     * channelActive();客户端连接服务器后被调用
     * channelRead0; 从服务器接受到数据后调用
     * exceptionCaught();发生异常时被调用
     */

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("netty client");
      ctx.write(Unpooled.copiedBuffer("Netty rocks !", CharsetUtil.UTF_8));
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        System.out.println("Client received: " + ByteBufUtil.hexDump(
                msg.readBytes(msg.readableBytes())
        ));
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx , Throwable cause){

        cause.printStackTrace();
        ctx.close();
    }
}
