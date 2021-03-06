package mapper;

import bot.TaxBot;
import dto.FeesRaw;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeesMapper {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public FeesRaw mapFees(String currency, Element element) {
        Elements tds = element.select("td");

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(tds.get(0).ownText());
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Error while parsing dates in fees. Exception: ", e);
        }

        String description = tds.get(1).ownText();
        double amount = Double.parseDouble(tds.get(2).ownText().replace(",", ""));

        return new FeesRaw(currency, date, description, amount);
    }
}
