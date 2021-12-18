package util;

import bot.TaxBot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IBReportValidator {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public boolean isFileIBReport(File file) {

        Path path = Paths.get(file.getPath());
        String string = null;
        try {
            string = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while parsing downloaded document for validating. Exception: ", e);
        }
        if (string.contains(">Interactive Brokers LLC, Two Pickwick Plaza, Greenwich, CT 06830</p>"))
            return true;
        return false;
    }
}
