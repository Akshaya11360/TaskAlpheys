package Task;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {

    private List<Transaction> transactions;
    private static final String FILE_NAME = "data.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String[] INCOME_CATEGORIES = {"Salary", "Business"};
    private static final String[] EXPENSE_CATEGORIES = {"Food", "Rent", "Travel"};

    public ExpenseTracker() {
        transactions = new ArrayList<>();
        loadTransactionsFromFile();
    }

    public void addTransaction(Scanner scanner) {
        System.out.println("\nEnter transaction type (1 for Income, 2 for Expense): ");
        int typeChoice;
        try {
            typeChoice = scanner.nextInt();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            scanner.nextLine();
            return;
        }

        String type;
        String[] categories;
        if (typeChoice == 1) {
            type = "Income";
            categories = INCOME_CATEGORIES;
        } else if (typeChoice == 2) {
            type = "Expense";
            categories = EXPENSE_CATEGORIES;
        } else {
            System.out.println("Invalid choice. Returning to menu.");
            return;
        }

        System.out.println("Choose category:");
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }

        int categoryChoice;
        try {
            categoryChoice = scanner.nextInt();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            return;
        }

        if (categoryChoice < 1 || categoryChoice > categories.length) {
            System.out.println("Invalid category. Returning to menu.");
            return;
        }

        String category = categories[categoryChoice - 1];

        System.out.print("Enter amount: ");
        double amount;
        try {
            amount = scanner.nextDouble();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount. Please enter a number.");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Invalid date format. Using current date.");
            date = LocalDate.now();
        }

        Transaction transaction = new Transaction(date, type, category, amount, description);
        transactions.add(transaction);
        saveTransactionsToFile();
        System.out.println("Transaction added successfully!");
    }

    public void showMonthlySummary() {
        Scanner scanner = new Scanner(System.in);
        int month, year;

        try {
            System.out.print("\nEnter month (1-12): ");
            month = scanner.nextInt();
            System.out.print("Enter year (e.g., 2025): ");
            year = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric month and year.");
            scanner.nextLine();
            return;
        }

        double totalIncome = 0.0;
        double totalExpense = 0.0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.getDate().getMonthValue() == month && t.getDate().getYear() == year) {
                double amount = t.getAmount();
                String category = t.getCategory();
                if (t.getType().equals("Income")) {
                    totalIncome += amount;
                } else {
                    totalExpense += amount;
                }
                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
            }
        }

        System.out.println("\nMonthly Summary for " + month + "/" + year);
        System.out.println("Total Income: $" + String.format("%.2f", totalIncome));
        System.out.println("Total Expense: $" + String.format("%.2f", totalExpense));
        System.out.println("Net Balance: $" + String.format("%.2f", totalIncome - totalExpense));
        System.out.println("\nCategory Breakdown:");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.println(entry.getKey() + ": $" + String.format("%.2f", entry.getValue()));
        }
    }

    private void loadTransactionsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                    String type = parts[1];
                    String category = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String description = parts[4];
                    transactions.add(new Transaction(date, type, category, amount, description));
                }
            }
            System.out.println("Loaded transactions from " + FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void saveTransactionsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction t : transactions) {
                writer.write(t.toString());
                writer.newLine();
            }
            System.out.println("Saved transactions to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Monthly Summary");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    addTransaction(scanner);
                    break;
                case 2:
                    showMonthlySummary();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.run();
    }
}
