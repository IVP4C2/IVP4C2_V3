package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.datastorage.DatabaseConnection;
import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *This DAO retrieves and creates the product for a given orderNumber
 * @author IVP4C2
 */
public class ProductDAO {

	public ProductDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}

	public ArrayList<Product> getProductViaOrder(int orderNumber) {
		String statement = "SELECT `item_id`, `name`, `price`, COUNT(*) AS amount " +
				"FROM `item` `i` " +
				"INNER JOIN `kpt_orderline` `ktol` ON `i`.`item_id` = `ktol`.`fk_item_id` " +
				"INNER JOIN `order` `o` ON `o`.`order_id` = `ktol`.`fk_order_id` " +
				"WHERE `o`.`order_id` = '"+orderNumber+"' GROUP BY `item_id`;";
		return getProduct(statement);
	}

	public ArrayList<Product> getProductViaBill(int billId) {
		String statement = "SELECT `item_id`, `name`, `price`, COUNT(*) AS amount FROM `item` `i` " +
				"INNER JOIN `kpt_orderline` `kol` ON `i`.`item_id` = `kol`.`fk_item_id` " +
				"INNER JOIN `order` `o` ON `kol`.`fk_order_id` = `o`.`order_id` " +
				"INNER JOIN `kpt_billed_order` `kbo` ON `kbo`.`fk_order_id` = `o`.`order_id` " +
				"WHERE `kbo`.`fk_bill_id` = '"+billId+"';";
		return getProduct(statement);
	}

	/*Retrieves all products for a given orderNumber
	 * @param int orderNUmber
	 * @return ArrayList<Product> */
	public ArrayList<Product> getProduct(String statement) {
		
		ArrayList<Product> products = new ArrayList<Product>();
		//Open db connection
		DatabaseConnection connection = new DatabaseConnection();
		if(connection.openConnection()) {
			//connection opened succesfully
			//execute SQL statement to retrieve Tables
			//Select all product for a given orderNumber
			ResultSet resultset = connection.executeSQLSelectStatement(statement);

	            if(resultset != null)
	            {
	                try
	                {
	                    while(resultset.next())
	                    {
	                    	//Create a new Product for each record
	                       Product newProduct = new Product(
								   resultset.getInt("item_id"),
								   resultset.getString("name"),
								   resultset.getInt("amount"),
								   resultset.getDouble("price"));
	                       products.add(newProduct); //Add newProduct to product ArrayList
		                }       
	                }
	            
	                catch(SQLException e)
	                {
	                    System.out.println(e);
	                    products = null;
	                }
	            }

	            connection.closeConnection();
	        }
	        //Return products ArrayList to be used in the OrderDAO
	        return products;
	    }
}
