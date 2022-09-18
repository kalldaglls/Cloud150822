package sidefileexchange;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * @decription: TODO
 * @author: PENGPANTING740
 * @date: 2019/4/25 16:59
 */
public class Client {
    private static String f = "C:\\JAVA\\IdeaProjects\\DropBoxNew\\SideEffect\\client-dir\\File.png";
    private static String ip = "localhost";
    private static int port = 8888;
    public static void main(String[] args) throws Exception {
//        ip = "192.168.5.19";
//        port = 8889;
        if (Objects.nonNull(args) && args.length > 0) {
            ip = args[0];
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
                if (args.length > 2) {
                    f = args[2];
                }
            }
        }
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(true);
        sc.connect(new InetSocketAddress(ip, port));
        File file = new File(f);
        String fileName = file.getName();
        FileInputStream fis = new FileInputStream(file);
        int bodyLength = fis.available();
        byte[] body = new byte[bodyLength];
        int totalLength = 4 + 2 + fileName.length() + bodyLength;
        ByteBuffer buffer = ByteBuffer.allocate(totalLength);//totalLength,fileNameLength,
        buffer.putInt(totalLength);
        buffer.putShort((short)fileName.length());
        buffer.put(fileName.getBytes());
        buffer.put(body);
        buffer.flip();
        sc.write(buffer);
        fis.close();
        sc.close();
    }
}

