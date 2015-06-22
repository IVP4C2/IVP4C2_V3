package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.datastorage.PaymentDAO;
import nl.edu.avans.ivp4c2.domain.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * PaymentManager is a manager which handles all operations regarding payments
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
     * @param tableNumber number of the table
     * @return payment for a active table
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
     * @param tableNumer of the table
     * @return result boolean true if a payment is succesfully completed or boolean false is unsuccesfully completed
     * @throws Exception if the completion of a payment fails
     */
    public boolean completePayment(int tableNumer) throws Exception {
        boolean result = false;
        if(paymentMap.containsKey(tableNumer)) {
            if(paymentDAO.completePayment(paymentMap.get(tableNumer).getPaymentNumber())) {
                paymentMap.remove(tableNumer);
                result = true;
            } else {
                throw new Exception("Afronden mislukt. Geen verbinding met de Database");
            }
        } else {
            throw new Exception("Afronden mislukt");
        }
        return result;
    }
}
