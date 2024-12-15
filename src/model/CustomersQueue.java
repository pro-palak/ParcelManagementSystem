package model;

import util.Log;
import java.util.Queue;
import java.util.LinkedList;

public class CustomersQueue {
	private Queue<Customer> customers;
	
	public CustomersQueue() {
        customers = new LinkedList<>();
    }
	
	public void add(Customer customer) {
        customers.offer(customer);
        Log.getInstance().log("Customer " + customer.getName() + " added to queue");
    }
	
	public Customer remove() {
        Customer customer = customers.poll();
        if (customer != null) {
            Log.getInstance().log("Customer " + customer.getName() + " removed from queue");
        }
        return customer;
    }
	
	public boolean checkempty() {
        return customers.isEmpty();
    }
	
	public Queue<Customer> getCustomers() {
        return new LinkedList<>(customers);
    }

	public static void enqueue(String name, Parcel parcel) {
				
	}
}
