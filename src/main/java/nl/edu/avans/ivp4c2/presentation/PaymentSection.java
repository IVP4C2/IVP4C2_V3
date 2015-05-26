package nl.edu.avans.ivp4c2.presentation;

import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Payment;
import nl.edu.avans.ivp4c2.domain.Product;
import nl.edu.avans.ivp4c2.domain.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * Created by Matthijsske on 24-5-2015.
 */
public class PaymentSection {
    private JPanel paymentPanel;
    private JButton printBill;
    private JPanel panelSouth;
    private JPanel southMid;
    private JPanel southRight;
    public PaymentSection() {
        printBill = new JButton("Print Bon");
        printBill.setBackground(Color.decode("#DFDFDF"));
        panelSouth = new JPanel(new GridLayout(1, 3));
        southMid = new JPanel(new BorderLayout());
        southRight = new JPanel(new GridLayout(2, 1));
        panelSouth.setBackground(Color.decode("#DFDFDF"));
        southMid.setBackground(Color.decode("#DFDFDF"));
        southRight.setBackground(Color.decode("#DFDFDF"));
    }


    public JPanel getPaymentPanel(Payment p) {
        this.paymentPanel = new JPanel();
        paymentPanel.setLayout(new BorderLayout());
        paymentPanel.add(new JScrollPane(new JTable(buildTableModel(p))), BorderLayout.CENTER);


        southMid.add(new JLabel("Totaal Prijs:         "), BorderLayout.EAST);
        southRight.add(new JLabel("Incl btw: " + String.format("%.2f", p.getTotalPrice()*1.06)));
        southRight.add(new JLabel("Excl btw: " + p.getTotalPrice()));

        panelSouth.add(printBill);
        panelSouth.add(southMid);
        panelSouth.add(southRight);
        paymentPanel.add(panelSouth, BorderLayout.SOUTH);
        return paymentPanel;
    }
    // Method to create JTable
    public DefaultTableModel buildTableModel(Payment payment) {

        // Gets column names from Table
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("ProductNr");
        columnNames.add("ProductNaam");
        columnNames.add("Aantal");
        columnNames.add("Prijs");

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();


        for (Product p : payment.getProductList()) {
            Vector<Object> vector = new Vector<Object>();
            vector.add(p.getProductNumber());
            vector.add(p.getProductName());
            vector.add(p.getAmount());
            vector.add(p.getPrice());
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }
}
