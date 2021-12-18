package util;

import bot.TaxBot;
import com.itextpdf.text.Document;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilesUtils {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public static final String rawFolder = "raw/";
    public static final String processedFolder = "processed/";

    public File saveRawFile(File file, String fileName) {
        File dir = new File(rawFolder);
        boolean isCreated = dir.mkdirs();
        File copied = new File(rawFolder + fileName);
        try {
            FileUtils.copyFile(file, copied);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while saveRawFile. Exception: ", e);
        }
        return copied;
    }

    public File writeXlsFile(XSSFWorkbook workbook, String fileName) {
        File dir = new File(processedFolder);
        boolean isCreated = dir.mkdirs();
        try (FileOutputStream outputStream = new FileOutputStream(processedFolder + fileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while writeXlsFile. Exception: ", e);
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
