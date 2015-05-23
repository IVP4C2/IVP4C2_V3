package nl.edu.avans.ivp4c2.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author IVP4C2
 */

public class Order {
	private int orderNumber;
	private String orderStatus;
	private Timestamp orderTime;
	private int destination;
	private ArrayList<Product> products;
	
	public Order(int orderNumber, String orderStatus, Timestamp orderTime, int destination) {
		this.orderNumber = orderNumber;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		products = new ArrayList<Product>();
		this.destination = destination;
		
	}

	/*Getters*/
	/**
	 * Return the orderNumber
	* @return returns the orderNumber
	* */
	public int getOrderNumber() {
		return orderNumber;
	}

	/**
	 * returns the full timestamp as a Date. By returning the full timestamp a Date,
	it can be used a different points in the code
	* @return returns the full timestamp as a Date
	* */
	public Timestamp getOrderTime() {
		return orderTime;
	}

	/**
	 * Returns the orderStatus
	 * @return orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Returns the Orders' Destination
	 * @return Destination
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * Returns an ArrayList containing all products for the order
	* @return ArrayLists containing products
	* */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/*Setters*/
	/**
	 * Adds the product given a parameter to the Orders' ArrayList
	 * @param product
	 */
	public void addProduct(Product product) {
		products.add(product);
	}

	/**
	 * Sets the order status using a given String
	 * @param newStatus
	 */
	public void setOrderStatus(String newStatus) {
		this.orderStatus = newStatus;
	}

}
