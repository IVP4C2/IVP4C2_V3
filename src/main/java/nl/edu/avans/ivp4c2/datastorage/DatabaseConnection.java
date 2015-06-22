/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.edu.avans.ivp4c2.datastorage;

import java.sql.*;

/**
 *Used to connect to the Database. Contains the connection details and handles CRUD and Exceptions
 * @author IVP4C2
 */
public class DatabaseConnection {
    
    private Connection connection;
    private static final String DB_NAME = "jdbc:mysql://mysql.famcoolen.nl/avans_hartigehap_all";
    private static String DB_USER = "ivp4_admin";
    private static String DB_PASS = "hHzCPejUBA";
    
    // The Statement object has been defined as a field because some methods
    // may return a ResultSet object. If so, the statement object may not
    // be closed as you would do when it was a local variable in the query
    // execution method.
    private Statement statement;
    
    public DatabaseConnection() {
        connection = null;
        statement = null;
    }

//    /**
//     * This user is allowed to execute SELECT queries.
//     * Should be used when only SELECTing data from the database. Should be used together with views
//     * @return
//     */
//    public boolean connectAsSelect() {
//        DB_USER = "Barselect";
//        DB_PASS = "hhc2barselect";
//        return openConnection();
//    }
//
//    /**
//     * This user is allowed to execute INSERT, UPDATE and SELECT statements
//     * Should be used when INSERTing or UPDATEing a row. It has the SELECT privilege so it can return results when needed
//     * @return
//     */
//    public boolean connectAsUDI() {
//        DB_USER = "Barudi";
//        DB_PASS = "hhc2barudi";
//        return openConnection();
//    }
    
    public boolean openConnection() {
        boolean result = false;

        if(connection == null) {
            try {
                // Try to create a connection with the library database
                connection = DriverManager.getConnection(DB_NAME, DB_USER, DB_PASS);
                if(connection != null) {
                    statement = connection.createStatement();
                }
                result = true;
            } catch(SQLException e) {
                System.out.println(e);
                result = false;
            }
        } else {
            // A connection was already initalized.
            result = true;
        }
        return result;
    }
    
    public boolean connectionIsOpen() {
        boolean open = false;
        
        if(connection != null && statement != null) {
            try {
                open = !connection.isClosed() && !statement.isClosed();
            } catch(SQLException e) {
                System.out.println(e);
                open = false;
            }
        }
        // Else, at least one the connection or statement fields is null, so
        // no valid connection.
        
        return open;
    }
    
    public void closeConnection() {
        try {
            statement.close();
            
            // Close the connection
            connection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public ResultSet executeSQLSelectStatement(String query) {
        ResultSet resultset = null;
        
        // First, check whether a some query was passed and the connection with
        // the database.
        if(query != null && connectionIsOpen()) {
            // Then, if succeeded, execute the query.
            try {
                resultset = statement.executeQuery(query);
            } catch(SQLException e) {
                System.out.println(e);
                resultset = null;
            }
        }
        
        return resultset;
    }
    
    public boolean executeSQLDeleteStatement(String query) {
        boolean result = false;
        
        // First, check whether a some query was passed and the connection with
        // the database.
        if(query != null && connectionIsOpen()) {
            // Then, if succeeded, execute the query.
            try {
                statement.executeUpdate(query);
                result = true;
            } catch(SQLException e) {
                System.out.println(e);
                result = false;
            }
        }
        
        return result;
    }
    
    public boolean executeUpdateStatement(String query){
        
        boolean result = false;
        
        if(query != null && connectionIsOpen()) {
            try {
                statement.executeUpdate(query);
                result = true;
            } catch(SQLException e) {
                System.out.println(e);
                result = false;
            }
        }
        return result;
    }
}
