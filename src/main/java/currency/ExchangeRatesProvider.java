package currency;

import dto.ExchangeRate;
import mapper.ExchangeRatesMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.CentralBankDataProvider;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRatesProvider {

    public static Map<Date, ExchangeRate> getExchangeRates(String currency, Date start, Date end) {

        Map<Date, ExchangeRate> result = new HashMap<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;

        //parse result
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(CentralBankDataProvider.getExchangeRatesRange(currency, start, end));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        //get root element
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();

        //get list of objects Record
        NodeList nodelist = root.getElementsByTagName("Record");

        //map to the ExchangeRate dto
        for (int i = 0; i < nodelist.getLength(); i++) {
            ExchangeRate exchangeRate = ExchangeRatesMapper.mapExchangeRate((Element) nodelist.item(i));
            result.put(exchangeRate.getDate(), exchangeRate);
        }
        return result;
    }
}
