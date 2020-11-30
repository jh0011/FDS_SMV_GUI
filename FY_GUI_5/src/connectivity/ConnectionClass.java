package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
	
	public Connection connection;
	
	public Connection getConnection(){
		String url = "jdbc:mysql://127.0.0.1:3306/fds_db?useTimezone=true&serverTimezone=UTC";
		String userName = "root";
		String password = "root";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			
			//connection = DriverManager.getConnection(url, userName, password); //WORKS
			connection = DriverManager.getConnection("mysql://zsl9m6thl6ff1n5u:tlzvdrrd8d2b0j9d@aqx5w9yc5brambgl.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/n92wmp3nrx2aa58q");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}

}
