package controller;

import model.Customer;
import model.Parcel;
import model.ParcelMap;
import util.Log;

public class Staff {
    private ParcelMap parcelMap;
    
    public Staff(ParcelMap parcelMap) {
        this.parcelMap = parcelMap;
    }
    
    public void manageCustomer(Customer customer) {
        Parcel parcel = parcelMap.pFind(customer.getParcelID());
        if (parcel != null) {
            double fee = collectionFee(parcel);
            parcel.setCollect(true);
            Log.getInstance().log("Processing customer " + customer.getName() + 
                    " for parcel " + parcel.getParcelID() + 
                    " with fee: $" + String.format("%.2f", fee));
        }
    }
    
    public double collectionFee(Parcel parcel) {
        return 5.0 + (parcel.getWeight() * 2.0) + (parcel.getNoOfDays() * 1.0);
   }
 }