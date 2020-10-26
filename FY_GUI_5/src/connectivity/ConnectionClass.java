package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
	
	public Connection connection;
	
	public Connection getConnection(){
		String dbName = "FDS_SMV";
		String userName = "root";
		String password = "";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?useTimezone=true&serverTimezone=UTC", userName, password);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}

}
