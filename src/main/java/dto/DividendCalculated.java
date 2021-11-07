package dto;

import java.util.Date;

public class DividendCalculated {

    private final String ticker;
    private final Date paymentDate;
    private final Double exchangeRate;
    private final Double dividendGross;
    private final Double dividendNet;
    private final Double tax;
    private final Double dividendRub;
    private final Double expectedDividendRub;
    private final Double payedTaxRub;
    private final Double result;

    public DividendCalculated(String ticker,
                              Date paymentDate,
                              Double exchangeRate,
                              Double dividendGross,
                              Double dividendNet,
                              Double tax,
                              Double dividendRub,
                              Double expectedDividendRub,
                              Double payedTaxRub,
                              Double result) {
        this.ticker = ticker;
        this.exchangeRate = exchangeRate;
        this.dividendGross = dividendGross;
        this.dividendNet = dividendNet;
        this.tax = tax;
        this.paymentDate = paymentDate;
        this.dividendRub = dividendRub;
        this.expectedDividendRub = expectedDividendRub;
        this.payedTaxRub = payedTaxRub;
        this.result = result;
    }

    public String getTicker() {
        return ticker;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public Double getDividendRub() {
        return dividendRub;
    }

    public Double getExpectedDividendRub() {
        return expectedDividendRub;
    }

    public Double getPayedTaxRub() {
        return payedTaxRub;
    }

    public Double getResult() {
        return result;
    }

    public Double getDividendGross() {
        return dividendGross;
    }

    public Double getDividendNet() {
        return dividendNet;
    }

    public Double getTax() {
        return tax;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }
}
