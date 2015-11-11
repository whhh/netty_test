package subscript;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2015/11/11 0011.
 */
@SuppressWarnings("all")
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {
    public SubReqClientHandler(){
        System.out.println("client init");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
            System.out.println("发送到server【"+subReq(i) + "】");
        }
        ctx.flush();
    }

    private SubscribeReq subReq(int i){
        SubscribeReq req = new SubscribeReq();
        req.setAddress("湖南邵阳五峰铺公园");
        req.setPhoneName("13832932209");
        req.setProductName("netty 权威指南");
        req.setSubReqID(i);
        req.setUserName("Lilinfeng");
        return req;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg)throws  Exception{
        System.out.println("Receive server response : 【" + msg + "】");
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
