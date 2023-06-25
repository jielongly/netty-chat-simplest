package org.jl.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap=new Bootstrap();
        EventLoopGroup group=new NioEventLoopGroup();
        ChannelFuture future = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer())
                .connect(new InetSocketAddress("localhost", 8080));
        Executors.newSingleThreadExecutor().submit(()->{
            Scanner s=new Scanner(System.in);
            while (s.hasNext()){
                String body = s.next();
                future.channel().writeAndFlush(body);
            }
        });
        future.sync().channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
