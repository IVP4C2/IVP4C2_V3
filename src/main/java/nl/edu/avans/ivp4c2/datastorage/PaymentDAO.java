package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            ResultSet resultset = connection.executeSQLSelectStatement("SELECT `b`.`total_price`, `b`.`bill_id`, `b`.`send_on` FROM `bill` `b` " +
                    "INNER JOIN `kpt_billed_order` `kbo` ON `kbo`.`fk_bill_id` = `b`.`bill_id` " +
                    "INNER JOIN `order` `o` ON `kbo`.`fk_order_id` = `o`.`order_id` " +
                    "INNER JOIN `kpt_table_order` `kto` ON `o`.`order_id` = `kto`.`fk_order_id` " +
                    "WHERE `kto`.`fk_table_id` = '7' AND `b`.`ispaid` = '0';");
            if (resultset != null)

            {
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
                        ArrayList<Product> products = productDAO.getProductViaBill(resultset.getInt("bill_id")); //Returns an ArrayList with Products
                        payment.addProduct(products); //Adds the ArrayList to the earlier created Payment object
                    }
                }
                catch(SQLException e)
                {
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
     * Updates the 'ispaid' column in the 'bill' table for the given tableNumber.
     * Returns true if the record was updated successfully, false if not.
     * @param tableNumber
     * @return true if updated successfully
     */
    public boolean completePayment(int tableNumber) {
        Boolean result = false;

        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the UPDATE statement.
            //executeUpdateStatement returns a boolean which is stored in result
            result = connection.executeUpdateStatement("UPDATE `bill` `b` " +
                    "INNER JOIN `kpt_billed_order` `kbo` ON `b`.`bill_id` = `kbo`.`fk_bill_id` " +
                    "INNER JOIN `order` `o` ON `kbo`.`fk_order_id` = `o`.`order_id` " +
                    "INNER JOIN `kpt_table_order` `kto` ON `o`.`order_id` = `kto`.`fk_order_id` " +
                    "INNER JOIN `table` `t` ON `kto`.`fk_table_id` = `t`.`table_id` " +
                    "SET `b`.`ispaid` = '1', `t`.`fk_table_status_id` = '4'  " +
                    "WHERE `kto`.`fk_table_id` = '"+tableNumber+"';");
            connection.closeConnection();
        }
        return result;
    }
}
