package smpl;

import java.util.Scanner;
import static smpl.Patient.viewPatients;

public class Reports {
    static Scanner scanner = new Scanner(System.in);
    static config dbConfig = new config(); // Using your existing config class

    // Report Panel to handle overall and detailed report options
    public static void reportPanel() {
        int action;
        do {
            System.out.println("REPORTS MENU");
            System.out.println("1. Overall Report ");
            System.out.println("2. Detailed Report ");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter action: ");
            action = scanner.nextInt();

            switch (action) {
                case 1:
                    overallReport();
                    break;
                case 2:
                    detailedReport();
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (action != 3);
    }

    // Method for Overall Report (e.g., summary statistics)
   private static void overallReport() {
    // Displaying Patients' Data with names instead of IDs
    System.out.println("PATIENTS DATA");
    String patientQuery = "SELECT p_id, p_fname, p_birthdate, p_gender, p_contact FROM patient_data";
    String[] patientHeaders = {"Patient ID", "Name", "PATIENT BDATE", "Gender", "Contact"};
    String[] patientColumnNames = {"p_id", "p_fname", "p_birthdate", "p_gender", "p_contact"};
    dbConfig.viewRecords(patientQuery, patientHeaders, patientColumnNames);

    System.out.println("--------------------------------------------------");

    // Displaying Healthcare Providers' Data with names instead of IDs
    System.out.println("HEALTHCARE PROVIDERS DATA");
    String providerQuery = "SELECT dr_id, dr_lname, available_days, available_hours, email FROM health_provider";
    String[] providerHeaders = {"Provider ID", "Name", "Avail Days", "Avail Hours", "Email"};
    String[] providerColumnNames = {"dr_id", "dr_lname", "available_days", "available_hours", "email"};
    dbConfig.viewRecords(providerQuery, providerHeaders, providerColumnNames);

    System.out.println("--------------------------------------------------");

    // Displaying Medical Records Data with names instead of IDs
    System.out.println("MEDICAL RECORDS DATA");
    String recordQuery = 
        "SELECT mr.record_id, p.p_fname AS patient_name, hp.dr_lname AS provider_name, mr.visit_date, mr.diagnosis, mr.treatment " +
        "FROM medical_record mr " +
        "JOIN patient_data p ON mr.p_id = p.p_id " +
        "JOIN health_provider hp ON mr.dr_id = hp.dr_id";
    
    String[] recordHeaders = {"Record ID", "Patient Name", "Provider Name", "Visit Date", "Diagnosis", "Treatment"};
    String[] recordColumnNames = {"record_id", "patient_name", "provider_name", "visit_date", "diagnosis", "treatment"};
    dbConfig.viewRecords(recordQuery, recordHeaders, recordColumnNames);

    System.out.println("--------------------------------------------------");
}


    // Method for Detailed Report (e.g., specific medical records)
  private static void detailedReport() {
    int patientId;

    while (true) {
        System.out.println("\n--- Available Patients ---");
        viewPatients();  // Display list of all patients
        System.out.print("Enter Patient ID to view detailed medical records: ");
        patientId = scanner.nextInt();

        // Check if patient exists before proceeding
        String checkPatientQuery = "SELECT 1 FROM patient_data WHERE p_id = ?";
        if (!dbConfig.checkIfIdExists(patientId)) {
            System.out.println("Patient ID does not exist.");
            continue;  // Ask user to enter a valid patient ID again
        }

        // Check if the patient has any medical records
        String checkRecordQuery = "SELECT COUNT(*) FROM medical_record WHERE p_id = ?";
        if (!dbConfig.checkIfIdExistsForQuery(checkRecordQuery, patientId)) {
            System.out.println("This patient does not have any medical records. Please select another patient.");
            continue;  // Prompt user to select a patient with medical records
        }

        // Query for detailed report (e.g., medical records for this patient)
        String sqlQuery = 
            "SELECT mr.record_id, hp.dr_lname AS provider_name, mr.visit_date, mr.diagnosis, mr.treatment " +
            "FROM medical_record mr " +
            "JOIN health_provider hp ON mr.dr_id = hp.dr_id " +
            "WHERE mr.p_id = ?";

        // Column headers and names for dynamic record display
        String[] columnHeaders = {"Record ID", "Provider Name", "Visit Date", "Diagnosis", "Treatment"};
        String[] columnNames = {"record_id", "provider_name", "visit_date", "diagnosis", "treatment"};

        // Display the detailed report using viewRecordsWithParam
        dbConfig.viewRecordsWithParam(sqlQuery, columnHeaders, columnNames, patientId);
        break;  // Exit loop once valid patient with medical records is selected
    }
}

}
