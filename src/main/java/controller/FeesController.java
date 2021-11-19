package controller;

import currency.ExchangeRatesProvider;
import dto.*;

import java.util.*;
import java.util.stream.Collectors;

public class FeesController {
    /**
     *
     *
     * -> Если дата одинаковая и сумма одинаковая но сначала прибавляется а потом отнимается, то такие строки исключаем
     * -> Если в description есть слово Dividend на русском или английском языках значит оно было учтено в дивидендах
     */

    public ArrayList<FeesCalculated> calculateFees(ArrayList<FeesRaw> list, Map<String, Map<Date, ExchangeRate>> exchangeRates) {

        ArrayList<FeesCalculated> listOfTaxes = new ArrayList<>();

        if (list.size() > 0) {
            ExchangeRatesProvider exchangeRatesProvider = new ExchangeRatesProvider();

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
}
