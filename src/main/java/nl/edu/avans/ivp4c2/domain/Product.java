package nl.edu.avans.ivp4c2.domain;

/**
 * Can be dish or a drink. A product can be a part of an Order and/or Payment.
 * @author IVP4C2
 */
public class Product {
	private int productNumber;
	private String productName;
	private int amount;
	private double price;
	private int btw;
	
	public Product(int productNumber, String productName, int amount, double price, int btw) {
		this.productNumber = productNumber;
		this.productName = productName;
		this.amount = amount;
		this.price = price;
		this.btw = btw;
	}

	/**
	 * Return the productNumber as an int
	 * @return productNumber
	 */
	public int getProductNumber() {
		return productNumber;
	}


	/**
	 * Return the productName as a Strng
	 * @return productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * Returns the amount as an int.
	 * When a customer orders two beers, the amount attribute gets set to two rather than reating two 'beer' Products
	 * @return amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Returns the price as a double.
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Returns the VAT as an Integer.
	 * @return VAT as an Integer
	 */
	public int getBtw() {
		return btw;
	}
}
