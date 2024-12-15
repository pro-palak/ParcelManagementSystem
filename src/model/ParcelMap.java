package model;

import util.Log;
import java.util.Map;
import java.util.HashMap;

public class ParcelMap {

    private HashMap<String, Parcel> parcelMap;

    public ParcelMap() {
        parcelMap = new HashMap<>();
    }

    public void pAdd(Parcel parcel) {
        parcelMap.put(parcel.getParcelID(), parcel);
        Log.getInstance().log("Parcel " + parcel.getParcelID() + " added to system");
    }

    public Parcel pFind(String id) {
        return parcelMap.get(id);
    }

    public void pRemove(Parcel parcel) {
        parcelMap.remove(parcel.getParcelID());
        Log.getInstance().log("Parcel " + parcel.getParcelID() + " removed from system");
    }

    public Map<String, Parcel> getParcels() {
        return new HashMap<>(parcelMap); // Defensive copy
    }

    public void displayParcels() {
        for (String parcelId : parcelMap.keySet()) {
            System.out.println("Parcel ID: " + parcelId + ", Details: " + parcelMap.get(parcelId));
        }
    }
}
