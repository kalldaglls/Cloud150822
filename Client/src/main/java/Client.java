import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress("localhost", 7887)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new InboundClientHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect().sync();
        Channel channel = channelFuture.channel();
        ByteBuf buffer = channel.alloc().buffer();

        Path path = Paths.get("3.txt");
        String filename = path.getFileName().toString();
        int filenameLength = filename.length();
        byte[] bytesFromFile = Files.readAllBytes(path);

        //правильно ли так запихивать файл в буффер?
        buffer.writeBytes(filename.getBytes());
        channel.writeAndFlush(buffer);
        buffer.writeInt(filenameLength);
        buffer.writeBytes(bytesFromFile);
        channel.writeAndFlush(buffer);

        channelFuture.channel().closeFuture().sync();
    }
}
