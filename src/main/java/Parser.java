import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
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

        ListMultimap<String, Element> dividends = ArrayListMultimap.create();
        String currency = null;

        Elements divDividends = doc.selectXpath("//div[starts-with(@id, 'tblChangeInDividend')]//table//tbody");
        Elements trs = divDividends.select("tr");

        for (Element tr:trs) {
            if (!tr.attr("class").equals(""))
                continue;
            Elements tds = tr.select("td");
            if (tds.get(0).attr("class").equals("header-asset"))
                continue;
            if (tds.get(0).attr("class").equals("header-currency")) {
                currency = tds.get(0).ownText();
                continue;
            }
            dividends.put(currency, tr);
        }


        Element test = trs.get(1);
        Elements tds = test.select("td");
        Element test1 = tds.get(0);
        String currency1 = test.text();


    }
}
