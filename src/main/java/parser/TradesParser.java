package parser;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dto.TradeRaw;
import mapper.TradesMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class TradesParser {

    public ArrayList<TradeRaw> parseTrades(Document document) {

        ArrayList<TradeRaw> tradeRaw = new ArrayList<>();

        ListMultimap<String, Element> equityRowMap = ArrayListMultimap.create();
        ListMultimap<Map<String, String>, Element> equityCurrencyRowMap = ArrayListMultimap.create();
        String equityClass = null;
        String currency = null;

        //get arrayList of nodes tbody from trades table
        Elements tbody = document.selectXpath("//div[starts-with(@id, 'tblTransactions')]//table");

        if (tbody.size() > 0) {
            Elements nodes = tbody.get(0).children();

            //split equity by types into a ListMultimap
            for (Element e : nodes) {
                if (e.tag().toString().equals("thead")) {
                    continue;
                } else {
                    Elements tds = e.select("td");
                    if (tds.get(0).attr("class").equals("header-asset"))
                        equityClass = tds.get(0).ownText();
                    equityRowMap.put(equityClass, e);
                }
            }

            //add currency information and transform it into other ListMultimap<Map<equity, currency>, trs>
            Set<String> equities = equityRowMap.keySet();
            for (String equity : equities) {
                List<Element> tbs = equityRowMap.get(equity);
                for (Element tb : tbs) {
                    Elements trs = tb.select("tr");
                    Elements tds = tb.select("td");
                    if (tds.get(0).attr("class").equals("header-asset"))
                        continue;
                    if (tds.get(0).attr("class").equals("header-currency")) {
                        currency = tds.get(0).ownText();
                        continue;
                    }
                    if (trs.get(0).attr("class").startsWith("row-summary")) {
                        Map<String, String> equityCurrency = new HashMap<>();
                        equityCurrency.put(equity, currency);
                        equityCurrencyRowMap.put(equityCurrency, trs.get(0));
                    }
                }
            }

            //map rows to tradesRaw dto Map<equity, TradeRaw>
            TradesMapper tradesMapper = new TradesMapper();
            //get Maps with <equity currency>
            Set<Map<String, String>> maps = equityCurrencyRowMap.keySet();
            //iterate by equity
            for (Map<String, String> m : maps) {
                Set<String> equitiesSet = m.keySet();
                //iterate by currency
                for (String s : equitiesSet) {
                    List<Element> tradeRows = equityCurrencyRowMap.get(m);
                    //iterate by trs
                    for (Element tradeRow : tradeRows) {
                        tradeRaw.add(tradesMapper.mapTrade(s, m.get(s), tradeRow));
                    }
                }
            }
        }
        return tradeRaw;
    }
}
