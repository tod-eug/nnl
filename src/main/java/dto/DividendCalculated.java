package dto;

import java.util.Date;

public class DividendCalculated {

    private final String ticker;
    private final Date paymentDate;
    private final Double dividendRub;
    private final Double expectedDividendRub;
    private final Double payedTaxRub;
    private final Double result;

    public DividendCalculated(String ticker,
                              Date paymentDate,
                              Double dividendRub,
                              Double expectedDividendRub,
                              Double payedTaxRub,
                              Double result) {
        this.ticker = ticker;
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
}
