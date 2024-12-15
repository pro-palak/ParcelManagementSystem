package main;

import controller.ParcelController;

public class mainApp {
    public static void main(String[] args) {
        ParcelController controller = new ParcelController();
        controller.loadParcelsFromCSV("Parcels.csv");
        controller.loadCustomerData("Custs.csv");
        
        controller.getMainWindow().setVisible(true);
    }
}