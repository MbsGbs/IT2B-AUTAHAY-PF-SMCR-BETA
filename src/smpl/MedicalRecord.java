package smpl;

import java.util.Scanner;

public class MedicalRecord {
    static Scanner scanner = new Scanner(System.in);
    static config dbConfig = new config();

    // Display the Medical Record panel and allow actions
    public static void medicalRecordPanel() {
        int action;
        do {
            System.out.println("MEDICAL RECORD MANAGEMENT");
            System.out.println("1. Add Medical Record");
            System.out.println("2. View Medical Records");
            System.out.println("3. Update Medical Record");
            System.out.println("4. Delete Medical Record");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter action: ");
            action = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (action) {
                case 1:
                    addMedicalRecord();
                    break;
                case 2:
                    viewMedicalRecords();
                    break;
                case 3:
                    updateMedicalRecord();
                    break;
                case 4:
                    deleteMedicalRecord();
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (action != 5);
    }
private static void displayPatients() {
    String sqlQuery = "SELECT * FROM patient_data"; // Replace with your table name if different
    String[] columnHeaders = {"PATIENT ID", "Name", "PATIENT BDATE", "Gender"};
    String[] columnNames = {"p_id", "p_lname", "p_birthdate", "p_gender"};
    dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
}
private static void displayHealthProviders() {
    String sqlQuery = "SELECT * FROM health_provider"; // Replace with your table name if different
    String[] columnHeaders = {"Provider ID", "Name", "Avail Days","Avail Hours"};
    String[] columnNames = {"dr_id", "dr_lname", "available_days","available_hours"};
    dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
}

   // Method to validate if the ID exists in the specified table and column
private static boolean isValidID(String tableName, String columnName, int id) {
    String sqlQuery = "SELECT COUNT(*) AS count FROM " + tableName + " WHERE " + columnName + " = ?";
    return dbConfig.recordExists(sqlQuery, id);
}

   // Method to add a new medical record
private static void addMedicalRecord() {
    int p_id;
    int dr_id;

    // Loop until a valid Patient ID is entered
    do {
        System.out.println("Available Patients:");
        displayPatients(); // Display a list of patients from the database
        System.out.println("Enter Patient ID: ");
        p_id = scanner.nextInt();
        if (!isValidID("patient_data", "p_id", p_id)) {
            System.out.println("Invalid Patient ID. Please try again.");
        }
    } while (!isValidID("patient_data", "p_id", p_id));

    // Loop until a valid Health Provider ID is entered
    do {
        System.out.println("Available Health Providers:");
        displayHealthProviders(); // Display a list of health providers from the database
        System.out.println("Enter Doctor ID: ");
        dr_id = scanner.nextInt();
        if (!isValidID("health_provider", "dr_id", dr_id)) {
            System.out.println("Invalid Health Provider ID. Please try again.");
        }
    } while (!isValidID("health_provider", "dr_id", dr_id));
    scanner.nextLine(); // Consume newline

    System.out.println("Enter Visit Date (yyyy-mm-dd): ");
    String visit_date = scanner.nextLine();
    System.out.println("Enter Diagnosis: ");
    String diagnosis = scanner.nextLine();
    System.out.println("Enter Treatment: ");
    String treatment = scanner.nextLine();
    System.out.println("Enter Notes: ");
    String note = scanner.nextLine();

    // Exclude record_id from the SQL INSERT query
    String sql = "INSERT INTO medical_record "
               + "(p_id, dr_id, visit_date, diagnosis, treatment, note) "
               + "VALUES (?, ?, ?, ?, ?, ?)";
    dbConfig.addPatient(sql, p_id, dr_id, visit_date, diagnosis, treatment, note);

    System.out.println("Medical record added successfully!");
}



    // Method to view all medical records
    private static void viewMedicalRecords() {
    // SQL query to join medical_record with patient_data and health_provider
    String sqlQuery = 
        "SELECT mr.record_id, pd.p_lname AS patient_name, hp.dr_lname AS provider_name, " +
        "mr.visit_date, mr.diagnosis, mr.treatment, mr.note " +
        "FROM medical_record mr " +
        "JOIN patient_data pd ON mr.p_id = pd.p_id " +
        "JOIN health_provider hp ON mr.dr_id = hp.dr_id";

    // Define the headers to display in the table
    String[] columnHeaders = 
        {"Record ID", "Patient Name", "Provider Name", "Visit Date", 
         "Diagnosis", "Treatment", "Notes"};
    
    // Define the column names from the query to map the data
    String[] columnNames = 
        {"record_id", "patient_name", "provider_name", "visit_date", 
         "diagnosis", "treatment", "note"};

    // Use dbConfig to display the records
    dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
}


    // Method to update a medical record
private static void updateMedicalRecord() {
    int record_id;

    // Display all available medical records to the user for reference
    System.out.println("\n--- Available Medical Records ---");
    viewMedicalRecords(); // Show all records so user can identify the record to update

    // Loop until a valid Record ID is entered
    do {
        System.out.println("Enter Record ID to update: ");
        record_id = scanner.nextInt();
        scanner.nextLine();  // consume newline

        // Check if the Record ID exists
        if (!isValidID("medical_record", "record_id", record_id)) {
            System.out.println("Record ID does not exist. Please try again.");
        }
    } while (!isValidID("medical_record", "record_id", record_id));

    // SQL query to retrieve the current details of the medical record
    String sqlQuery = 
        "SELECT mr.record_id, pd.p_lname AS patient_name, hp.dr_lname AS provider_name, " +
        "mr.visit_date, mr.diagnosis, mr.treatment, mr.note " +
        "FROM medical_record mr " +
        "JOIN patient_data pd ON mr.p_id = pd.p_id " +
        "JOIN health_provider hp ON mr.dr_id = hp.dr_id " +
        "WHERE mr.record_id = ?";

    // Define the headers to display in the table
    String[] columnHeaders = 
        {"Record ID", "Patient Name", "Provider Name", "Visit Date", 
         "Diagnosis", "Treatment", "Notes"};
    
    // Define the column names from the query to map the data
    String[] columnNames = 
        {"record_id", "patient_name", "provider_name", "visit_date", 
         "diagnosis", "treatment", "note"};

    // Display the current medical record details
    System.out.println("\n--- Current Medical Record Details ---");
    dbConfig.viewRecordsWithParam(sqlQuery, columnHeaders, columnNames, record_id);

    int choice;
    do {
        System.out.println("\nSelect the field to update:");
        System.out.println("1. Diagnosis");
        System.out.println("2. Treatment");
        System.out.println("3. Notes");
        System.out.println("4. Exit Update");
        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();  // consume newline

        String sql;
        switch (choice) {
            case 1:
                System.out.print("Enter new Diagnosis: ");
                String newDiagnosis = scanner.nextLine();
                sql = "UPDATE medical_record SET diagnosis = ? WHERE record_id = ?";
                dbConfig.updateRecord(sql, newDiagnosis, record_id);
                System.out.println("Diagnosis updated successfully!");
                break;

            case 2:
                System.out.print("Enter new Treatment: ");
                String newTreatment = scanner.nextLine();
                sql = "UPDATE medical_record SET treatment = ? WHERE record_id = ?";
                dbConfig.updateRecord(sql, newTreatment, record_id);
                System.out.println("Treatment updated successfully!");
                break;

            case 3:
                System.out.print("Enter new Notes: ");
                String newNotes = scanner.nextLine();
                sql = "UPDATE medical_record SET note = ? WHERE record_id = ?";
                dbConfig.updateRecord(sql, newNotes, record_id);
                System.out.println("Notes updated successfully!");
                break;

            case 4:
                System.out.println("Exiting update...");
                break;

            default:
                System.out.println("Invalid option. Please try again.");
        }
    } while (choice != 4);
}





    // Method to delete a medical record
private static void deleteMedicalRecord() {
    int record_id;

    // Display all medical records before deletion
    System.out.println("--- View All Medical Records ---");
    viewMedicalRecords(); // Show all records for the user to review

    // Loop until a valid Record ID is entered
    do {
        System.out.println("\nEnter Record ID to delete: ");
        record_id = scanner.nextInt();

        // Check if the Record ID exists
        if (!isValidID("medical_record", "record_id", record_id)) {
            System.out.println("Record ID does not exist. Please try again.");
        }
    } while (!isValidID("medical_record", "record_id", record_id));

    // Confirm the deletion
    System.out.println("Are you sure you want to delete the record with ID: " + record_id + "? (y/n)");
    char confirm = scanner.next().charAt(0);

    if (confirm == 'y' || confirm == 'Y') {
        // Perform the deletion if confirmed
        String sql = "DELETE FROM medical_record WHERE record_id = ?";
        dbConfig.deleteRecord(sql, record_id);
        System.out.println("Record deleted successfully!");
    } else {
        System.out.println("Deletion canceled.");
    }
}


    }

