package nl.edu.avans.ivp4c2.domain;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Order {
	private int orderNumber;
	private String orderStatus;
	private Date orderTime;
	private int destination;
	private ArrayList<Product> products;
	
	public Order(int orderNumber, String orderStatus, Date orderTime, int destination) {
		this.orderNumber = orderNumber;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		products = new ArrayList<Product>();
		this.destination = destination;
		
	}

	/*
	* @return returns an ArrayLists of products
	* */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/*
	* @return returns the order number
	* */
	public int getOrderNumber() {
		return orderNumber;
	}

	/*returns the full timestamp as a Date. By returning the full timestamp a Date,
	it can be used a different points in the code
	* @return returns the full timestamp as a Date
	* */
	public Date getOrderTime() {
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
