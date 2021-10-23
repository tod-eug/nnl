import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class Parser {

    public void parseData() {
        File file = new File("U3434582.htm");
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element divDividends = doc.getElementById("tblCombDiv_U3434582Body");
        Elements tableDividends = divDividends.getElementsByClass("table-bordered");
        Elements tbodyDividends = tableDividends.get(0).select("tbody");
        Elements td = tbodyDividends.select("td");
        Element test = td.get(0);
        String currency = test.text();


    }
}
