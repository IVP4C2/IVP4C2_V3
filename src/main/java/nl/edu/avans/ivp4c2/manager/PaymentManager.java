package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.datastorage.PaymentDAO;
import nl.edu.avans.ivp4c2.domain.Payment;

/**
 * Manager which handles all operations regarding payments
 * @author IVP4C2
 */
public class PaymentManager {
    private PaymentDAO paymentDAO;

    public PaymentManager() {
        paymentDAO = new PaymentDAO();
    }

    /**
     * Get the payment for a given tableNumber. Return a Payment object
     * @param tableNumber
     * @return payment
     */
    public Payment getPayment(int tableNumber) {
        return paymentDAO.getActivePayment(tableNumber);
    }

    /**
     * Completes a Payment using a given tableNumber.
     * Returns true is the payment was completed successfully
     * @param tableNumber
     * @return true is completed succesfully
     */
    public boolean completePayment(int tableNumber) {
        return paymentDAO.completePayment(tableNumber);
    }

}
