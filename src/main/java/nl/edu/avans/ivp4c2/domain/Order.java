package nl.edu.avans.ivp4c2.domain;

import java.sql.Time;
import java.util.ArrayList;


public class Order {
	private int orderNumber;
	private String orderStatus;
	private String orderTime;
	private int destination;
	private ArrayList<Product> products;
	
	public Order(int orderNumber, String orderStatus, String orderTime, int destination) {
		this.orderNumber = orderNumber;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		products = new ArrayList<Product>();
		this.destination = destination;
		
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public String getOrderTime() {
		return orderTime;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public int getDestination() {
		return destination;
	}

	
	public void addProduct(Product product) {
		products.add(product);
	}
	
	public void setOrderStatus(String newStatus) {
		this.orderStatus = newStatus;
	}

}
