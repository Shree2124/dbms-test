import java.sql.*;
import java.util.Scanner;

public class postgreSQLConnect {
    static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASSWORD = "shree";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            createCustomerTable(connection);
            displayMenu(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createCustomerTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS customer (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "contact_number VARCHAR(15) NOT NULL," +
                "address VARCHAR(255) NOT NULL," +
                "age INT NOT NULL CHECK (age > 0)," +
                "bill DECIMAL(10, 2) NOT NULL CHECK (bill >= 0)" +
                ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
            System.out.println("Customer table created successfully.");
        }
    }

    static void displayMenu(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCustomer Management System");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addCustomer(connection, scanner);
                    break;
                case 2:
                    try {
                        showCustomers(connection);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    updateCustomer(connection, scanner);
                    break;
                case 4:
                    deleteCustomer(connection, scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    static void addCustomer(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("\nAdd Customer");
        System.out.print("Enter customer name: ");
        String name = scanner.next();
        System.out.print("Enter customer email: ");
        String email = scanner.next();
        System.out.print("Enter contact number (10 digits): ");
        String contactNumber = scanner.next();
        System.out.print("Enter address: ");
        String address = scanner.next();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        System.out.print("Enter bill amount: ");
        double bill = scanner.nextDouble();

        String insertSQL = "INSERT INTO customer (name, email, contact_number, address, age, bill) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, contactNumber);
            preparedStatement.setString(4, address);
            preparedStatement.setInt(5, age);
            preparedStatement.setDouble(6, bill);
            preparedStatement.executeUpdate();
            System.out.println("Customer added successfully.");
        }
    }

    static void showCustomers(Connection connection) throws SQLException {
        System.out.println("\nCustomer Data:");
        System.out.println("ID\t\tName\t\tEmail\t\t\tContact\t\tAddress\t\tAge\t\tBill");
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String contactNumber = resultSet.getString("contact_number");
                String address = resultSet.getString("address");
                int age = resultSet.getInt("age");
                double bill = resultSet.getDouble("bill");
                System.out.println(id + "\t\t" + name + "\t\t" + email + "       " + contactNumber + "       " + address
                        + "\t         " + age + "\t       " + bill);
            }
        }
    }

    static void updateCustomer(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("\nUpdate Customer");
        System.out.print("Enter customer ID to update: ");
        int id = scanner.nextInt(); 
        System.out.println("1.For name\n2.For email.\n3.For contact number.\n4.For age.\n5.For bill.");
        int ch1 = scanner.nextInt();
        switch (ch1){
            case 1 :
                System.out.print("Enter updated name: ");
                String name = System.console().readLine();
                String updateSQLName = "UPDATE customer SET name = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQLName)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Customer updated successfully.");
                } else {
                    System.out.println("No customer found with the provided ID.");
                }
            }
                break;
            case 2:
                System.out.print("Enter updated email: ");
                String email = System.console().readLine();
                String updateSQLEmail = "UPDATE customer SET email = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQLEmail)) {
                    preparedStatement.setString(1, email);
                    preparedStatement.setInt(2, id);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Customer updated successfully.");
                    } else {
                        System.out.println("No customer found with the provided ID.");
                    }
                }
                break;
            case 3:
                System.out.print("Enter updated contact number: ");
                int contact_number = scanner.nextInt();
                String updateSQLContact_Number = "UPDATE customer SET contact_number = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQLContact_Number)) {
                    preparedStatement.setInt(1, contact_number);
                    preparedStatement.setInt(2, id);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Customer updated successfully.");
                    } else {
                        System.out.println("No customer found with the provided ID.");
                    }
                }
                break;
            case 4:
                System.out.println("Enter updated age: ");
                int age = scanner.nextInt();
                String updateSQLAge = "UPDATE customer SET age = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQLAge)) {
                    preparedStatement.setInt(1, age);
                    preparedStatement.setInt(2, id);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Customer updated successfully.");
                    } else {
                        System.out.println("No customer found with the provided ID.");
                    }
                }
                break;
            case 5:
                System.out.println("Enter updated bill: ");
                float bill = scanner.nextFloat();
                String updateSQLBill = "UPDATE customer SET bill = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQLBill)) {
                    preparedStatement.setFloat(1, bill);
                    preparedStatement.setInt(2, id);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Customer updated successfully.");
                    } else {
                        System.out.println("No customer found with the provided ID.");
                    }
                }
                break;
            default:
                System.out.println("Invalid choice!");
                break;
            }
    }

    static void deleteCustomer(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("\nDelete Customer");
        System.out.print("Enter customer ID to delete: ");
        int id = scanner.nextInt();

        String deleteSQL = "DELETE FROM customer WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("No customer found with the provided ID.");
            }
        }
    }
}