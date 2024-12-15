package model;

public class Customer {
		private int Qno;
		private String name;
	    private String parcelID;
	    
	    public Customer(int Qno, String name, String parcelID) {
	    		this.Qno = Qno;
	    		this.name = name;
	    		this.parcelID = parcelID;
	    }
	    
	    public String getParcelID() {
	    	return parcelID;
	    }
	    public String getName() {
	    	return name; 
	    }
	    public int getQno() {
	    	return Qno; 
	    }
}
