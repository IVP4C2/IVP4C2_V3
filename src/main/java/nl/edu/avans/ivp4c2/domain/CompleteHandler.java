package nl.edu.avans.ivp4c2.domain;

import nl.edu.avans.ivp4c2.manager.BarManager;
import nl.edu.avans.ivp4c2.manager.PaymentManager;

/**
 * Created by Matthijsske on 26-5-2015.
 */
public class CompleteHandler {
    private int tableNumber;
    private PaymentManager paymentManager;
    private BarManager barManager;
    public CompleteHandler(PaymentManager paymentManager, BarManager barManager) throws Exception {
        this.paymentManager = paymentManager;
        this.barManager = barManager;
    }

    public boolean completeTable(int tableNumber) throws Exception{
        this.tableNumber = tableNumber;

        return false;
    }

}
