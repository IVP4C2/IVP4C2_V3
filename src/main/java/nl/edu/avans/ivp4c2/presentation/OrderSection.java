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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all operations regarding Orders
 * @author IVP4C2
 */
public class OrderSection {
    private JTable tableLeft = new JTable();
    private JTable tableRight = new JTable();
    final JPanel rightPanel = new JPanel(new GridLayout(1, 1));
    private Table table;
    private Order tempOrder;
    public OrderSection() {
    }

    /**
     * Returns a JTable for a given Order object
     * @param o The Order Object from which the right JTable should be created
     * @return JPanel with order details
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
     * @param table The Table Object from which the left JTable should be created
     * @param panelCenter JPanel with all acctive order for the given Table
     * @return JTable with table orders
     */
    public JPanel getTableLeft(Table table, JPanel panelCenter) {
        this.table = table;
        JPanel barPanel = new JPanel(new GridLayout(1, 2));
        JPanel leftPanel = new JPanel(new GridLayout(1, 1));
        leftPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        barPanel.add(leftPanel);
        barPanel.add(rightPanel);
        panelCenter.add(barPanel);
        tableLeft = new JTable(buildTableModel(table));

        // Add mouse listener
        final Table finalTable = table;
        tableLeft.addMouseListener(new OrderHandler());
        leftPanel.add(new JScrollPane(tableLeft))
                .setBackground(Color.WHITE);
        leftPanel.revalidate();
        return barPanel;
    }

    /**
     * Finds the Order matching the clicked Order in the left JTable
     */
    private class OrderHandler extends MouseAdapter{
        @Override
        public void mouseClicked(final MouseEvent e) {
            if (e.getClickCount() == 1) {
                rightPanel.removeAll();
                final JTable target = (JTable) e
                        .getSource(); // Get left JTable
                final int row = target.getSelectedRow(); //Get row selected by user
                int value = (Integer) target.getValueAt(row, 1); // Get value from cell. 'row' is the row clicked by the user, '1' is the second column
                tempOrder = table.getSpecificOrder(value);
                JTable tableRight = getTableRight(tempOrder);
                rightPanel.add(
                        new JScrollPane(tableRight))
                        .setBackground(Color.WHITE);
                rightPanel.revalidate();
            }
        }
    }

    /**
     * Returns the OrderNumber from the selected order
     * @return OrderNumber as an Integer
     */
    public Order getSelectedOrder() throws NullPointerException{
        Order order;
        try {
            order = tempOrder;
        } catch (NullPointerException e) {
            throw new NullPointerException("Geen bestelling geselecteerd");
        }
        return order;
    }


    public void clearSelectedOrder() {
        tempOrder = null;
    }

    /**
     * Creates the DefaultTableModel for the Table object to fill the left JTable
     * @param t The Table object from which to create a JTable
     * @return DefaultTableModel
     */
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


    /**
     * Creates the DefaultTableModel for the given Order object to fill the right JTable
     * @param order The Order from which to create the right JTable
     * @return DafaultTableModel
     */
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