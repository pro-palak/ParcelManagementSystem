package main;

import controller.Manager;

public class mainApp {
    public static void main(String[] args) {
    	Manager controller = new Manager();
        controller.loadParcelsFromCSV("Parcels.csv");
        controller.loadCustomerData("Custs.csv");
        
        controller.getMainWindow().setVisible(true);
    }
}