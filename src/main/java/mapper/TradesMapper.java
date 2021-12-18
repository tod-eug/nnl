package mapper;

import bot.TaxBot;
import dto.TradeRaw;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradesMapper {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public TradeRaw mapTrade(String instrument, String currency, Element element) {
        Elements tds = element.select("td");

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss").parse(tds.get(1).ownText());
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Error while parsing dates in trades. Exception: ", e);
        }

        double closePrice = 0.0;
        double basis = 0.0;
        double realizedPL = 0.0;
        ArrayList<String> code = new ArrayList<>();
        if (!tds.get(4).ownText().equals(""))
            closePrice = Double.parseDouble(tds.get(4).ownText().replace(",", ""));
        if (!tds.get(7).ownText().equals(""))
        basis = Double.parseDouble(tds.get(7).ownText().replace(",", ""));
        if (!tds.get(8).ownText().equals(""))
        realizedPL = Double.parseDouble(tds.get(8).ownText().replace(",", ""));
        if (!tds.get(10).ownText().equals(""))
        code = new ArrayList<>(Arrays.asList(tds.get(10).ownText().split(";")));

        double quantity = Double.parseDouble(tds.get(2).ownText().replace(",", ""));
        double tradePrice = Double.parseDouble(tds.get(3).ownText().replace(",", ""));
        double sum = Double.parseDouble(tds.get(5).ownText().replace(",", ""));
        double commission = Double.parseDouble(tds.get(6).ownText().replace(",", ""));
        double mtmPL = Double.parseDouble(tds.get(9).ownText().replace(",", ""));

        return new TradeRaw(instrument,
                currency,
                tds.get(0).ownText(),
                date,
                quantity,
                tradePrice,
                closePrice,
                sum,
                commission,
                basis,
                realizedPL,
                mtmPL,
                code);
    }
}
