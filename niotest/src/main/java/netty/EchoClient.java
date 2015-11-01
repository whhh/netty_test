package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


/**
 * Created by Administrator on 2015/11/1 0001.
 */
public class EchoClient {
    private int post;
    private final String host;
    public EchoClient(String host,int post){
        this.post=post;
        this.host=host;
    }

    /**
     *
     * @throws Exception
     * 连接服务器
     * 谢数据到服务器
     * 等待接受服务器返回相同数据
     * 关闭连接
     *
     * 创建Bootstrap对象用来引导启动客户端
     * 创建EventLoopGroup对象并设置到Bootstrap中，EventLoopGroup可以理解为是一个线程池，
     * 这个线程池用来处理连接，接受数据、发送数据。
     * 创建InetSocketAddress并设置到Bootstrap中，InetSocketAddress是指定连接的服务器地址
     * 添加一个ChannelHandler，客户端成功连接服务器后就会被执行
     * 调用Bootstrap.connect()来连接服务器
     * 最后关闭EventLoopGroup来释放资源
     */
    public void start()throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host,post)
            ).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new EchoClientHandler());
                }
            });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully().sync();
        }



    }

    public static void main(String[] args)throws Exception{
        new EchoClient("localhost",65535).start();
    }
}
