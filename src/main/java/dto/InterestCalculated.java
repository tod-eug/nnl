package dto;

import java.util.Date;
import java.util.UUID;

public class InterestCalculated {
    private final UUID id;
    private final String currency;
    private final String description;
    private final Date date;
    private final double amount;
    private final double amountRub;
    private final double exchangeRate;

    public InterestCalculated (String currency,
                               Date date,
                               String description,
                               Double amount,
                               Double amountRub,
                               Double exchangeRate) {
        id = UUID.randomUUID();
        this.currency = currency;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.amountRub = amountRub;
        this.exchangeRate = exchangeRate;
    }

    public UUID getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getDate() {
        return date;
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

    public String getDescription() {
        return description;
    }
}
