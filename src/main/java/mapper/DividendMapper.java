package mapper;

import dto.DividendRaw;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DividendMapper {

    public DividendRaw mapDividend(String currency, Element element) {
        Elements tds = element.select("td");

        Date date = null;
        Date exDividendDate = null;
        Date paymentDate = null;
        ArrayList<String> code;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(tds.get(1).ownText());
            exDividendDate = new SimpleDateFormat("yyyy-MM-dd").parse(tds.get(2).ownText());
            paymentDate = new SimpleDateFormat("yyyy-MM-dd").parse(tds.get(3).ownText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int quantity = Integer.parseInt(tds.get(4).ownText().replace(",", ""));
        double tax = Double.parseDouble(tds.get(5).ownText().replace(",", "").replace("-", ""));
        double payment = Double.parseDouble(tds.get(6).ownText().replace(",", "").replace("-", ""));
        double divPerShare = Double.parseDouble(tds.get(7).ownText().replace(",", ""));
        double dividendGross = Double.parseDouble(tds.get(8).ownText().replace(",", "").replace("-", ""));
        double dividendNet = Double.parseDouble(tds.get(9).ownText().replace(",", "").replace("-", ""));
        code = new ArrayList<>(Arrays.asList(tds.get(10).ownText().split(";")));


        return new DividendRaw(currency,
                tds.get(0).ownText(),
                date,
                exDividendDate,
                paymentDate,
                quantity,
                tax,
                payment,
                divPerShare,
                dividendGross,
                dividendNet,
                code);
    }
}
