package store.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SFileWriter {
    private static final String ERROR_FILE_NOT_FOUND = "[ERROR] '%s' 파일을 찾을 수 없습니다.";
    private static final String ERROR_FILE_EDIT = "[ERROR] 파일을 수정할 수 없습니다.";

    public SFileWriter() {
    }

    public void writeFile(String filePath, List<String> lines) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format(ERROR_FILE_NOT_FOUND, filePath));
        }
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            for (String line : lines) {
                fileWriter.write(line);
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_FILE_EDIT);
        }

    }
}
