package util;

import bot.TaxBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataProvider {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public static Document getDocument(File file) {
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8", "");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while parsing downloaded document. Exception: ", e);
        }
        return doc;
    }
}
