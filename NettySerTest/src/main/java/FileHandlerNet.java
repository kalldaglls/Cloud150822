import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;

public class FileHandlerNet extends ChannelInboundHandlerAdapter {
    private ByteBuf totalBuf;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuf byteBuf = (ByteBuf) msg;
        //System.out.println(byteBuf.array());
        if (byteBuf.isReadable()){
            byteBuf.readBytes(totalBuf);
        }

        System.out.println(totalBuf);
//        byte[] arr = (byte[]) msg;
//
//        Files.write(Paths.get("_НОВАЯ ВИДЕО-ИНСТРУКЦИЯ ПО ДС.mp4"),arr);
//        System.out.println("Сообщение записано в файл");
//        ctx.writeAndFlush("Java\n");
//        File file = new File("_НОВАЯ ВИДЕО-ИНСТРУКЦИЯ ПО ДС.mp4");
//        RandomAccessFile file1 = new RandomAccessFile(file, "rw");
//        ctx.writeAndFlush(new DefaultFileRegion(file1.getChannel(), 0, file1.length()));
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < 8; i++) {
//            stringBuilder.append((char) arr[i]);
//        }
//        System.out.println(stringBuilder);
//        String _filename = new String("_" + stringBuilder);

//        File f = new File("new_File.png");
//        RandomAccessFile fw = new RandomAccessFile(f, "rw");
//        fw.write(arr);
//        fw.close();



//       File path = new File("_filename.mp4");
//
//        ByteBuffer byteBuffer = ByteBuffer.allocate(arr.length);
//        byteBuffer.put(arr);
//
//        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
//        FileChannel fileChannel = randomAccessFile.getChannel();
//        byteBuffer.flip();
//
//        fileChannel.write(byteBuffer);
//        System.out.println("Сообщение записано в файл");
//        ctx.writeAndFlush("Java\n");

    }
}

