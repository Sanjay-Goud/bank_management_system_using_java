import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a bank transaction with amount, type, and timestamp
 */
class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private final double amount;
    private final String type;
    private final Date timestamp;
    
    public Transaction(double amount, String type) {
        this.amount = amount;
        this.type = type;
        this.timestamp = new Date();
    }
    
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: $%.2f", dateFormat.format(timestamp), type, amount);
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getType() {
        return type;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
}

/**
 * Represents a bank customer with account details and transaction history
 */
class BankCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int accountNumber;
    private double balance;
    private String password;
    private List<Transaction> transactionHistory;
    
    // Constructor
    public BankCustomer(String name, int accountNumber, double initialBalance, String password) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.password = password;
        this.transactionHistory = new ArrayList<>();
        // Record the initial deposit as a transaction
        if (initialBalance > 0) {
            transactionHistory.add(new Transaction(initialBalance, "Initial Deposit"));
        }
    }
    
    // Getters and setters with proper encapsulation
    public String getName() {
        return name;
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
    
    // Deposit money with validation
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Deposit amount must be positive.");
            return false;
        }
        
        balance += amount;
        Transaction transaction = new Transaction(amount, "Deposit");
        transactionHistory.add(transaction);
        System.out.printf("Deposit successful. Current balance: $%.2f%n", balance);
        return true;
    }
    
    // Withdraw money with validation
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
            return false;
        }
        
        if (balance >= amount) {
            balance -= amount;
            Transaction transaction = new Transaction(amount, "Withdrawal");
            transactionHistory.add(transaction);
            System.out.printf("Withdrawal successful. Current balance: $%.2f%n", balance);
            return true;
        } else {
            System.out.println("Insufficient funds. Withdrawal failed.");
            return false;
        }
    }
    
    // Calculate and add interest with validation
    public boolean addInterest(double rate) {
        if (rate <= 0 || rate > 20) {
            System.out.println("Error: Interest rate must be between 0 and 20 percent.");
            return false;
        }
        
        double interest = balance * rate / 100;
        balance += interest;
        Transaction transaction = new Transaction(interest, "Interest Added");
        transactionHistory.add(transaction);
        System.out.printf("Interest added (%.2f%%). Current balance: $%.2f%n", rate, balance);
        return true;
    }
    
    // Display transaction history
    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction history available.");
            return;
        }
        
        System.out.println("\n===== Transaction History =====");
        for (Transaction transaction : transactionHistory) {
            System.out.println(transaction);
        }
        System.out.println("==============================");
    }
}

/**
 * Represents a bank employee with authentication
 */
class BankEmployee implements Serializable {
    private static final long serialVersionUID = 1L;
    private int employeeId;
    private String name;
    private String position;
    private String password;
    
    // Constructor
    public BankEmployee(int employeeId, String name, String position, String password) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.password = password;
    }
    
    // Getters with proper encapsulation
    public int getEmployeeId() {
        return employeeId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPosition() {
        return position;
    }
    
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}

/**
 * Admin class with extended capabilities to manage customers and employees
 */
class BankAdmin {
    private final Map<Integer, BankCustomer> customers;
    private final List<BankEmployee> employees;
    private String adminPassword;
    
    // Constructor
    public BankAdmin(String adminPassword) {
        this.customers = new HashMap<>();
        this.employees = new ArrayList<>();
        this.adminPassword = adminPassword;
    }
    
    // Authenticate admin
    public boolean authenticate(String password) {
        return this.adminPassword.equals(password);
    }
    
    // Customer management methods
    public void addCustomer(BankCustomer customer) {
        customers.put(customer.getAccountNumber(), customer);
    }
    
    public BankCustomer getCustomer(int accountNumber) {
        return customers.get(accountNumber);
    }
    
    public boolean removeCustomer(int accountNumber) {
        return customers.remove(accountNumber) != null;
    }
    
    public Map<Integer, BankCustomer> getAllCustomers() {
        return new HashMap<>(customers);
    }
    
    // Employee management methods
    public void addEmployee(BankEmployee employee) {
        employees.add(employee);
    }
    
    public BankEmployee findEmployeeById(int employeeId) {
        for (BankEmployee emp : employees) {
            if (emp.getEmployeeId() == employeeId) {
                return emp;
            }
        }
        return null;
    }
    
    public boolean removeEmployee(int employeeId) {
        return employees.removeIf(emp -> emp.getEmployeeId() == employeeId);
    }
    
    // View customer profile
    public void viewCustomerProfile(int accountNumber) {
        BankCustomer customer = customers.get(accountNumber);
        if (customer != null) {
            System.out.println("\n===== Customer Profile =====");
            System.out.printf("Name: %s%n", customer.getName());
            System.out.printf("Account Number: %d%n", customer.getAccountNumber());
            System.out.printf("Balance: $%.2f%n", customer.getBalance());
            System.out.println("============================");
        } else {
            System.out.println("Customer not found with account number: " + accountNumber);
        }
    }
    
