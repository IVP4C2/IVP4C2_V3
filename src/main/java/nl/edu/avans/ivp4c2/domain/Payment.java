package nl.edu.avans.ivp4c2.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author IVP4C2
 */
public class Payment {
    private final int paymentNumber;
    private final Date paymentDate;
    private final double totalPrice;
    private final List<Product> productList;

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
     * Returns the Total Price including VAT from the Payment object as a double
     * @return TotalPrice as a double including VAT
     */
    public double getTotalPrice() {
        double totalPriceIncl = 0;
        for(Product p : productList) {
            totalPriceIncl += ((p.getPrice()*(p.getBtw()+100)/100)*p.getAmount());
        }
        return totalPriceIncl;
    }

    /**
     * Returns the Total Price excluding VAT as a double
     * @return Total Price as a double excluding VAT
     */
    public double getTotalPriceExcl() {
        return totalPrice;
    }

    /**
     * Returns an ArrayList containing all the products from the Payment object
     * @return
     */
    public List<Product> getProductList() {
        return productList;
    }

    /*Setters*/

    /**
     * Add a product to the Payment Object. Expects an ArrayList<Product> as parameter.
     * The method loops through the given ArrayList and adds each Product to the class's ArrayList
     * @param products
     */
    public void addProduct(List<Product> products) {
        for(Product p : products) {
            productList.add(p);
        }
    }


    /*Default equals and hashCode methods*/

    /**
     * Returns true if the given Object equals 'this',
     * false if they are not equal
     * @param obj
     * @return True is obj equals 'this'
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Payment payment = (Payment) obj;

        if (paymentNumber != payment.paymentNumber) return false;
        return !(paymentDate != null ? !paymentDate.equals(payment.paymentDate) : payment.paymentDate != null);

    }

    /**
     * Return the hashCode as an int.
     * @return hashCode as int
     */
    @Override
    public int hashCode() {
        int result = paymentNumber;
        result = 31 * result + (paymentDate != null ? paymentDate.hashCode() : 0);
        return result;
    }
}
