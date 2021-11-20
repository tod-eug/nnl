package dto;

public class TradeResultsForInstrument {
    private final double tax;
    private final double deduction;

    public TradeResultsForInstrument(double tax, double deduction) {
        this.tax = tax;
        this.deduction = deduction;
    }

    public double getTax() {
        return tax;
    }

    public double getDeduction() {
        return deduction;
    }
}
