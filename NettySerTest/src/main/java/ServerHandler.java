import common.dto.BasicRequest;
import common.dto.BasicResponse;
import common.handler.HandlerRegistry;
import common.handler.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        BasicRequest request = (BasicRequest) msg;
        RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
        BasicResponse response = handler.handle(request);
        channelHandlerContext.writeAndFlush(response);
    }  

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
