package dto;

import java.util.Date;
import java.util.UUID;

public class FeesTransactionsCalculated {
    private final UUID id;
    private final String instrument;
    private final String currency;
    private final String ticker;
    private final Date date;
    private final String description;
    private final double quantity;
    private final double tradePrice;
    private final double amount;
    private final double amountRub;
    private final double exchangeRate;

    public FeesTransactionsCalculated(String instrument,
                          String currency,
                          String ticker,
                          Date date,
                          String description,
                          Double quantity,
                          Double tradePrice,
                          Double amount,
                          Double amountRub,
                          Double exchangeRate) {
        id = UUID.randomUUID();
        this.instrument = instrument;
        this.currency = currency;
        this.ticker = ticker;
        this.date = date;
        this.description = description;
        this.quantity = quantity;
        this.tradePrice = tradePrice;
        this.amount = amount;
        this.amountRub = amountRub;
        this.exchangeRate = exchangeRate;
    }

    public UUID getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
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

    public String getDescription() {
        return description;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public double getAmount() {
        return amount;
    }

    public double getAmountRub() {
        return amountRub;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
