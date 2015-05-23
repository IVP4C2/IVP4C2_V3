package nl.edu.avans.ivp4c2.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author IVP4C2
 */
public class Payment {
    private final int paymentNumber;
    private final Date paymentDate;
    private final double totalPrice;
    private final ArrayList<Product> productList;

    public Payment(int paymentNumber, Date paymentDate, double totalPrice) {
        this.paymentNumber = paymentNumber;
        this.paymentDate = paymentDate;
        this.totalPrice = totalPrice;
        productList = new ArrayList<Product>();
    }

    /*Getters*/

    /**
     * Return the Paymentumber from the Payment Object
     * @return PaymentNumber int
     */
    public int getPaymentNumber() {
        return paymentNumber;
    }

    /**
     * Returns the PaymentDate fro the Payment object
     * @return PaymentDate Date
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * Returns the Total Price from the Payment object
     * @return TotalPrice double
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Returns an ArrayList containing all the products from the Payment object
     * @return
     */
    public ArrayList<Product> getProductList() {
        return productList;
    }

    /*Setters*/

    /**
     * Add a product to the Payment Object. Expects an ArrayList<Product> as parameter.
     * The method loops through the given ArrayList and adds each Product to the class's ArrayList
     * @param products
     */
    public void addProduct(ArrayList<Product> products) {
        for(Product p : products) {
            productList.add(p);
        }
    }
}
