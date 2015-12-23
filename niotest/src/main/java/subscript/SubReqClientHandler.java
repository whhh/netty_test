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
      String str = "0008770143series=NjBBNDU3NkFBOUM1REUwMjQ5MjdBNjU0NTY3QzgyNzA4QjQxRDVGQkU5MDlGRDVGNkQ2RTkzNTI1ODY0MEE4ODUzMzc4QkZDMTVDMjhCOTU=&uid=010104&workday=20161029<SDOROOT><SYS_HEAD><SERVICE_CODE>02003000007</SERVICE_CODE><CONSUMER_ID>010104</CONSUMER_ID><SERVICE_SCENE>01</SERVICE_SCENE><CONSUMER_SEQ_NO>0101042015112552809216</CONSUMER_SEQ_NO><ORG_SYS_ID>010104</ORG_SYS_ID><TRAN_DATE>20151125</TRAN_DATE><TRAN_TIMESTAMP>125501040</TRAN_TIMESTAMP></SYS_HEAD><APP_HEAD><BRANCH_ID>9990</BRANCH_ID><USER_ID>99034</USER_ID><BIZ_SEQ_NO>52809216</BIZ_SEQ_NO></APP_HEAD><LOCAL_HEAD><RURAL_BRANCH_ID>0000</RURAL_BRANCH_ID><KEY_NAME>1</KEY_NAME><BUS_SEQ_NO>0101042015112552809216</BUS_SEQ_NO><CHANNEL_CODE>000013</CHANNEL_CODE><KEY_TYPE>2</KEY_TYPE></LOCAL_HEAD><BODY><CUST_NO>A0000577464</CUST_NO><CUST_NAME>TESTA0000577464</CUST_NAME><START_IDX>1</START_IDX><PAGE_NUM>10</PAGE_NUM></BODY></SDOROOT>\n";
        System.out.println(str);
            ctx.write(str);
            System.out.println("发送到server【"+str + "】");

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
