package mapper;

import dto.ExchangeRate;
import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExchangeRatesMapper {

    public ExchangeRate mapExchangeRate(Element element) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(element.getAttribute("Date"));
        } catch (ParseException e) {
            System.out.println("Couldn't parse date");
            e.printStackTrace();
        }

        int nominal = Integer.parseInt(element.getElementsByTagName("Nominal").item(0).getTextContent());
        double value = Double.parseDouble(element.getElementsByTagName("Value").item(0).getTextContent().replace(",", "."));

        return new ExchangeRate(date, nominal, value);
    }
}
