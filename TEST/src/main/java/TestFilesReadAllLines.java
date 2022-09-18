import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFilesReadAllLines {
    public static void main(String[] args) throws IOException {
        System.out.println(Files.readAllLines(Paths.get("DZ_1.txt")));
    }
}
