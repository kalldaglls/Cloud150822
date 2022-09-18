import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestRandomAccessFromNotFile {
    public static void main(String[] args) throws IOException {
        OutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("DZ_1.txt"));
        RandomAccessFile randomAccessFile = new RandomAccessFile("_DZ_1.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        RandomAccessFile randomAccessFile1 = new RandomAccessFile("file_to_fill.txt", "rw");
        FileChannel fileChannel1 = randomAccessFile1.getChannel();

        fileChannel1.transferFrom(fileChannel, 0, fileChannel.size());

        ByteBuffer buffer = ByteBuffer.allocate(10);

        int readBytes = fileChannel.read(buffer);
    }
}
