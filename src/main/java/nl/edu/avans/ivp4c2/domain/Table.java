package nl.edu.avans.ivp4c2.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Table has Orders and Orders have Products
 * 
 * @author IVP4C2
 */


public class Table {
	private int tableNumber;
	private String tableStatus;
	private ArrayList<Order> orders;
	private HashMap<Integer, Order> specificOrder;
	
	public Table(int tableNumber, String tableStatus) {
		this.tableNumber = tableNumber;
		this.tableStatus = tableStatus;
		specificOrder = new HashMap<Integer, Order>();
		orders = new ArrayList<Order>();
	}
	
	//Getters
	public int getTableNumber() {
		return tableNumber;
	}
	
	public String getTableStatus() {
		return tableStatus;
	}

	
	public ArrayList<Order> getOrders() {
		return orders;
	}

	public Order getSpecificOrder(int orderNumber) {
		return specificOrder.get(orderNumber);
	}
	
	//Setters
	public void addOrder(Order order) {

		orders.add(order);
		specificOrder.put(order.getOrderNumber(), order);

	}
}
