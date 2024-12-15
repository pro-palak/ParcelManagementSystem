package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import model.Customer;
import model.CustomersQueue;
import model.Parcel;
import model.ParcelMap;
import util.CSVReader;
import util.StatusP;
import view.MainWindow;

public class ParcelController {
    private CustomersQueue queue;
    private ParcelMap parcelMap;
    private Staff staff;
    private MainWindow mainWindow;  // Changed from ParcelView to MainWindow
    private Customer currentCustomer;
    
    public ParcelController() {
        this.parcelMap = new ParcelMap();
        this.staff = new Staff(parcelMap);
        this.queue = new CustomersQueue();
        this.mainWindow = new MainWindow(this);  // Create MainWindow with reference to controller
    }

    public void addParcel(String parcelID, double weight, int noOfDays, String dimensions) {
        
    	Parcel parcel = new Parcel(parcelID, weight, noOfDays, StatusP.PENDING, dimensions);
        parcelMap.pAdd(parcel);
        System.out.println("Parcel added successfully: " + parcel);
        if (mainWindow != null) {
            mainWindow.updateDisplay();  // Update the display through MainWindow
        }
    }

    // Overloaded method for backward compatibility
    public void addParcel(String parcelID, double weight, int noOfDays) {
        addParcel(parcelID, weight, noOfDays, "Not specified");
    }

    public void addCustomer(String name, String parcelID) {
        int qno = queue.getCustomers().size() + 1;
        Customer customer = new Customer(qno, name, parcelID);
        queue.add(customer);
        System.out.println("Customer added successfully: " + customer);
        if (mainWindow != null) {
            mainWindow.updateDisplay();
        }
    }

    public void processNextCustomer() {
        if (!queue.checkempty()) {
            Customer customer = queue.remove();
            this.currentCustomer = customer;
            staff.manageCustomer(customer);
            System.out.println("Processed customer: " + customer);
            if (mainWindow != null) {
                mainWindow.updateDisplay();
            }
        } else {
            System.out.println("No customers in the queue.");
        }
    }

    public CustomersQueue getQueue() {
        return queue;
    }

    public ParcelMap getParcelMap() {
        return parcelMap;
    }

    public void loadParcelsFromCSV(String filePath) {
    	String fullPath = System.getProperty("user.dir") + "/src/Data/Parcels.csv";
    	List<String[]> data = CSVReader.readCSV(fullPath);
        if (data != null) {
            for (String[] row : data) {
                try {
                    String parcelID = row[0];
                    double weight = Double.parseDouble(row[1]);
                    int noOfDays = Integer.parseInt(row[2]);
                    String dimensions = row.length > 3 ? row[3] : "Not specified";
                    addParcel(parcelID, weight, noOfDays, dimensions);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error parsing parcel data: " + String.join(" ", row));
                }
            }
            System.out.println("Parcels loaded from " + filePath);
            if (mainWindow != null) {
                mainWindow.updateDisplay();
            }
        }
    }

    public void loadCustomerData(String filename) {
    	String fullPath = System.getProperty("user.dir") + "/src/Data/Custs.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String name = data[0].trim();
                    String parcelID = data[1].trim();
                    addCustomer(name, parcelID);
                }
            }
            if (mainWindow != null) {
                mainWindow.updateDisplay();
            }
        } catch (IOException e) {
            System.err.println("Error reading customer file: " + e.getMessage());
        }
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

	public void listCustomers() {
		// TODO Auto-generated method stub
		
	}
	public Customer getCurrentCustomer() {
        return currentCustomer;
    }
	
}