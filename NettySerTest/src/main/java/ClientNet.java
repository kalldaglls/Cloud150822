import common.dto.GetFilesListRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientNet {
    private static final int MAX_OBJECT_SIZE = 20 * 1_000_000;

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress("localhost", 7887)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(
                        new ObjectDecoder(MAX_OBJECT_SIZE, ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder(),
                                new ClientHandler()
                                );
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect().sync();
        Channel channel = channelFuture.channel();

        GetFilesListRequest getFilesListRequest = new GetFilesListRequest("Bogdan:1234", "C:\\Users\\Тереза\\Desktop\\JAVA lessons\\Q2\\Облачное new\\Les_3\\file-warehouse-7b0c6be14854bba82441e7d6294b2c981aa61e55");
        channel.writeAndFlush(getFilesListRequest);
        channelFuture.channel().closeFuture().sync();
//        sendText(bootstrap);
//       // sendFile(bootstrap);
    }

    private static void sendText(Bootstrap bootstrap) throws InterruptedException, IOException {
        ChannelFuture channelFuture = bootstrap.connect().sync();
        Channel channel = channelFuture.channel();
        ByteBuf buffer = channel.alloc().buffer();

        buffer.writeBytes("Hello!".getBytes(StandardCharsets.UTF_8));
        System.out.println(buffer.getByte((char) 1));
        System.out.println(channel.writeAndFlush(buffer));
        System.out.println(channel.read());
        channelFuture.channel().closeFuture().sync();
    }

    private static void sendFile(Bootstrap bootstrap) throws InterruptedException, IOException {
        ChannelFuture channelFuture = bootstrap.connect().sync();
        Channel channel = channelFuture.channel();
        ByteBuf buffer = channel.alloc().buffer();

        Path path = Paths.get("DZ_1.txt");
        String filename = path.getFileName().toString();
        int filenameLength = filename.length();
        byte[] bytesFromFile = Files.readAllBytes(path);

        //правильно ли так запихивать файл в буффер?
        //buffer.writeBytes(filename.getBytes());
        //    channel.writeAndFlush(buffer);
        //buffer.writeInt(filenameLength);
        buffer.writeBytes(bytesFromFile);
        System.out.println(buffer);
        channel.writeAndFlush(buffer);

//        buffer.writeBytes("Server hello".getBytes(StandardCharsets.UTF_8));
//        channel.writeAndFlush(buffer);

        channelFuture.channel().closeFuture().sync();
    }
}
