package mapper;

import bot.TaxBot;
import dto.InterestRaw;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterestMapper {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public InterestRaw mapInterest(String currency, Element element) {
        Elements tds = element.select("td");

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(tds.get(0).ownText());
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Error while parsing dates in interest. Exception: ", e);
        }

        String description = tds.get(1).ownText();
        double amount = Double.parseDouble(tds.get(2).ownText().replace(",", ""));

        return new InterestRaw(currency, date, description, amount);
    }
}
