package dto;

import java.util.Date;

public class ExchangeRate {

    private final Date date;
    private final int nominal;
    private final double value;

    public ExchangeRate(Date date,
                        int nominal,
                        double value) {
        this.date = date;
        this.nominal = nominal;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public int getNominal() {
        return nominal;
    }

    public double getValue() {
        return value;
    }
}
