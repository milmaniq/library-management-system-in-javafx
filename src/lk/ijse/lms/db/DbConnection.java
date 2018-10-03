/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ilman Iqbal
 */
public class DbConnection {
    private Connection connection;
    public static DbConnection dbConnection;

    public DbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lms", "root", "123456");
        
    }
    
    
    public static DbConnection getInstance() throws ClassNotFoundException, SQLException{
        if (dbConnection == null){
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
            
            
}
