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
			
			connection = DriverManager.getConnection(url, userName, password); //WORKS
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}

}
