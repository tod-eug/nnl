package controller;

import currency.ExchangeRatesProvider;
import dto.*;
import util.DateUtil;

import java.util.*;

public class FeesTransactionsController {

    public ArrayList<FeesTransactionsCalculated> calculateFeesTransactions(ArrayList<FeesTransactionsRaw> list) {

        ArrayList<FeesTransactionsCalculated> listOfTaxes = new ArrayList<>();

        if (list.size() > 0) {
            //get range of dates from list
            Map<String, Date> datesRange = getDatesRange(list);

            //get list of currencies
            List<String> currencies = getListOfCurrencies(list);

            ExchangeRatesProvider exchangeRatesProvider = new ExchangeRatesProvider();
            Map<String, Map<Date, ExchangeRate>> exchangeRates = exchangeRatesProvider.getExchangeRates(currencies, datesRange);

            //calculate
            for (FeesTransactionsRaw f : list) {
                DateUtil dateUtil = new DateUtil();
                Date justDate = dateUtil.removeTimeFromDate(f.getDate());
                Double amount = f.getAmount();

                Date exchangeRateDate = exchangeRatesProvider.adjustExchangeRateDate(justDate, exchangeRates.get(f.getCurrency()));
                Double value = exchangeRates.get(f.getCurrency()).get(exchangeRateDate).getValue();
                int nominal = exchangeRates.get(f.getCurrency()).get(exchangeRateDate).getNominal();

                Double amountRub = amount * value / nominal;

                listOfTaxes.add(new FeesTransactionsCalculated(f.getInstrument(),f.getCurrency(), f.getTicker(),
                        f.getDate(), f.getDescription(), f.getQuantity(), f.getTradePrice(), amount,
                        amountRub, value));
            }
            return listOfTaxes;
        }
        return listOfTaxes;
    }

    /**
     * Method defines range of date from the list of transactions. By default, "from" date decreased on 30 days
     * because in the new year holiday could be 8 -10 empty dates.
     *
     * @param list - list of transactions
     * Return map with two keys:
     * - "from" with the earliest date in the list
     * - "to" with the latest date in the list
     * @return - map with range of dates
     */
    private Map<String, Date> getDatesRange(List<FeesTransactionsRaw> list) {
        DateUtil dateUtil = new DateUtil();
        Map<String, Date> result = new HashMap<>();
        Date from = list.get(0).getDate();
        Date to = list.get(0).getDate();
        for (FeesTransactionsRaw f : list) {
            if (f.getDate().before(from))
                from = f.getDate();
            if (f.getDate().after(to))
                to = f.getDate();
        }
        from = dateUtil.increaseDate(from, -30);
        result.put("from", from);
        result.put("to", to);
        return result;
    }

    /**
     * Method defines list of currencies from the list of transactions
     *
     * @param list - list of transactions
     * @return list of currencies
     */
    private List<String> getListOfCurrencies(List<FeesTransactionsRaw> list) {
        List<String> currencies = new ArrayList<>();
        for (FeesTransactionsRaw f : list) {
            if (currencies.contains(f.getCurrency()))
                continue;
            currencies.add(f.getCurrency());
        }
        return currencies;
    }
}
