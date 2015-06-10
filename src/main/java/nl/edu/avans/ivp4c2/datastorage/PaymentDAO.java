package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves the payment for a given tableNumber.
 * @return Payment
 * @author IVP4C2
 */
public final class PaymentDAO {

    public PaymentDAO() {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    /**
     * Retrieves the payment for a given tableNumber and adds products to the created Payment object
     * @param tableNumber
     * @return Payment
     */
    public Payment getActivePayment(int tableNumber) {
        Payment payment = null;

        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT
            // statement.
            //Retrieve the Payment from the database
            ResultSet resultset = connection.executeSQLSelectStatement("SELECT * FROM SelectPayment_V WHERE `fk_table_id` = '"+tableNumber+"';");
            if (resultset != null) {
                try {
                    while (resultset.next()) {
                        //Create new Payment object
                        //Resultset contains the retrieved values
                        payment = new Payment(
                                resultset.getInt("bill_id"),
                                resultset.getTimestamp("send_on"),
                                resultset.getDouble("total_price")
                        );

                        //Create new ProductDAO to retrieve the product from the Database and store them in an ArrayList
                        ProductDAO productDAO = new ProductDAO();
                        List<Product> products = productDAO.getProductViaBill(resultset.getInt("bill_id")); //Returns an ArrayList with Products
                        payment.addProduct(products); //Adds the ArrayList to the earlier created Payment object
                    }
                } catch(SQLException e) {
                    System.out.println(e);
                    payment = null;
                }
            }
            connection.closeConnection();
        }
        //Return Payment object
        return payment;
    }

    /**
     * Updates the 'ispaid' column in the 'bill' table for the given paymentNumber
     * Returns true if the record was updated successfully, false if not.
     * @param paymentNumber
     * @return true if updated successfully
     */
    public boolean completePayment(int paymentNumber) throws SQLException {
        Boolean result = false;
        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the UPDATE statement.
            //executeUpdateStatement returns a boolean which is stored in result
            result = connection.executeUpdateStatement("UPDATE `bill` SET `ispaid` = '1' WHERE `bill_id` = "+paymentNumber+";");
            connection.closeConnection();
        }
        return result;
    }
}
