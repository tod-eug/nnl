package controller;

import currency.ExchangeRatesProvider;
import dto.*;
import util.DateUtil;

import java.util.*;
import java.util.stream.Collectors;

public class FeesController {
    /**
     *
     *
     * -> Если дата одинаковая и сумма одинаковая но сначала прибавляется а потом отнимается, то такие строки исключаем
     * -> Если в description есть слово Dividend на русском или английском языках значит оно было учтено в дивидендах
     */

    public ArrayList<FeesCalculated> calculateFees(ArrayList<FeesRaw> list) {

        ArrayList<FeesCalculated> listOfTaxes = new ArrayList<>();

        if (list.size() > 0) {
            //get range of dates from list
            Map<String, Date> datesRange = getDatesRange(list);

            //get list of currencies
            List<String> currencies = getListOfCurrencies(list);

            ExchangeRatesProvider exchangeRatesProvider = new ExchangeRatesProvider();
            Map<String, Map<Date, ExchangeRate>> exchangeRates = exchangeRatesProvider.getExchangeRates(currencies, datesRange);

            //filter by criteria described in the description of the class
            List<FeesRaw> filteredList = filterFeesList(list);

            //calculate
            for (FeesRaw f : filteredList) {
                Double amount = f.getAmount();

                Date exchangeRateDate = exchangeRatesProvider.adjustExchangeRateDate(f.getDate(), exchangeRates.get(f.getCurrency()));
                Double value = exchangeRates.get(f.getCurrency()).get(exchangeRateDate).getValue();
                int nominal = exchangeRates.get(f.getCurrency()).get(exchangeRateDate).getNominal();

                Double amountRub = amount * value / nominal;

                listOfTaxes.add(new FeesCalculated(f.getCurrency(), f.getDate(), f.getDescription(), amount,
                        amountRub, value));
            }
            return listOfTaxes;
        }
        return listOfTaxes;
    }

    private List<FeesRaw> filterFeesList(List<FeesRaw> list) {
        ArrayList<FeesRaw> res = new ArrayList<>();
        for (FeesRaw f : list) {
            List<FeesRaw> filt = list.stream()
                .filter(feesRaw -> !feesRaw.getId().equals(f.getId()))
                .collect(Collectors.toList());
            if (listContainsItemWithThisDate(filt, f.getDate()) && listContainsItemWithThisAmount(filt, f.getAmount() * -1))
                continue;
            else
                res.add(f);
        }

        List<FeesRaw> result = res.stream()
                .filter(feesRaw -> !feesRaw.getDescription().toLowerCase(Locale.ROOT).contains("dividend"))
                .filter(feesRaw -> !feesRaw.getDescription().toLowerCase(Locale.ROOT).contains("дивиденд"))
                .collect(Collectors.toList());
        return result;
    }

    private boolean listContainsItemWithThisDate(List<FeesRaw> list, Date date) {
        for (FeesRaw f : list) {
            if (f.getDate().equals(date))
                return true;
        }
        return false;
    }

    private boolean listContainsItemWithThisAmount(List<FeesRaw> list, Double amount) {
        for (FeesRaw f : list) {
            if (f.getAmount() == amount)
                return true;
        }
        return false;
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
    private Map<String, Date> getDatesRange(List<FeesRaw> list) {
        DateUtil dateUtil = new DateUtil();
        Map<String, Date> result = new HashMap<>();
        Date from = list.get(0).getDate();
        Date to = list.get(0).getDate();
        for (FeesRaw f : list) {
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
    private List<String> getListOfCurrencies(List<FeesRaw> list) {
        List<String> currencies = new ArrayList<>();
        for (FeesRaw f : list) {
            if (currencies.contains(f.getCurrency()))
                continue;
            currencies.add(f.getCurrency());
        }
        return currencies;
    }
}
