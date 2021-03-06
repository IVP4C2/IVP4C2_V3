package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Access Object for the Order class. Handles all operation regarding the Order class in which a database is used
 * This DAO uses the ProductDAO to fill the Order with products
 * @author IVP4C2
 */
public final class OrderDAO {

	public OrderDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}

    /**
     * Retrieves all orders for a given tableNumber
     * @param tableNumber is the table number of a table
     * @return a list of orders
     */
	public final static List<Order> getTableOrder(int tableNumber) {
		List<Order> orders = new ArrayList<Order>();

		// First open a database connnection
		DatabaseConnection connection = new DatabaseConnection();
		if (connection.openConnection()) {
			// If a connection was successfully setup, execute the SELECT
			// statement.
			//Select all orders for a given tableNumber
			ResultSet resultset = connection.executeSQLSelectStatement("SELECT * FROM selectorders_v " +
                    "WHERE `fk_table_id` = '"+tableNumber+"' " +
                    "AND ((`destination` = '2' AND `name` IN ('In behandeling', 'Bestelling is geplaatst', 'Gereed')) " +
                    "OR (`destination` = '1' AND `name` = 'Gereed')) " +
                    "GROUP BY `order_id` " +
                    "ORDER BY `destination` ASC;");
            if (resultset != null) {
                try {
                    while (resultset.next()) {
                        //Create new Order Object for each record
                       Order newOrder = new Order(
                    		   resultset.getInt("order_id"),
                    		   resultset.getString("name"),
                    		   resultset.getTimestamp("send_on"),
                               resultset.getInt("destination")
                    		   );
                       
                           //Create new ProductDAO to retrieve and create Product Object for the order
                           ProductDAO productDAO = new ProductDAO();
                           List<Product> products = productDAO.getProductViaOrder(resultset.getInt("order_id")); //Returns an ArrayList with Products
                           for(Product p : products) { //Add product to the new Order
                               newOrder.addProduct(p);
                           }
                        orders.add(newOrder);
                    }
                } catch(SQLException e) {
                    Logger logger = Logger.getAnonymousLogger();
                    logger.log(Level.SEVERE, "an exception was thrown in the OrderDAO", e);
                    orders = null;
                }
            }
            connection.closeConnection();
        }
		//Return orders ArrayList to be used in TableDAO
		return orders;
	}

    /**
     * @param orderNumber the number of the order
     * @param status the status of the order
     * @return true if the query was a success, false otherwise
     * @throws SQLException throws a exception if something is wrong with the sql.
     */
    public boolean updateOrder(int orderNumber, int status) throws SQLException{
        boolean result = false;
        DatabaseConnection connection = new DatabaseConnection();
        if(connection.openConnection()) {
            String updateStatement = "UPDATE `order` SET `fk_status_id` = '"+status+"' WHERE `order_id` = "+orderNumber+";";
            result = connection.executeUpdateStatement(updateStatement);
        }
        return result;
    }
}
