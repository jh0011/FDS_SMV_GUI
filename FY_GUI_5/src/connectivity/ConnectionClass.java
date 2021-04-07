package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Establish a connection to the database. Values can be changed as needed.
 */
public class ConnectionClass {
	
	public Connection connection;
	
	public Connection getConnection(){
		String userName = "root"; //Change this as needed
		String password = "root"; //Change this as needed
		String db_name = "fds_db"; //Change this as needed
		String url = "jdbc:mysql://127.0.0.1:3306/" + db_name + "?useTimezone=true&serverTimezone=UTC";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			
			connection = DriverManager.getConnection(url, userName, password); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}

}
