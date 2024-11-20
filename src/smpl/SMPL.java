package smpl;

import java.util.Scanner;

public class SMPL {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int action;

        do {
            System.out.println("WELCOME TO MEDICAL RECORD APP");
            System.out.println("1. Patient Data");
            System.out.println("2. Health Provider");
            System.out.println("3. Medical Record");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.print("Enter action: ");
            action = scanner.nextInt();

            switch (action) {
                case 1:
                    Patient.patientPanel();
                    break;
                case 2:
                    HealthProvider.healthProviderPanel();
                    break;
                case 3:
                    MedicalRecord.medicalRecordPanel();
                    break;
                case 4:
                    Reports.reportPanel(); // Call the Reports panel
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (action != 5);

        scanner.close();
    }
}
