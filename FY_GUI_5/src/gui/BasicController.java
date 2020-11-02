package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class BasicController implements Initializable{
	
	@FXML Button basicNextBtn;
	@FXML Button basicBackBtn;
	@FXML Button cancelBtn;
	@FXML TextField chidText = new TextField();
	@FXML TextArea titleText;
	
	protected String chid;
	protected String title;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	@FXML
	private void goToTime(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		//store values
//		Values.allStrings[0][0] = chidText.getText();
//		Values.allStrings[1][0] = titleText.getText();
		
		if (checkChid(chidText.getText())){
			String sql = "INSERT INTO head (CHID, TITLE) VALUES ('" + chidText.getText() + "', '" + titleText.getText() + "');";
			
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Time.fxml"));
			Parent root = loader.load();
			
			TimeController timeCont = loader.getController(); //Get the next page's controller
			timeCont.showInfo(); //Set the values of the page //Values.T_END, Values.T_BEGIN, Values.DT
			Scene timeScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(timeScene);
			mainWindow.show();
		}
		
		
	}
	
	@FXML
	private void cancelOption(ActionEvent event) throws IOException, SQLException{ //CANCEL
		if (Values.cancelWarning()){
			Values.cancelForm();
			Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
			Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			
			mainWindow.setScene(introScene);
			mainWindow.show();
		}
		
		
	}
	
	@FXML
	private void goToIntro(ActionEvent event) throws IOException{
		Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
		Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
		
		
		mainWindow.setScene(introScene);
		mainWindow.show();
		
	}
	
	
	private boolean checkChid(String chid){
		if (chid.equals("")){ //check whether CHID is filled
			Alert chidAlert = new Alert(Alert.AlertType.INFORMATION);
			chidAlert.setTitle("Empty CHID");
			chidAlert.setContentText("CHID is required.");
			chidAlert.setHeaderText(null);
			chidAlert.show();
			return false;
		}
		for (int i=0; i<chid.length(); i++){ //check for white spaces
			if (chid.charAt(i) == ' '){
				Alert chidAlert = new Alert(Alert.AlertType.INFORMATION);
				chidAlert.setTitle("Invalid CHID");
				chidAlert.setContentText("Whitespaces are not allowed in the CHID field");
				chidAlert.setHeaderText(null);
				chidAlert.show();
				return false;
			}
		}
		if (chid.contains(".fds")){ //check for .fds extension
			Alert extAlert = new Alert(Alert.AlertType.INFORMATION);
			extAlert.setTitle("Invalid CHID");
			extAlert.setContentText("The extension .fds does not need to be specified.");
			extAlert.setHeaderText(null);
			extAlert.show();
			return false;
		}
		//Values.allStrings[0][0] = chidText.getText();
		return true;
	}
	

	
	protected void showInfo() throws SQLException{ //String chid, String title
		String sql = "SELECT * FROM head";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()){
			chidText.setText(rs.getString(1));
			titleText.setText(rs.getString(2));
		}
	}
	


}
