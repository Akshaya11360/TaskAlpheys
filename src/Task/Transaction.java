package Task;

import java.time.LocalDate;

public class Transaction {
	private LocalDate date;
    private String type; // Income or Expense
    private String category; // Salary, Business, Food, Rent, Travel
    private double amount;
    private String description;

    public Transaction(LocalDate date, String type, String category, double amount, String description) {
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.2f,%s", date, type, category, amount, description);
    }

}
