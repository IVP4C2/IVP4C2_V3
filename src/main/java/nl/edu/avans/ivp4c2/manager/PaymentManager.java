package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.datastorage.PaymentDAO;
import nl.edu.avans.ivp4c2.domain.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Manager which handles all operations regarding payments
 * @author IVP4C2
 */
public class PaymentManager {
    private PaymentDAO paymentDAO;
    private HashMap<Integer, Payment> paymentMap; //Holds all the Payment Objects

    public PaymentManager() {
        paymentDAO = new PaymentDAO();
        paymentMap = new HashMap<Integer, Payment>();
    }

    /**
     * Get the payment for a given tableNumber. Returns a Payment object
     * @param tableNumber
     * @return payment
     */
    public Payment getActivePayment(int tableNumber) {
        if (paymentMap.containsKey(tableNumber)){
            return paymentMap.get(tableNumber);
        }
        else {
            Payment newPayment = paymentDAO.getActivePayment(tableNumber);
            paymentMap.put(tableNumber, newPayment);
            return newPayment;
        }
    }

    /**
     * Completes a Payment using a given tableNumber.
     * Returns true is the payment was completed successfully
     * @param tableNumer
     * @return true is completed succesfully
     */
    public boolean completePayment(int tableNumer) {
        boolean result = false;
        if(paymentMap.containsKey(tableNumer)) {
            if(paymentDAO.completePayment(paymentMap.get(tableNumer).getPaymentNumber())) {
                paymentMap.remove(tableNumer);
                result = true;
            }
        }
        else {
            result = false;
        }
        return result;
    }
}
