package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IBReportValidator {

    public boolean isFileIBReport(File file) {

        Path path = Paths.get(file.getPath());
        String string = null;
        try {
            string = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (string.contains(">Interactive Brokers LLC, Two Pickwick Plaza, Greenwich, CT 06830</p>"))
            return true;
        return false;
    }
}
