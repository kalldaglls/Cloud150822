import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCountUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

//обработчик входящих соединений!!! объясняем проге, что делать с посылками!!!
public class ServerHandler extends ChannelInboundHandlerAdapter {
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) {//Запускается при
        ByteBuf in = (ByteBuf) msg;//как просто принять буффер и записать из него файл(в файл?)?
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        ctx.fireChannelRead(data);
//        try {
//////            if (!in.isReadable()) {
//////                in.retain(1);
//////            }
//////            while (in.isReadable()) {
//////                System.out.print((char) in.readByte());
//////            }
//////            in.release();
////
////              in.readBytes(data);
////        } catch (IllegalReferenceCountException e) {
////            e.printStackTrace();
////        }
////        finally {
////            ctx.fireChannelRead(data);
////            ReferenceCountUtil.release(msg);
////        }
//        if (in.isReadable()){//Зачем в ServerHandler создаем два ByteBuf, если можно выливать из одного?
//            in.readBytes(totalBuf);
//        }
//
//        byte[] array = totalBuf.array();
//        System.out.println(new String(array));

        //File file = Files.createFile(Paths.get(totalBuf.))

    }
}
