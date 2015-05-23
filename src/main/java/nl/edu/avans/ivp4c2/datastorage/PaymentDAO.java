package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *This DAO retrieves the order data and creates an Order object.
 *This DAO uses the ProductDAO to fill the Order with products
 * @author IVP4C2
 */
public final class PaymentDAO {

    public PaymentDAO() {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    /*Retrieves all orders for a given tableNumber
     * @param int tableNumber
     * @return ArrayList<Order> */
    public final static Payment getActivePayment(int tableNumber) {
        Payment payment = null;

        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT
            // statement.
            //Select all orders for a given tableNumber
            ResultSet resultset = connection.executeSQLSelectStatement("SELECT `b`.`total_price`, `b`.`bill_id`, `b`.`send_on` FROM `bill` `b` " +
                    "INNER JOIN `kpt_billed_order` `kbo` ON `kbo`.`fk_bill_id` = `b`.`bill_id` " +
                    "INNER JOIN `order` `o` ON `kbo`.`fk_order_id` = `o`.`order_id` " +
                    "INNER JOIN `kpt_table_order` `kto` ON `o`.`order_id` = `kto`.`fk_order_id` " +
                    "WHERE `kto`.`fk_table_id` = '"+tableNumber+"';");
            if (resultset != null)

            {
                try {
                    while (resultset.next()) {
                        //Create new Order Object for each record
                        payment = new Payment(
                                resultset.getInt("bill_id"),
                                resultset.getTimestamp("send_on"),
                                resultset.getDouble("total_price")
                        );

                        //Create new ProductDAO to retrieve and create Product Object for the order
                        ProductDAO productDAO = new ProductDAO();
                        ArrayList<Product> products = productDAO.getProductViaBill(resultset.getInt("bill_id")); //Returns an ArrayList with Products
                        payment.addProduct(products);

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
        //Return orders ArrayList to be used in TableDAO
        return payment;
    }
}
