package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class DataProvider {

    public static Document getDocument() {
        File file = new File("U3434582.htm");
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8", "");
        } catch (IOException e) {
            System.out.println("File not found or parser error");
            e.printStackTrace();
        }
        return doc;
    }
}
