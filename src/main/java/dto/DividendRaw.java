package dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DividendRaw {

    private final UUID id;
    private final String currency;
    private final String ticker;
    private final Date date;
    private final Date exDividendDate;
    private final Date paymentDate;
    private final int quantity;
    private final double tax;
    private final double payment;
    private final double divPerShare;
    private final double dividendGross;
    private final double dividendNet;
    private final ArrayList<String> code;

    public DividendRaw(String currency,
                       String ticker,
                       Date date,
                       Date exDividendDate,
                       Date paymentDate,
                       int quantity,
                       double tax,
                       double payment,
                       double divPerShare,
                       double dividendGross,
                       double dividendNet,
                       ArrayList<String> code) {
        id = UUID.randomUUID();
        this.currency = currency;
        this.ticker = ticker;
        this.date = date;
        this.exDividendDate = exDividendDate;
        this.paymentDate = paymentDate;
        this.quantity = quantity;
        this.tax = tax;
        this.payment = payment;
        this.divPerShare = divPerShare;
        this. dividendGross = dividendGross;
        this.dividendNet = dividendNet;
        this.code = code;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTicker() {
        return ticker;
    }

    public Date getDate() {
        return date;
    }

    public Date getExDividendDate() {
        return exDividendDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTax() {
        return tax;
    }

    public double getPayment() {
        return payment;
    }

    public double getDivPerShare() {
        return divPerShare;
    }

    public double getDividendGross() {
        return dividendGross;
    }

    public double getDividendNet() {
        return dividendNet;
    }

    public ArrayList<String> getCode() {
        return code;
    }

    public UUID getId() {
        return id;
    }
}
