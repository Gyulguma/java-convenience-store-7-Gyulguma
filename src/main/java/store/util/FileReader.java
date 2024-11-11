package store.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private static final String ERROR_FILE_NOT_FOUND = "[ERROR] '%s' 파일을 찾을 수 없습니다.";

    public FileReader() {
    }

    public List<String> readFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format(ERROR_FILE_NOT_FOUND, filePath));
        }
        Scanner fileScanner = new Scanner(file);
        List<String> lines = new ArrayList<>();
        while (fileScanner.hasNext()) {
            lines.add(fileScanner.nextLine());
        }
        return lines;
    }
}
