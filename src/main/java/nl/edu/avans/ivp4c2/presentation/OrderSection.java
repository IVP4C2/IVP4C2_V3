package nl.edu.avans.ivp4c2.presentation;

import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Product;
import nl.edu.avans.ivp4c2.domain.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * Handles all operations regarding Orders
 * @author IVP4C2
 */
public class OrderSection {
    private JTable tableLeft = new JTable();
    private JTable tableRight = new JTable();
    public OrderSection() {
    }

    /**
     * Returns a JTable for a given Order object
     * @param o
     * @return JTable with order details
     */
    public JTable getTableRight(Order o) {
        this.tableRight = new JTable(buildTableModelRight(o));
        tableRight.setBorder(BorderFactory
                .createEtchedBorder());
        tableRight.setEnabled(false); // Disable user input
        return tableRight;
    }

    /**
     * Returns a JTable for a given Table object. Requires panelCenter to add the JTable to the frame.
     * @param table
     * @param panelCenter
     * @return JTable with table orders
     */
    public JPanel getTableLeft(Table table, JPanel panelCenter) {
        JPanel barPanel = new JPanel(new GridLayout(1, 2));
        JPanel leftPanel = new JPanel(new GridLayout(1, 1));
        final JPanel rightPanel = new JPanel(new GridLayout(1, 1));
        leftPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        barPanel.add(leftPanel);
        barPanel.add(rightPanel);
        panelCenter.add(barPanel);

        final OrderSection orderSection = new OrderSection();
        tableLeft = new JTable(buildTableModel(table));

        // Add mouse listener
        final Table finalTable = table;
        tableLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 1) {
                    rightPanel.removeAll();
                    final JTable target = (JTable) e
                            .getSource(); // Get left JTable
                    final int row = target.getSelectedRow(); //Get row selected by user
                    int value = (Integer) target.getValueAt(row, 1); // Get value from cell. 'row' is the row clicked by the user, '1' is the second column
                    Order tempOrder = finalTable.getSpecificOrder(value);
                    JTable tableRight = orderSection.getTableRight(tempOrder);
                    rightPanel.add(
                            new JScrollPane(tableRight))
                            .setBackground(Color.WHITE);
                    rightPanel.revalidate();
                }
            }
        });

        leftPanel.add(new JScrollPane(tableLeft))
                .setBackground(Color.WHITE);
        leftPanel.revalidate();
        return barPanel;
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