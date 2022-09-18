import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCountUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

//обработчик входящих соединений!!! объясняем проге, что делать с посылками!!!
public class ServerHandler2 extends ChannelInboundHandlerAdapter {
    private ByteBuf totalBuf;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {//Запускается при соединении клиента!
        System.out.println(ctx.channel().remoteAddress());
        totalBuf = ctx.alloc().buffer();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {//Запускается, когда с сокета можно что-то прочитать от клая
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws FileNotFoundException {//Запускается при
        ByteBuf in = (ByteBuf) msg;
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("_НОВАЯ ВИДЕО-ИНСТРУКЦИЯ ПО ДС.mp4", true))) {
            while (in.readableBytes() > 0) {
                out.write(in.readByte());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        in.release();
    }
}

