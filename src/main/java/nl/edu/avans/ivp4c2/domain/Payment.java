package nl.edu.avans.ivp4c2.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Matthijsske on 23-5-2015.
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

    public void addProduct(ArrayList<Product> products) {
        for(Product p : products) {
            productList.add(p);
        }
    }

}
