package subscript;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Administrator on 2015/11/11 0011.
 */
@SuppressWarnings("all")
public class SubReqServer {

    public void bind(int port) throws Exception{
        //���÷���˵�NIO�߳���
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ObjectDecoder(
                                            1024 * 1024,
                                            ClassResolvers
                                                    .weakCachingConcurrentResolver(this
                                                            .getClass()
                                                            .getClassLoader())));
                                   ch.pipeline().addLast(new ObjectEncoder());
                                   ch.pipeline().addLast(new SubReqServerHandler());
                        }
                    });
            //�󶨶˿ڣ�ͬ���ȴ��ɹ���
            ChannelFuture f = b.bind(port).sync();
            //�ȴ�����˼����˿ڹر�
            f.channel().closeFuture().sync();
        }catch (Exception e){
            //
        }finally {
            //�ͷ��̳߳���Դ
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if(args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (Exception e){
                //����Ĭ��ֵ
            }

        }
        new SubReqServer().bind(port);
    }


}
