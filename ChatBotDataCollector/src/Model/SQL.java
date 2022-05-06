package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQL 
{
	private String connectionUrl;
    String username;
    String password;
    Connection connection = null;
    public String schema;
    
    public SQL(String ip, int port, String username, String password, String schema)
    {
        changeConnectionURL(ip, port);
        this.username = username;
        this.password = password;
        this.schema = schema;
    }
    
//    public SQL(String ip, int port, String username, String password)
//    {
//        changeConnectionURL(ip, port);
//        this.username = username;
//        this.password = password;
//    }
    
    public void changeConnectionURL(String ip, int port)
    {
        connectionUrl = "jdbc:mysql://" + ip + ":" + port;
    }
    
    public ResultSet executeQuery(String query)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionUrl,username,password);
            Statement statment = connection.createStatement();
            ResultSet rs = statment.executeQuery(query);
            return rs;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
    
    public int executeUpdate(String query)
    {
        try(Connection connectionT = DriverManager.getConnection(connectionUrl,username,password);
        Statement statment = connectionT.createStatement())
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            int numberOfEffectRow = statment.executeUpdate(query);
            closeConnection();
            return numberOfEffectRow;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    
    public void closeConnection()
    {
        try 
        {
            if(connection != null)
                connection.close();
            connection = null;
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
    public void changeSQL(String ip, int port, String username, String password)
    {
    	changeConnectionURL(ip, port);
    	this.username = username;
    	this.password = password;
    }
}
