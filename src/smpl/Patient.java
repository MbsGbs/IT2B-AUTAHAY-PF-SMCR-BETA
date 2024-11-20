package smpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Patient {
    static Scanner scanner = new Scanner(System.in);
    static config dbConfig = new config();  // Use the config instance for database operations

    public static void patientPanel() {
        int selection;
        do {
            System.out.println("PATIENT DATA PANEL");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Exit");
            System.out.print("Enter Selection: ");
            try {
                selection = scanner.nextInt();
                switch (selection) {
                    case 1:
                        addPatient();
                        break;
                    case 2:
                        viewPatients();
                        break;
                    case 3:
                        updatePatient();
                        break;
                    case 4:
                        deletePatient();
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

    public static void addPatient() {
    System.out.print("How many patients would you like to add? ");
    int numPatients;
    try {
        numPatients = scanner.nextInt();
    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        scanner.nextLine();
        return;
    }

    for (int i = 0; i < numPatients; i++) {
        System.out.println("\nAdding patient " + (i + 1) + " of " + numPatients);

        int p_id;
        while (true) {
            try {
                System.out.print("Enter Patient ID: ");
                p_id = scanner.nextInt();
                scanner.nextLine(); // consume newline

                // Check if the ID already exists
                if (dbConfig.checkIfIdExists(p_id)) {
                    System.out.println("This Patient ID is already in use. Please enter a unique ID.");
                } else {
                    break; // ID is unique, continue with data entry
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for Patient ID.");
                scanner.nextLine(); // clear invalid input
            }
        }

        try {
            System.out.print("Enter First Name: ");
            String p_fname = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String p_lname = scanner.nextLine();
            System.out.print("Enter Birthdate (YYYY-MM-DD): ");
            String p_birthdate = scanner.nextLine();
            System.out.print("Enter Gender: ");
            String p_gender = scanner.nextLine();
            System.out.print("Enter Address: ");
            String p_address = scanner.nextLine();
            System.out.print("Enter Email: ");
            String p_email = scanner.nextLine();
            System.out.print("Enter Contact: ");
            String p_contact = scanner.nextLine();

            String sql = "INSERT INTO patient_data "
                    + "(p_id, p_fname, p_lname, p_birthdate, p_gender,"
                    + " p_address, p_email, p_contact) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            dbConfig.addPatient(sql, p_id, p_fname, p_lname,
                    p_birthdate, p_gender, p_address,
                    p_email, p_contact);
            System.out.println("Patient added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the details again.");
            scanner.nextLine();  // clear invalid input
            i--;  // repeat the current iteration
        }
    }
}


    public static void viewPatients() {
        String sqlQuery = 
       "SELECT * FROM patient_data";
        String[] columnHeaders = 
       {"Patient ID", "First Name", "Last Name", "Birthdate",
           "Gender", "Address", "Email", "Contact"};
        String[] columnNames = 
       {"p_id", "p_fname", "p_lname", "p_birthdate", "p_gender", 
           "p_address", "p_email", "p_contact"};
        dbConfig.viewRecords(sqlQuery, 
                columnHeaders, columnNames);
    }

  public static void updatePatient() {
       try {
        System.out.print("Enter Patient ID to update: ");
        int p_id = scanner.nextInt();
        scanner.nextLine();  // consume newline

        // Check if the patient exists
        if (!dbConfig.checkIfIdExists(p_id)) {
            System.out.println("Patient ID does not exist. Please enter a valid Patient ID.");
            return;
        }

        // Query to retrieve the patient's current information
        String query = "SELECT * FROM patient_data WHERE p_id = ?";
        String[] columnHeaders = {"Patient ID", "First Name", "Last Name", "Birthdate",
                                  "Gender", "Address", "Email", "Contact"};
        String[] columnNames = {"p_id", "p_fname", "p_lname", "p_birthdate", "p_gender", 
                                "p_address", "p_email", "p_contact"};
        
        // Display the current data of the patient
        System.out.println("\nCurrent Patient Data:");
        dbConfig.viewRecordsWithParam(query, columnHeaders, columnNames, p_id);

        int choice;
        do {
            System.out.println("\nSelect the field to update:");
            System.out.println("1. Address");
            System.out.println("2. Contact");
            System.out.println("3. First Name");
            System.out.println("4. Last Name");
            System.out.println("5. Birthdate");
            System.out.println("6. Gender");
            System.out.println("7. Email");
            System.out.println("8. Exit Update");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            String sql;
            switch (choice) {
                case 1:
                    System.out.print("Enter New Address: ");
                    String newAddress = scanner.nextLine();
                    sql = "UPDATE patient_data SET p_address = ? WHERE p_id = ?";
                    dbConfig.updateRecord(sql, newAddress, p_id);
                    System.out.println("Address updated successfully!");
                    break;

                case 2:
                    System.out.print("Enter New Contact: ");
                    String newContact = scanner.nextLine();
                    sql = "UPDATE patient_data SET p_contact = ? WHERE p_id = ?";
                    dbConfig.updateRecord(sql, newContact, p_id);
                    System.out.println("Contact updated successfully!");
                    break;

                case 3:
                    System.out.print("Enter New First Name: ");
                    String newFName = scanner.nextLine();
                    sql = "UPDATE patient_data SET p_fname = ? WHERE p_id = ?";
                    dbConfig.updateRecord(sql, newFName, p_id);
                    System.out.println("First Name updated successfully!");
                    break;

                case 4:
                    System.out.print("Enter New Last Name: ");
                    String newLName = scanner.nextLine();
                    sql = "UPDATE patient_data SET p_lname = ? WHERE p_id = ?";
                    dbConfig.updateRecord(sql, newLName, p_id);
                    System.out.println("Last Name updated successfully!");
                    break;

                case 5:
                    System.out.print("Enter New Birthdate (YYYY-MM-DD): ");
                    String newBirthdate = scanner.nextLine();
                    sql = "UPDATE patient_data SET p_birthdate = ? WHERE p_id = ?";
                    dbConfig.updateRecord(sql, newBirthdate, p_id);
                    System.out.println("Birthdate updated successfully!");
                    break;

                case 6:
                    System.out.print("Enter New Gender: ");
                    String newGender = scanner.nextLine();
                    sql = "UPDATE patient_data SET p_gender = ? WHERE p_id = ?";
                    dbConfig.updateRecord(sql, newGender, p_id);
                    System.out.println("Gender updated successfully!");
                    break;

                case 7:
                    System.out.print("Enter New Email: ");
                    String newEmail = scanner.nextLine();
                    sql = "UPDATE patient_data SET p_email = ? WHERE p_id = ?";
                    dbConfig.updateRecord(sql, newEmail, p_id);
                    System.out.println("Email updated successfully!");
                    break;

                case 8:
                    System.out.println("Exiting update...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 8);

    } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please try again.");
        scanner.nextLine();  // clear invalid input
    }
}




    public static void deletePatient() {
        try {
            System.out.print("Enter Patient ID to delete: ");
            int p_id = scanner.nextInt();
            String sql = "DELETE FROM patient_data WHERE p_id = ?";
            dbConfig.deleteRecord(sql, p_id);
            System.out.println("Patient deleted successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid Patient ID.");
            scanner.nextLine();  // clear invalid input
        }
    }
}
