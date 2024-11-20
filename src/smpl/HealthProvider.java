package smpl;

import java.util.InputMismatchException;
import java.util.Scanner;


public class HealthProvider {
    static Scanner scanner = new Scanner(System.in);
    static config dbConfig = new config();  // Use the config instance for database operations

    public static void healthProviderPanel() {
        int selection;
        do {
            System.out.println("HEALTH PROVIDER PANEL");
            System.out.println("1. Add Provider");
            System.out.println("2. View Providers");
            System.out.println("3. Update Provider");
            System.out.println("4. Delete Provider");
            System.out.println("5. Exit");
            System.out.print("Enter Selection: ");
            try {
                selection = scanner.nextInt();
                switch (selection) {
                    case 1:
                        addProvider();
                        break;
                    case 2:
                        viewProviders();
                        break;
                    case 3:
                        updateProvider();
                        break;
                    case 4:
                        deleteProvider();
                        break;
                    case 5:
                        System.out.println("Returning to main menu...");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  // clear invalid input
                selection = 0;  // reset selection to keep loop running
            }
        } while (selection != 5);
    }

  public static void addProvider() {
    System.out.print("How many providers would you like to add? ");
    int numProviders;
    try {
        numProviders = scanner.nextInt();
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        scanner.nextLine(); // Clear invalid input
        return;
    }

    for (int i = 0; i < numProviders; i++) {
        System.out.println("\nAdding provider " + (i + 1) + " of " + numProviders);

        try {
            int dr_id;
            config dbConfig = new config(); // Create an instance of the config class

            // Loop until a unique dr_id is provided
            while (true) {
                System.out.print("Enter Provider ID: ");
                dr_id = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (!dbConfig.checkIfIdExists(dr_id)) {
                    break; // Exit loop if ID is unique
                } else {
                    System.out.println("Error: Provider ID already exists. Please enter a unique ID.");
                }
            }

            // Gather other details
            System.out.print("Enter First Name: ");
            String dr_fname = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String dr_lname = scanner.nextLine();
            System.out.print("Enter Contact Number: ");
            String dr_contact = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Address: ");
            String address = scanner.nextLine();
            System.out.print("Enter Available Days: ");
            String available_days = scanner.nextLine();
            System.out.print("Enter Available Hours: ");
            String available_hours = scanner.nextLine();

            // Insert provider into the database
            String sql = "INSERT INTO health_provider "
                    + "(dr_id, dr_fname, dr_lname, dr_contact, email, address, "
                    + "available_days, available_hours) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            dbConfig.addPatient(sql, dr_id, dr_fname, dr_lname, dr_contact, email, address, available_days, available_hours);

            System.out.println("Provider added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear invalid input
            i--; // Retry this iteration
        }
    }
}



    public static void viewProviders() {
        String sqlQuery = "SELECT * FROM health_provider";
        String[] columnHeaders = 
            {"Provider ID", "First Name", "Last Name", "Contact", "Email", "Address",
             "Available Days", "Available Hours"};
        String[] columnNames = 
            {"dr_id", "dr_fname", "dr_lname", "dr_contact", "email", "address",
             "available_days", "available_hours"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    public static void updateProvider() {
      System.out.println("\n--- Available Medical Records ---");
    viewProviders();
    try {
        System.out.print("Enter Provider ID to update: ");
        int dr_id = scanner.nextInt();
        scanner.nextLine();  // consume newline

        int choice;
        do {
            System.out.println("\nSelect the field to update:");
            System.out.println("1. Available Days");
            System.out.println("2. Available Hours");
            System.out.println("3. Contact Number");
            System.out.println("4. Email");
            System.out.println("5. Address");
            System.out.println("6. Exit Update");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            String sql;
            switch (choice) {
                case 1:
                    System.out.print("Enter New Available Days: ");
                    String newDays = scanner.nextLine();
                    sql = "UPDATE health_provider SET available_days = ? WHERE dr_id = ?";
                    dbConfig.updateRecord(sql, newDays, dr_id);
                    System.out.println("Available Days updated successfully!");
                    break;

                case 2:
                    System.out.print("Enter New Available Hours: ");
                    String newHours = scanner.nextLine();
                    sql = "UPDATE health_provider SET available_hours = ? WHERE dr_id = ?";
                    dbConfig.updateRecord(sql, newHours, dr_id);
                    System.out.println("Available Hours updated successfully!");
                    break;

                case 3:
                    System.out.print("Enter New Contact Number: ");
                    String newContact = scanner.nextLine();
                    sql = "UPDATE health_provider SET dr_contact = ? WHERE dr_id = ?";
                    dbConfig.updateRecord(sql, newContact, dr_id);
                    System.out.println("Contact Number updated successfully!");
                    break;

                case 4:
                    System.out.print("Enter New Email: ");
                    String newEmail = scanner.nextLine();
                    sql = "UPDATE health_provider SET email = ? WHERE dr_id = ?";
                    dbConfig.updateRecord(sql, newEmail, dr_id);
                    System.out.println("Email updated successfully!");
                    break;

                case 5:
                    System.out.print("Enter New Address: ");
                    String newAddress = scanner.nextLine();
                    sql = "UPDATE health_provider SET address = ? WHERE dr_id = ?";
                    dbConfig.updateRecord(sql, newAddress, dr_id);
                    System.out.println("Address updated successfully!");
                    break;

                case 6:
                    System.out.println("Exiting update...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 6);

    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please try again.");
        scanner.nextLine();  // clear invalid input
    }
}
    public static void deleteProvider() {
        try {
            System.out.print("Enter Provider ID to delete: ");
            int dr_id = scanner.nextInt();
            String sql = "DELETE FROM health_provider WHERE dr_id = ?";
            dbConfig.deleteRecord(sql, dr_id);
            System.out.println("Provider deleted successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid Provider ID.");
            scanner.nextLine();  // clear invalid input
        }
    }
}
