package parser;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import dto.FeesRaw;
import mapper.FeesMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FeesParser {

    public ArrayList<FeesRaw> parseFees(Document document) {

        ArrayList<FeesRaw> fees = new ArrayList<>();

        ListMultimap<String, Element> currencyRowMap = ArrayListMultimap.create();
        String currency = null;

        Elements tbody = document.selectXpath("//div[starts-with(@id, 'tblCombFees')]//table//tbody");

        if (tbody.size() > 0) {
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
                if (tds.get(0).attr("colspan").equals(""))
                    currencyRowMap.put(currency, tr);
            }

            //map rows to feesRaw dto
            FeesMapper feesMapper = new FeesMapper();
            Set<String> currencies = currencyRowMap.keySet();
            for (String cur:currencies) {
                List<Element> feesRows = currencyRowMap.get(cur);
                for (Element element:feesRows) {
                    fees.add(feesMapper.mapFees(cur, element));
                }
            }
        }
        return fees;
    }
}
