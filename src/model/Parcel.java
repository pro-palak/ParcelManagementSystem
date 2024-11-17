package model;

public class Parcel {
	    private String parcelID;
	    private double weight;
	    private int noOfDays;
	    
	    public Parcel(String parcelID, double weight, int noOfDays) {
	        this.parcelID = parcelID;
	        this.weight = weight;
	        this.noOfDays = noOfDays;
	    }
}