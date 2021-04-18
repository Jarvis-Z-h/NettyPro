package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author jarvis
 * @date 2021/4/18 0018 22:53
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //创建 BossGroup 和 WorkGroup
        /**
         * 说明：
         * 1.创建两个线程组 bossGroup 和 workGroup
         * 2.bossGroup 只是处理连接请求，真正和客户端业务处理，会交给 workGroup 完成
         * 3.两个都是无限循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        //使用链式编程来进行设置
        bootstrap.group(bossGroup, workGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class)//使用 NioServerSocketChannel 作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象（匿名对象）
                    //给 pipeline 设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(null);
                    }
                });//给我们的 workGroup 的 EventLoop 对应的管道设置处理器


        System.out.println("Server is reading....");

        //绑定一个端口并且同步，生成一个 ChannelFuture 对象
        //启动服务器（并绑定端口）
        ChannelFuture cf = bootstrap.bind(6668).sync();

        //对关闭通道进行监听
        cf.channel().closeFuture().sync();
    }
}
