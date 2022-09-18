package sidefileexchange;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;

/**
 * @DECPIPPORT: протокол = 4: 2 (FileNAMELLENG): переменная (имя файла): переменная (тело)
 * @author: PENGPANTING740
 * @date: 2019/4/25 16:08
 */
public class Server {

    private static String dir = "C:\\JAVA\\IdeaProjects\\DropBoxNew\\SideEffect\\server-dir";
    private static int port = 8888;
    public static void main(String[] args) throws Exception {
        if (Objects.nonNull(args) && args.length > 0) {
            dir = args[0];
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
        }
        System.out.println("dir is: " + dir);
        System.out.println("Server start, and listen port is: " + port);
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    System.out.println("isAcceptable");
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    System.out.println("isReadable");
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(4);
                    int length = read(socketChannel, buffer);
                    if (length > -1) {
                        buffer.flip();
                        int totalLength = buffer.getInt();
                        System.out.println("total length is: " + totalLength);
                        ByteBuffer body = ByteBuffer.allocate(totalLength - 4);
                        length = read(socketChannel, body);
                        if (length > -1) {
                            body.flip();
                            short fileNameLength = body.getShort();
                            byte[] fileName = new byte[fileNameLength];
                            body.get(fileName);
                            System.out.println("file name is: " + new String(fileName));
                            byte[] fileBody = new byte[totalLength - 4 - 2 - fileNameLength];
                            body.get(fileBody);
                            writeToFile(fileName, fileBody);
                        }
                    } else {
                        socketChannel.close();
                    }
                }
                it.remove();
            }
        }
    }

    private static int read(SocketChannel channel, ByteBuffer buffer) throws Exception {
        int length = -1;
        while (buffer.hasRemaining()) {
            length = channel.read(buffer);
            if (length < 0) {
                break;
            }
            Thread.sleep(5);
        }
        return length;
    }


    private static void writeToFile(byte[] fileName, byte[] content) throws Exception {
        String name = new String(fileName);
        FileOutputStream fos = new FileOutputStream(dir + File.separator + name);
        fos.write(content);
        fos.close();
    }
}

