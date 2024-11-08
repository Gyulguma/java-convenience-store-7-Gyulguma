package store.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private static final String ERROR_FILE_NOT_FOUND = "[ERROR] '%s' 파일을 찾을 수 없습니다.";

    public FileReader() {
    }

    public List<String> readFile(String filePath) throws FileNotFoundException {
        InputStream inputStream = getClass().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FileNotFoundException(String.format(ERROR_FILE_NOT_FOUND, filePath));
        }
        Scanner fileScanner = new Scanner(inputStream);
        List<String> lines = new ArrayList<>();
        while (fileScanner.hasNext()) {
            lines.add(fileScanner.nextLine());
        }
        return lines;
    }
}
