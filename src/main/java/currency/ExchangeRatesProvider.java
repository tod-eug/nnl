package currency;

import dto.ExchangeRate;
import mapper.ExchangeRatesMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.CentralBankDataProvider;
import util.DateUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class ExchangeRatesProvider {

    /**
     * Method return Map<Currency, Map<Date, ExchangeRate>> with exchange rates for necessary currencies in the necessary dates range
     * @param currencies - list of currencies
     * @param datesRange - range of the dates for which we need exchange rates for all currencies
     * @return map with exchange rates for all currencies
     */
    public Map<String, Map<Date, ExchangeRate>> getExchangeRates(List<String> currencies, Map<String, Date> datesRange) {
        Map<String, Map<Date, ExchangeRate>> exchangeRates = new HashMap<>();

        for (String l: currencies) {
            Document doc = requestExchangeRates(l, datesRange.get("from"), datesRange.get("to"));
            exchangeRates.put(l, parseDocument(doc));
        }
        return exchangeRates;
    }

    /**
     * Method check if the map contains necessary date. If it doesn't then it finds the closest previous date
     * with the exchangeRate
     *
     * @param necessaryDate - necessary date
     * @param rates - map with exchange rates
     * @return adjusted date, which guaranteed present in the map
     */
    public Date adjustExchangeRateDate(Date necessaryDate, Map<Date, ExchangeRate> rates)  {
        DateUtil dateUtil = new DateUtil();
        int timer = 0;
        while (!rates.containsKey(necessaryDate)) {
            timer++;
            necessaryDate = dateUtil.increaseDate(necessaryDate, -1);
            if (timer > 15)
                return dateUtil.increaseDate(necessaryDate, timer+1);
        }
        return necessaryDate;
    }

    /**
     * Method requests XML Document with exchange rates for necessary currency and necessary dates range
     * @param currency - currency transforms in CBR currency code in class CBRCurrencyCodesProvider.java
     * @param start - start date
     * @param end - end date
     * @return - XML Document
     */
    private Document requestExchangeRates(String currency, Date start, Date end) {
        CentralBankDataProvider centralBankDataProvider = new CentralBankDataProvider();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(centralBankDataProvider.getExchangeRatesRange(CBRCurrencyCodesProvider.getCurrencyCBRCode(currency), start, end));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Method parse XML Document from Central Bank with list of exchange rates.
     * Example of the XML Document: https://cbr.ru/scripts/XML_dynamic.asp?date_req1=01/01/2021&date_req2=12/01/2021&VAL_NM_RQ=R01235
     *
     * @param doc - XML Document for parsing
     * @return - map with key Date and value exchangeRate from this document
     */
    private Map<Date, ExchangeRate> parseDocument(Document doc) {

        Map<Date, ExchangeRate> result = new HashMap<>();

        //get root element
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();

        //get list of objects Record
        NodeList nodelist = root.getElementsByTagName("Record");

        //map to the ExchangeRate dto
        ExchangeRatesMapper exchangeRatesMapper = new ExchangeRatesMapper();
        for (int i = 0; i < nodelist.getLength(); i++) {
            ExchangeRate exchangeRate = exchangeRatesMapper.mapExchangeRate((Element) nodelist.item(i));
            result.put(exchangeRate.getDate(), exchangeRate);
        }
        return result;
    }
}
