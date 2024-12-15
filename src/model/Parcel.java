package model;

import util.StatusP;

public class Parcel {
	    private String parcelID;
	    private double weight;
	    private int noOfDays;
	    private StatusP status;
	    private String dimensions;
	    
	    public Parcel(String parcelID, double weight, int noOfDays, StatusP string, String dimensions) {
	        this.parcelID = parcelID;
	        this.weight = weight;
	        this.noOfDays = noOfDays;
	        this.status = string;
	        this.dimensions = dimensions;
	    }
	   
	    public String getParcelID() {
	    	return parcelID; 
	    }
	    public double getWeight() {
	    	return weight; 
	    }
	    public int getNoOfDays() {
	    	return noOfDays; 
	    }
	    public StatusP getStatus() {
	    	return status;  
	    } 
	    
	    public String getDimensions() {
	        return dimensions;
	    }
	    
	    public void setCollect(boolean collected) {
	    	if (collected) {
	    	    status = StatusP.COLLECTED;
	    	} else {
	    	    status = StatusP.PENDING;
	    	}
	    }
}