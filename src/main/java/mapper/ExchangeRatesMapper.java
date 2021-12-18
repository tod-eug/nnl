package mapper;

import bot.TaxBot;
import dto.ExchangeRate;
import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExchangeRatesMapper {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    public ExchangeRate mapExchangeRate(Element element) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(element.getAttribute("Date"));
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Error while parsing dates in exchange rates. Exception: ", e);
        }

        int nominal = Integer.parseInt(element.getElementsByTagName("Nominal").item(0).getTextContent());
        double value = Double.parseDouble(element.getElementsByTagName("Value").item(0).getTextContent().replace(",", "."));

        return new ExchangeRate(date, nominal, value);
    }
}
