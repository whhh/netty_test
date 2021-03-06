package nettyoio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;


/**
 * Created by Administrator on 2015/11/2 0002.
 */
@SuppressWarnings("all")
public class NettyNioServer {
    public void server(int port)throws Exception{
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi\r\n", CharsetUtil.UTF_8));
        //事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //用来引导服务器配置
            ServerBootstrap b = new ServerBootstrap();
            // 使用NIO异步模式
            b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
            //指定ChannelInitializer初始化handlers
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //添加一个“入站”handler到ChannelPipelien
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                       @Override
                        public void channelActive(ChannelHandlerContext ctx)throws Exception{
                           //连接后，写信息到客户端，写完后便关闭连接
                           ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                       }
                    });
                }
            });
            //绑定服务器接受连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
            //释放所有资源
            group.shutdownGracefully();
        }
    }
}
