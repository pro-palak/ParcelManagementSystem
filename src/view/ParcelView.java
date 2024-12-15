package view;

import controller.ParcelController;

import java.util.Scanner;

public class ParcelView {
    private ParcelController controller;
    private Scanner scanner;

    public ParcelView(ParcelController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Add Parcel");
            System.out.println("2. Add Customer");
            System.out.println("3. Process Next Customer");
            System.out.println("4. List All Customers");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addParcel();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    processNextCustomer();
                    break;
                case 4:
                    listCustomers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // Method to add a parcel
    private void addParcel() {
        System.out.print("Enter Parcel ID: ");
        String parcelID = scanner.nextLine();
        System.out.print("Enter Weight (kg): ");
        double weight = scanner.nextDouble();
        System.out.print("Enter Number of Days: ");
        int noOfDays = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        controller.addParcel(parcelID, weight, noOfDays);
        System.out.println("Parcel added successfully!");
    }

    // Method to add a customer
    private void addCustomer() {
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Parcel ID: ");
        String parcelID = scanner.nextLine();
        controller.addCustomer(name, parcelID);
        System.out.println("Customer added successfully!");
    }

    // Method to process the next customer in the queue
    private void processNextCustomer() {
        controller.processNextCustomer();
    }

    // Method to list all customers in the queue
    private void listCustomers() {
        controller.listCustomers();
    }
}
