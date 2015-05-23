package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *Retrieves the order data and creates an Order object.
 *This DAO uses the ProductDAO to fill the Order with products
 * @author IVP4C2
 */
public final class OrderDAO {

	public OrderDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}
	
	/**Retrieves all orders for a given tableNumber
	 * @param tableNumber
	 * @return ArrayList<Order> */
	public final static ArrayList<Order> getTableOrder(int tableNumber) {
		ArrayList<Order> orders = new ArrayList<Order>();

		// First open a database connnection
		DatabaseConnection connection = new DatabaseConnection();
		if (connection.openConnection()) {
			// If a connection was successfully setup, execute the SELECT
			// statement.
			//Select all orders for a given tableNumber
			ResultSet resultset = connection.executeSQLSelectStatement("SELECT `o`.`order_id`, `o`.`send_on`, `s`.`name`, `o`.`destination` " +
                            "FROM `order` `o` INNER JOIN `status` `s` ON `o`.`fk_status_id` = `s`.`status_id` " +
                            "INNER JOIN `kpt_orderline` `ktol` ON `ktol`.`fk_order_id` = `o`.`order_id` " +
                            "INNER JOIN `kpt_table_order` `ktto` on `ktto`.`fk_order_id` = `o`.`order_id` " +
                            "WHERE `ktto`.`fk_table_id` = '"+tableNumber+"' " +
                            "AND ((`destination` = '1' AND `name` IN ('In behandeling', 'Bestelling is geplaatst', 'Gereed')) " +
                            "OR (`destination` = '2' AND `name` = 'Gereed')) GROUP BY `o`.`order_id` ORDER BY `o`.`destination` DESC;");
            if (resultset != null)

            {
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
                           ArrayList<Product> products = productDAO.getProductViaOrder(resultset.getInt("order_id")); //Returns an ArrayList with Products
                           for(Product p : products) { //Add product to the new Order
                               newOrder.addProduct(p);
                           }
                        System.out.println(newOrder.getOrderTime());
                        orders.add(newOrder);

                    }
                    
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    orders = null;
                }
            }

            connection.closeConnection();
        }
		//Return orders ArrayList to be used in TableDAO
		return orders;
	}
}
