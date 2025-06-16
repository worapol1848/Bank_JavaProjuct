import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

abstract class BankWindow {
    protected JFrame frame;
    protected final Color backgroundColor = new Color(200, 255, 200);
    protected final Color buttonColor = new Color(50, 150, 50);

    protected void setupFrame(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(backgroundColor);
        frame.setLocationRelativeTo(null);
    }

    protected JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        return button;
    }

    abstract void display();
}

class BankSystem {
    private List<User> users;
    private UserManager userManager;

    public BankSystem() {
        this.users = new ArrayList<>();
        this.userManager = new UserManager();
        new MainMenuWindow().display();
    }

    private class UserManager {
        public void addUser(User user) {
            users.add(user);
        }

        public User findUser(String cardNumber, String pin) {
            return users.stream()
                    .filter(u -> u.getCardNumber().equals(cardNumber)
                            && u.verifyPin(pin))
                    .findFirst()
                    .orElse(null);
        }

        public String generateCardNumber() {
            Random rand = new Random();
            StringBuilder cardNumber = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                cardNumber.append(rand.nextInt(10));
            }
            return cardNumber.toString();
        }
    }

    private class MainMenuWindow extends BankWindow {
        @Override
        void display() {
            setupFrame("Bank System", 400, 300);
            frame.setLayout(new GridLayout(3, 1, 10, 10));

            JButton createAccountButton = createButton("Create Account", buttonColor);
            JButton loginButton = createButton("Login", buttonColor);
            JButton exitButton = createButton("Exit", buttonColor);

            createAccountButton.addActionListener(e -> {
                frame.dispose();
                new CreateAccountWindow().display();
            });

            loginButton.addActionListener(e -> {
                frame.dispose();
                new LoginWindow().display();
            });

            exitButton.addActionListener(e -> System.exit(0));

            frame.add(createAccountButton);
            frame.add(loginButton);
            frame.add(exitButton);
            frame.setVisible(true);
        }
    }

    private class CreateAccountWindow extends BankWindow {
        @Override
        void display() {
            setupFrame("Create Account", 400, 500);
            frame.setLayout(new GridLayout(11, 2, 5, 5));

            JTextField[] fields = new JTextField[9];
            String[] labels = {
                    "Name", "6-digit PIN", "Birthdate (DD/MM/YYYY)",
                    "Gender", "Address", "District", "Province",
                    "Postal Code", "Initial Deposit"
            };

            for (int i = 0; i < fields.length; i++) {
                fields[i] = new JTextField();
                frame.add(new JLabel(labels[i]));
                frame.add(fields[i]);
            }

            JButton submitButton = createButton("Create Account", buttonColor);
            JButton backButton = createButton("Back", new Color(150, 50, 50));

            submitButton.addActionListener(e -> {
                try {
                    String cardNumber = userManager.generateCardNumber();
                    Account account = new Account(
                            Double.parseDouble(fields[8].getText())
                    );

                    User newUser = new User(
                            fields[0].getText(), fields[1].getText(),
                            fields[2].getText(), fields[3].getText(),
                            fields[4].getText(), fields[5].getText(),
                            fields[6].getText(), fields[7].getText(),
                            cardNumber, account
                    );

                    userManager.addUser(newUser);
                    JOptionPane.showMessageDialog(frame,
                            "Account created successfully!\nCard Number: " + cardNumber);
                    frame.dispose();
                    new MainMenuWindow().display();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Error: " + ex.getMessage());
                }
            });

            backButton.addActionListener(e -> {
                frame.dispose();
                new MainMenuWindow().display();
            });

            frame.add(submitButton);
            frame.add(backButton);
            frame.setVisible(true);
        }
    }

    private class LoginWindow extends BankWindow {
        @Override
        void display() {
            setupFrame("Login", 400, 200);
            frame.setLayout(new GridLayout(3, 2, 5, 5));

            JTextField cardNumberField = new JTextField();
            JPasswordField pinField = new JPasswordField();
            JButton loginButton = createButton("Login", buttonColor);
            JButton backButton = createButton("Back", new Color(150, 50, 50));

            loginButton.addActionListener(e -> {
                String cardNumber = cardNumberField.getText();
                String pin = new String(pinField.getPassword());

                User user = userManager.findUser(cardNumber, pin);
                if (user != null) {
                    frame.dispose();
                    new DashboardWindow(user).display();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid card number or PIN");
                }
            });

            backButton.addActionListener(e -> {
                frame.dispose();
                new MainMenuWindow().display();
            });

            frame.add(new JLabel("Card Number:"));
            frame.add(cardNumberField);
            frame.add(new JLabel("PIN:"));
            frame.add(pinField);
            frame.add(loginButton);
            frame.add(backButton);
            frame.setVisible(true);
        }
    }

    private class DashboardWindow extends BankWindow {
        private final User user;

        public DashboardWindow(User user) {
            this.user = user;
        }

        @Override
        void display() {
            setupFrame("Dashboard", 300, 300);
            frame.setLayout(new GridLayout(5, 1, 5, 5));

            JButton[] buttons = {
                    createButton("View Balance", buttonColor),
                    createButton("Deposit", buttonColor),
                    createButton("Withdraw", buttonColor),
                    createButton("View Profile", buttonColor),
                    createButton("Logout", buttonColor)
            };

            buttons[0].addActionListener(e ->
                    JOptionPane.showMessageDialog(frame,
                            String.format("Balance: %.2f", user.getAccount().getBalance()))
            );

            buttons[1].addActionListener(e -> handleDeposit());
            buttons[2].addActionListener(e -> handleWithdraw());

            buttons[3].addActionListener(e ->
                    JOptionPane.showMessageDialog(frame, user.getUserInfo())
            );

            buttons[4].addActionListener(e -> {
                frame.dispose();
                new MainMenuWindow().display();
            });

            for (JButton button : buttons) {
                frame.add(button);
            }

            frame.setVisible(true);
        }

        private void handleDeposit() {
            String input = JOptionPane.showInputDialog("Enter deposit amount:");
            try {
                if (input != null) {
                    double amount = Double.parseDouble(input);
                    user.getAccount().deposit(amount);
                    JOptionPane.showMessageDialog(frame, "Deposit successful!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "Error: " + ex.getMessage());
            }
        }

        private void handleWithdraw() {
            String input = JOptionPane.showInputDialog("Enter withdrawal amount:");
            try {
                if (input != null) {
                    double amount = Double.parseDouble(input);
                    user.getAccount().withdraw(amount);
                    JOptionPane.showMessageDialog(frame, "Withdrawal successful!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "Error: " + ex.getMessage());
            }
        }
    }
}   