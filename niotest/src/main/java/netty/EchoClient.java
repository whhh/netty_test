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
     * ���ӷ�����
     * л���ݵ�������
     * �ȴ����ܷ�����������ͬ����
     * �ر�����
     *
     * ����Bootstrap�����������������ͻ���
     * ����EventLoopGroup�������õ�Bootstrap�У�EventLoopGroup�������Ϊ��һ���̳߳أ�
     * ����̳߳������������ӣ��������ݡ��������ݡ�
     * ����InetSocketAddress�����õ�Bootstrap�У�InetSocketAddress��ָ�����ӵķ�������ַ
     * ���һ��ChannelHandler���ͻ��˳ɹ����ӷ�������ͻᱻִ��
     * ����Bootstrap.connect()�����ӷ�����
     * ���ر�EventLoopGroup���ͷ���Դ
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
