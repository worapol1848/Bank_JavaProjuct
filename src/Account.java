interface BankTransaction {
    double getBalance();
    void deposit(double amount);
    void withdraw(double amount);
    boolean hasSufficientFunds(double amount);
}

class Account implements BankTransaction {
    private double balance;
    private final String accountNumber;
    private static int accountCounter = 1000;

    public Account(double balance) {
        this.balance = balance;
        this.accountNumber = generateAccountNumber();
    }

    private String generateAccountNumber() {
        return "ACC" + (++accountCounter);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (hasSufficientFunds(amount)) {
            balance -= amount;
        } else {
            throw new IllegalStateException("Insufficient funds");
        }
    }

    @Override
    public boolean hasSufficientFunds(double amount) {
        return balance >= amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}


