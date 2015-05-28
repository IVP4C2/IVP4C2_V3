package nl.edu.avans.ivp4c2.presentation;

import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Product;
import nl.edu.avans.ivp4c2.domain.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * Created by Matthijsske on 24-5-2015.
 */
public class OrderSection {
    private JTable tableLeft = new JTable();
    private JTable tableRight = new JTable();
    public OrderSection() {
    }

    public JTable getTableRight(Order o) {
        this.tableRight = new JTable(buildTableModelRight(o));
        tableRight.setBorder(BorderFactory
                .createEtchedBorder());
        tableRight.setEnabled(false); // Disable user input
        return tableRight;
    }

    public JTable getTableLeft(Table t) {
        this.tableLeft = new JTable(buildTableModel(t));
        tableLeft.setBorder(BorderFactory.createEtchedBorder());
        tableLeft.getTableHeader().setReorderingAllowed(false); // Added
        return tableLeft;
    }
    // Method to create JTable
    public DefaultTableModel buildTableModel(Table t) {

        // Gets column names from Table
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("TafelNr");
        columnNames.add("BestelNr");
        columnNames.add("Tijd");
        columnNames.add("Status");

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();


        for (Order o : t.getOrders()) {
            Vector<Object> vector = new Vector<Object>();
            vector.add(t.getTableNumber());
            vector.add(o.getOrderNumber());
            vector.add(new SimpleDateFormat("HH:mm:ss").format(o.getOrderTime())); //Formats the Date to a useful string
            vector.add(o.getOrderStatus());
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }


    // Method to create JTable
    public DefaultTableModel buildTableModelRight(Order order) {

        // Gets column names from Table
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("ProductNaam");
        columnNames.add("Aantal");

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();

        for (Product p : order.getProducts()) {
            Vector<Object> vector = new Vector<Object>();
            vector.add(p.getProductName());
            vector.add(p.getAmount());
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}