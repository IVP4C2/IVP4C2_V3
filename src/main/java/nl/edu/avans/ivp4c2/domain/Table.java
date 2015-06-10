package nl.edu.avans.ivp4c2.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

	/**
	 * Returns the table number as an integer
	 * @return tablenumber as an integer
	 */
	public int getTableNumber() {
		return tableNumber;
	}

	/**
	 * Returns the table status as a string.
	 * Table status can be 'Bezet', 'Leeg', 'Hulp' or 'Afrekenen'
	 * @return table status as a string
	 */
	public String getTableStatus() {
		return tableStatus;
	}

	/**
	 * Returns all the orders which have not been served or payed
	 * @return orders in an ArrayList
	 */
	public ArrayList<Order> getOrders() {
		return orders;
	}

	/**
	 * Returns an Order object for a given orderNumber
	 * @param orderNumber
	 * @return Order
	 */
	public Order getSpecificOrder(int orderNumber) {
		return specificOrder.get(orderNumber);
	}
	
	//Setters

	/**
	 * Add and Order to the Table's lists
	 * @param order
	 */
	public void addOrder(Order order) {

		orders.add(order);
		specificOrder.put(order.getOrderNumber(), order);

	}

	/*Default equals an hashCode methods*/
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Table table = (Table) o;

		return tableNumber == table.tableNumber;

	}

	@Override
	public int hashCode() {
		return tableNumber;
	}
}
