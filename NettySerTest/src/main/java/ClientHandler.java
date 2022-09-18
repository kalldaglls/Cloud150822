import common.dto.BasicResponse;
import common.dto.GetFilesListResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        BasicResponse response = (BasicResponse) msg;
        if (response instanceof GetFilesListResponse) {
            GetFilesListResponse getFilesListResponse = (GetFilesListResponse) response;
            getFilesListResponse.getList()
                    .forEach(System.out::println);
        } else {
            System.out.println(response.getErrorMessage());
        }
    }
}
