package nl.edu.avans.ivp4c2.domain;

public class Product {
	private int productNumber;
	private String productName;
	private int amount;
	private double price;
	
	public Product(int productNumber, String productName, int amount, double price) {
		this.productNumber = productNumber;
		this.productName = productName;
		this.amount = amount;
		this.price = price;
	}

	public int getProductNumber() {
		return productNumber;
	}

	public String getProductName() {
		return productName;
	}

	public int getAmount() {
		return amount;
	}
}
