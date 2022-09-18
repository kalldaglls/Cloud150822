package common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToStringDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        StringBuilder stringBuffer = new StringBuilder();
        while (byteBuf.isReadable()) {
            stringBuffer.append((char) byteBuf.readByte());
        }
        list.add(stringBuffer.toString());
    }
}
