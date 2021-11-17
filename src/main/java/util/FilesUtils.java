package util;

import com.itextpdf.text.Document;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilesUtils {

    public static final String rawFolder = "raw/";
    public static final String processedFolder = "processed/";

    public File saveRawFile(File file, String fileName) {
        File dir = new File(rawFolder);
        boolean isCreated = dir.mkdirs();
        File copied = new File(rawFolder + fileName);
        try {
            FileUtils.copyFile(file, copied);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copied;
    }

    public File writeXlsFile(XSSFWorkbook workbook, String fileName) {
        File dir = new File(processedFolder);
        boolean isCreated = dir.mkdirs();
        try (FileOutputStream outputStream = new FileOutputStream(processedFolder + fileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(processedFolder + fileName);
        return file;
    }

    public File writePdfFile(String fileName) {
        File dir = new File(processedFolder);
        boolean isCreated = dir.mkdirs();
        File file = new File(processedFolder + fileName);
        return file;
    }
}
