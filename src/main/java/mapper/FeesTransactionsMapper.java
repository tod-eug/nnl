package mapper;

import bot.TaxBot;
import dto.FeesTransactionsRaw;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeesTransactionsMapper {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public FeesTransactionsRaw mapFeesTransactions(String instrument, String currency, Element element) {
        Elements tds = element.select("td");

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss").parse(tds.get(0).ownText());
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Error while parsing dates in fees transactions. Exception: ", e);
        }

        double quantity = 0.0;
        double tradePrice = 0.0;
        double amount = 0.0;
        ArrayList<String> code = new ArrayList<>();
        if (!tds.get(3).ownText().equals(""))
            quantity = Double.parseDouble(tds.get(3).ownText().replace(",", ""));
        if (!tds.get(4).ownText().equals(""))
            tradePrice = Double.parseDouble(tds.get(4).ownText().replace(",", ""));
        if (!tds.get(5).ownText().equals(""))
            amount = Double.parseDouble(tds.get(5).ownText().replace(",", ""));
        if (!tds.get(6).ownText().equals(""))
            code = new ArrayList<>(Arrays.asList(tds.get(6).ownText().split(";")));

        String ticker = tds.get(1).ownText();
        String description = tds.get(2).ownText();

        return new FeesTransactionsRaw(instrument, currency, date, ticker, description, quantity, tradePrice, amount, code);
    }
}
