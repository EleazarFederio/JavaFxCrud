package system;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connection;

    public Connection connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/bugc", "root", "");
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return connection;
    }

}
