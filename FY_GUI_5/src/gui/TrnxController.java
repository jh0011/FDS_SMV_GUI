package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TrnxController implements Initializable{
	//trnx
	@FXML TextField idText; //string
    @FXML TextField meshText; //integer (+)
    @FXML TextField ccText; //float (+)
    @FXML TextField pcText; //float (+)
    
    @FXML Button addTrnxBtn;
    
    boolean checkPosInt;
    boolean checkPosFloat;
    
    static int mainTrnxId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
    private void cancelOption(ActionEvent event) throws IOException, SQLException { //CANCEL
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
    private void goToMove(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	if(checkPosInt && checkPosFloat) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Move.fxml"));
    		Parent root = loader.load();
    		
    		MoveController moveCont = loader.getController(); //Get the next page's controller
    		moveCont.showInfo(); //Set the values of the page 
    		Scene moveScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(moveScene);
    		mainWindow.show();
    	}
    	
    }
    
    @FXML
    private void newTrnxLine(ActionEvent event) throws SQLException { //ADD NEW TRNX LINE
    	doCheckingTrnx();
    	
    	if (checkPosInt && checkPosFloat) {
    		//store values
    		storeValuesTrnx();
    		
    		mainTrnxId++;
    		String mainTrnxIdString = Integer.toString(mainTrnxId);
    		String sqlTrnx = "INSERT INTO trnx (mainID, ID, MESH_NUMBER, CC, PC) VALUES ('" + mainTrnxIdString + "', '', '', '', '');";
    		ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(sqlTrnx);
    		
    		showInfoTrnx();
    	}
    	else {
    		System.out.println("Unable to add new TRNX line");
    	}
    }
    
    private void doChecking() {
    	doCheckingTrnx();
    }
    
    private void doCheckingTrnx() {
    	checkPosInt = true;
    	checkPosFloat = true;
    	if(!meshText.getText().equals("")) {
    		checkPosInt = checkPosInt && checkPosIntValues(meshText);
    	}
    	if(!ccText.getText().equals("")) {
    		checkPosFloat = checkPosFloat && checkPosFloatValues(ccText);
    	}
    	if(!pcText.getText().equals("")) {
    		checkPosFloat = checkPosFloat && checkPosFloatValues(pcText);
    	}
    }
    
    private boolean checkPosIntValues(TextField tempField) { //check if integer is positive
    	try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
				trnxAlert.setTitle("Invalid integer value");
				trnxAlert.setContentText("Mesh_number should be a positive integer. Please check again.");
				trnxAlert.setHeaderText(null);
				trnxAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
			trnxAlert.setTitle("Invalid integer value");
			trnxAlert.setContentText("Mesh_number should be an integer. Please check again.");
			trnxAlert.setHeaderText(null);
			trnxAlert.show();
			return false;
		}
    }
    
    private boolean checkPosFloatValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
				trnxAlert.setTitle("Invalid value");
				trnxAlert.setContentText("CC and PC should be positive values. Please check again.");
				trnxAlert.setHeaderText(null);
				trnxAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
			trnxAlert.setTitle("Invalid value");
			trnxAlert.setContentText("CC and PC should be numerical values. Please check again.");
			trnxAlert.setHeaderText(null);
			trnxAlert.show();
			return false;
		}
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesTrnx();
    }
    
    private void storeValuesTrnx() throws SQLException { //store TRNX values into the database
    	String mainTrnxIdString = Integer.toString(mainTrnxId);
    	String sqlTrnx = "INSERT INTO trnx VALUES ('" + mainTrnxIdString + "', '" + idText.getText() + "', '" + meshText.getText() + "', '" + ccText.getText() + "', '" + pcText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlTrnx);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoTrnx();
    }
    
    protected void showInfoTrnx() throws SQLException { //to show the info when the page is loaded
    	String sqlTrx = "SELECT * FROM trnx";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlTrx);
		while (rs.next()) {
			idText.setText(rs.getString(2));
			meshText.setText(rs.getString(3));
			ccText.setText(rs.getString(4));
			pcText.setText(rs.getString(5));
		}
    }

}