package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2015/11/1 0001.
 */
public class EchoServer {
    private int post;
    public  EchoServer(int post){
        this.post=post;
    }

    public void start() throws Exception{
        /**
         * 创建ServerBootstrap实列来引导绑定和启动服务器
         * 创建NioEventLoopGroup对象来处理事件，如接受新连接、接受数据、写数据等等
         * 指定InetSocketAddress,服务监听此端口
         * 设置chidHandler执行所有连接请求
         * 都设置完毕了，最后调用ServerBootstrap.bind()方法绑定服务器
         */
        EventLoopGroup  group = new NioEventLoopGroup();
        try {
            //创建ServerBootstrap 对象
            ServerBootstrap b = new ServerBootstrap();
            /**
             * 指定通道类型为NioServerSocketChannel，设置inetSocketAddress
             * 让服务监听某个端口已等待客户端连接。
             * 调用childHandler放来指定连接后调用的ChannelHandler，这个方法ChannelInitializes
             * 参数类型，ChannelInitializer是个抽象类所以需要实现initChannel方法，这个方法
             * 就是用来设置ChannelHandler。
             *
             */
            b.group(group).channel(NioServerSocketChannel.class).localAddress(post)
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            /**
             * 绑定服务器等待直到绑定完成，调用sync()方法会阻塞知道服务器绑定完成，
             * 然后服务器等待通道关闭，应为使用sync(),所以关闭操作也
             * 会被阻塞。
             * 关闭EventLoopGroup和释放所有资源。包括传教的线程
             */
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() + "  started and listen on" + f.channel().localAddress());
            f.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)throws Exception{
        new EchoServer(65535).start();
    }
}
