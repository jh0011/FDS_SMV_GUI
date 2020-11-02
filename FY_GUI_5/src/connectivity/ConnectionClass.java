package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
	
	public Connection connection;
	
	public Connection getConnection(){
		String dbName = "FDS_SMV";
		String url = "jdbc:mysql://freedb.tech:3306/freedbtech_FDS_SMV";
		String userName = "freedbtech_jashini";
		String password = "root";
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?useTimezone=true&serverTimezone=UTC", userName, password); //WORKS
			//connection = DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12374026", "sql12374026", "1YTc52bNUt"); //WORKS
			
			connection = DriverManager.getConnection(url, userName, password); //WORKS
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}

}