    // View employee profile
    public void viewEmployeeProfile(int employeeId) {
        BankEmployee employee = findEmployeeById(employeeId);
        if (employee != null) {
            System.out.println("\n===== Employee Profile =====");
            System.out.printf("Name: %s%n", employee.getName());
            System.out.printf("Employee ID: %d%n", employee.getEmployeeId());
            System.out.printf("Position: %s%n", employee.getPosition());
            System.out.println("============================");
        } else {
            System.out.println("Employee not found with ID: " + employeeId);
        }
    }
    
    // List all customers
    public void listAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }
        
        System.out.println("\n===== All Customers =====");
        for (BankCustomer customer : customers.values()) {
            System.out.printf("Account #%d - %s: $%.2f%n", 
                customer.getAccountNumber(), customer.getName(), customer.getBalance());
        }
        System.out.println("========================");
    }
    
    // List all employees
    public void listAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees registered.");
            return;
        }
        
        System.out.println("\n===== All Employees =====");
        for (BankEmployee employee : employees) {
            System.out.printf("ID #%d - %s: %s%n", 
                employee.getEmployeeId(), employee.getName(), employee.getPosition());
        }
        System.out.println("========================");
    }
    
    // Save data to file
    public void saveData(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(customers);
            oos.writeObject(employees);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    // Load data from file
    @SuppressWarnings("unchecked")
    public void loadData(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No saved data found. Starting with empty database.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<Integer, BankCustomer> loadedCustomers = (Map<Integer, BankCustomer>) ois.readObject();
            List<BankEmployee> loadedEmployees = (List<BankEmployee>) ois.readObject();
            
            customers.clear();
            customers.putAll(loadedCustomers);
            
            employees.clear();
            employees.addAll(loadedEmployees);
            
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}

/**
 * Main system class with UI handling separate from business logic
 */
public class BankManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "bank_data.ser";
    private static BankAdmin admin;
    
    public static void main(String[] args) {
        // Initialize admin with a default password
        admin = new BankAdmin("admin123");
        
        // Load saved data
        admin.loadData(DATA_FILE);
        
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        customerLogin();
                        break;
                    case 2:
                        registerNewCustomer();
                        break;
                    case 3:
                        employeeLogin();
                        break;
                    case 4:
                        adminLogin();
                        break;
                    case 5:
                        // Save data before exiting
                        admin.saveData(DATA_FILE);
                        running = false;
                        System.out.println("Thank you for using the Bank Management System!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the scanner buffer
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    // Display main menu
    private static void displayMainMenu() {
        System.out.println("\n==== Bank Management System ====");
        System.out.println("1. Customer Login");
        System.out.println("2. Register New Customer");
        System.out.println("3. Employee Login");
        System.out.println("4. Admin Login");
        System.out.println("5. Exit");
        System.out.println("===============================");
    }
    
    // Customer login and operations
    private static void customerLogin() {
        System.out.println("\n==== Customer Login ====");
        int accountNumber = getIntInput("Enter account number: ");
        String password = getStringInput("Enter password: ");
        
        BankCustomer customer = admin.getCustomer(accountNumber);
        if (customer != null && customer.verifyPassword(password)) {
            System.out.println("Login successful. Welcome, " + customer.getName() + "!");
            customerMenu(customer);
        } else {
            System.out.println("Invalid account number or password.");
        }
    }
    
    // Customer operations menu
    private static void customerMenu(BankCustomer customer) {
        boolean customerLoggedIn = true;
        
        while (customerLoggedIn) {
            System.out.println("\n==== Customer Menu ====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transaction History");
            System.out.println("5. Logout");
            System.out.println("=====================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    System.out.printf("Current balance: $%.2f%n", customer.getBalance());
                    break;
                case 2:
                    double depositAmount = getDoubleInput("Enter deposit amount: $");
                    customer.deposit(depositAmount);
                    break;
                case 3:
                    double withdrawalAmount = getDoubleInput("Enter withdrawal amount: $");
                    customer.withdraw(withdrawalAmount);
                    break;
                case 4:
                    customer.showTransactionHistory();
                    break;
                case 5:
                    customerLoggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // Register new customer
    private static void registerNewCustomer() {
        System.out.println("\n==== Customer Registration ====");
        String name = getStringInput("Enter your name: ");
        
        // Generate a unique account number
        int accountNumber;
        Random random = new Random();
        do {
            accountNumber = 10000 + random.nextInt(90000); // 5-digit account number
        } while (admin.getCustomer(accountNumber) != null);
        
        double initialBalance = getDoubleInput("Enter initial deposit amount: $");
        if (initialBalance < 0) {
            System.out.println("Initial deposit cannot be negative. Registration cancelled.");
            return;
        }
        
        String password = getStringInput("Create a password: ");
        String confirmPassword = getStringInput("Confirm password: ");
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Registration cancelled.");
            return;
        }
        
        BankCustomer newCustomer = new BankCustomer(name, accountNumber, initialBalance, password);
        admin.addCustomer(newCustomer);
        
        System.out.println("\nRegistration successful!");
        System.out.println("Your account number is: " + accountNumber);
        System.out.println("Please remember this number for future logins.");
    }
    
    // Employee login and operations
    private static void employeeLogin() {
        System.out.println("\n==== Employee Login ====");
        int employeeId = getIntInput("Enter employee ID: ");
        String password = getStringInput("Enter password: ");
        
        BankEmployee employee = admin.findEmployeeById(employeeId);
        if (employee != null && employee.authenticate(password)) {
            System.out.println("Login successful. Welcome, " + employee.getName() + "!");
            employeeMenu(employee);
        } else {
            System.out.println("Invalid employee ID or password.");
        }
    }
    
    // Employee operations menu
    private static void employeeMenu(BankEmployee employee) {
        boolean employeeLoggedIn = true;
        
        while (employeeLoggedIn) {
            System.out.println("\n==== Employee Menu ====");
            System.out.println("1. View Customer Profile");
            System.out.println("2. Process Interest");
            System.out.println("3. View All Customers");
            System.out.println("4. Logout");
            System.out.println("=====================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    int accountNumber = getIntInput("Enter customer account number: ");
                    admin.viewCustomerProfile(accountNumber);
                    break;
                case 2:
                    processInterest();
                    break;
                case 3:
                    admin.listAllCustomers();
                    break;
                case 4:
                    employeeLoggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // Process interest for a customer
    private static void processInterest() {
        int accountNumber = getIntInput("Enter customer account number: ");
        BankCustomer customer = admin.getCustomer(accountNumber);
        
        if (customer != null) {
            double interestRate = getDoubleInput("Enter interest rate (%): ");
            customer.addInterest(interestRate);
        } else {
            System.out.println("Customer not found with account number: " + accountNumber);
        }
    }
    
    // Admin login and operations
    private static void adminLogin() {
        System.out.println("\n==== Admin Login ====");
        String password = getStringInput("Enter admin password: ");
        
        if (admin.authenticate(password)) {
            System.out.println("Admin login successful!");
            adminMenu();
        } else {
            System.out.println("Invalid admin password.");
        }
    }
    
    // Admin operations menu
    private static void adminMenu() {
        boolean adminLoggedIn = true;
        
        while (adminLoggedIn) {
            System.out.println("\n==== Admin Menu ====");
            System.out.println("1. View All Customers");
            System.out.println("2. View All Employees");
            System.out.println("3. Add New Employee");
            System.out.println("4. Remove Customer");
            System.out.println("5. Remove Employee");
            System.out.println("6. Save Data");
            System.out.println("7. Load Data");
            System.out.println("8. Logout");
            System.out.println("===================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    admin.listAllCustomers();
                    break;
                case 2:
                    admin.listAllEmployees();
                    break;
                case 3:
                    addNewEmployee();
                    break;
                case 4:
                    removeCustomer();
                    break;
                case 5:
                    removeEmployee();
                    break;
                case 6:
                    admin.saveData(DATA_FILE);
                    break;
                case 7:
                    admin.loadData(DATA_FILE);
                    break;
                case 8:
                    adminLoggedIn = false;
                    System.out.println("Admin logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // Add a new employee
    private static void addNewEmployee() {
        System.out.println("\n==== Add New Employee ====");
        String name = getStringInput("Enter employee name: ");
        
        // Generate a unique employee ID
        int employeeId;
        Random random = new Random();
        do {
            employeeId = 1000 + random.nextInt(9000); // 4-digit employee ID
        } while (admin.findEmployeeById(employeeId) != null);
        
        String position = getStringInput("Enter employee position: ");
        String password = getStringInput("Create employee password: ");
        
        BankEmployee newEmployee = new BankEmployee(employeeId, name, position, password);
        admin.addEmployee(newEmployee);
        
        System.out.println("\nEmployee added successfully!");
        System.out.println("Employee ID: " + employeeId);
    }
    
    // Remove a customer
    private static void removeCustomer() {
        int accountNumber = getIntInput("Enter account number of customer to remove: ");
        if (admin.removeCustomer(accountNumber)) {
            System.out.println("Customer removed successfully.");
        } else {
            System.out.println("Customer not found with account number: " + accountNumber);
        }
    }
    
    // Remove an employee
    private static void removeEmployee() {
        int employeeId = getIntInput("Enter ID of employee to remove: ");
        if (admin.removeEmployee(employeeId)) {
            System.out.println("Employee removed successfully.");
        } else {
            System.out.println("Employee not found with ID: " + employeeId);
        }
    }
    
    // Utility method to get integer input with validation
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    // Utility method to get double input with validation
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    // Utility method to get string input
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
