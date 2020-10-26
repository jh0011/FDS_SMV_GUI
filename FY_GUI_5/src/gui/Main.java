package gui;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import connectivity.ConnectionClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Values.initValues();
			Parent root;
			root = FXMLLoader.load(getClass().getResource("Intro.fxml"));
			Scene introScene = new Scene(root, 870, 710);
			primaryStage.setScene(introScene);
			primaryStage.setTitle("FDS-SMV GUI");
			primaryStage.show();
			initDB();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDB(){
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			String sqlHead = "DELETE FROM head;";
			String sqlTime = "DELETE FROM time;";
			String initHead = "INSERT INTO head(CHID, TITLE) VALUES (' ', ' ');";
			String initTime = "INSERT INTO time (EndTime, StartTime, DT) VALUES ('', '', '');";
			Statement statement;
			statement = connection.createStatement();
			statement.executeUpdate(sqlHead);
			statement.executeUpdate(sqlTime);
			statement.executeUpdate(initHead);
			statement.executeUpdate(initTime);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("DATABASE NOT SET CORRECTLY");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
