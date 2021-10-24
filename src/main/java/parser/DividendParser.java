package parser;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dto.Dividend;
import mapper.DividendMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DividendParser {

    public static ArrayList<Dividend> parseDividends(Document document) {

        ListMultimap<String, Element> currencyRowMap = ArrayListMultimap.create();
        String currency = null;

        //get arrayList of row from dividend table
        Elements tbody = document.selectXpath("//div[starts-with(@id, 'tblChangeInDividend')]//table//tbody");
        Elements trs = tbody.select("tr");

        //relate currency and rows, put it in ListMultimap
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
            currencyRowMap.put(currency, tr);
        }

        //map rows to dividends dto
        ArrayList<Dividend> dividends = new ArrayList<>();
        Set<String> currencies = currencyRowMap.keySet();
        for (String cur:currencies) {
            List<Element> dividendRows = currencyRowMap.get(cur);
            for (Element element:dividendRows) {
                dividends.add(DividendMapper.mapDividend(cur, element));
            }
        }

        return dividends;
    }
}
