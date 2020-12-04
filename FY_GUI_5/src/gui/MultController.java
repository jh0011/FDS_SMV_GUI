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
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class MultController implements Initializable{
	//mult
	@FXML TextField idText; //string
	@FXML TextField iUpperText; //int (+)
    @FXML TextField jUpperText; //int (+)
    @FXML TextField kUpperText; //int (+)
    @FXML TextField dxText; //float (+)
    @FXML TextField dyText; //float (+)
    @FXML TextField dzText; //float (+)
    
    //wind
    @FXML TextField zText; //float (+)
    @FXML TextField directionText; //float (0 - 360)
    @FXML TextField lText; //float (>= -500)
    @FXML TextField speedText; //float (+)
    
    @FXML Button addMultBtn;
    
    boolean checkIntPosMult;
    boolean checkFloatPosMult;
    
    boolean checkFloatPosWind;
    boolean checkDirection;
    boolean checkL;
    
    static int mainMultId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip multooltip = new Tooltip("Click to add another MULT field.");
		addMultBtn.setTooltip(multooltip);
	}

	@FXML
	public void cancelOption(ActionEvent event) throws IOException, SQLException { //CANCEL
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
    public  void goToDump(ActionEvent event) throws SQLException, IOException { //PREVIOUS SCENE
    	doChecking();
    	
    	if (checkIntPosMult && checkFloatPosMult && checkFloatPosWind && checkDirection && checkL) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dump.fxml"));
    		Parent root = loader.load();
    		
    		DumpController dumpCont = loader.getController(); //Get the next page's controller
    		dumpCont.showInfo(); //Set the values of the page 
    		Scene dumpScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(dumpScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to DUMP page");
    	}
    }
    
    @FXML
    public  void goToPres(ActionEvent event) throws SQLException, IOException { //NEXT SCENE
    	doChecking();
    	
    	if (checkIntPosMult && checkFloatPosMult && checkFloatPosWind && checkDirection && checkL) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Pres.fxml"));
    		Parent root = loader.load();
    		
    		PresController presCont = loader.getController(); //Get the next page's controller
    		presCont.showInfo(); //Set the values of the page 
    		Scene presScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(presScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to proceed to PRES page");
    	}
    }
    
    @FXML
    public void newMultLine(ActionEvent event) throws SQLException { //ADD NEW MULT LINE
    	doCheckingMult();
    	
    	if (checkIntPosMult && checkFloatPosMult) {
    		mainMultId++;
        	String mainMultIdString = Integer.toString(mainMultId);
        	String sqlMult = "INSERT INTO mult (mainID, ID, I_UPPER, J_UPPER, K_UPPER, DX, DY, DZ) VALUES ('" + mainMultIdString + "', '', '', '', '', '', '', '');";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(sqlMult);
    		
    		showInfoMult();
    	}
    	else {
    		System.out.println("Unable to add new MULT line");
    	}
    }
    
    public void doChecking() {
    	doCheckingMult();
    	doCheckingWind();
    }
    
    public void doCheckingMult() {
    	checkIntPosMult = true;
    	checkFloatPosMult = true;
    	if(!iUpperText.getText().equals("")) {
    		checkIntPosMult = checkIntPosMult && checkPosIntValues(iUpperText);
    	}
    	if(!jUpperText.getText().equals("")) {
    		checkIntPosMult = checkIntPosMult && checkPosIntValues(jUpperText);
    	}
    	if(!kUpperText.getText().equals("")) {
    		checkIntPosMult = checkIntPosMult && checkPosIntValues(kUpperText);
    	}
    	if(!dxText.getText().equals("")) {
    		checkFloatPosMult = checkFloatPosMult && checkPosFloatValues(dxText);
    	}
    	if(!dyText.getText().equals("")) {
    		checkFloatPosMult = checkFloatPosMult && checkPosFloatValues(dyText);
    	}
    	if(!dzText.getText().equals("")) {
    		checkFloatPosMult = checkFloatPosMult && checkPosFloatValues(dzText);
    	}
    }
    
    public void doCheckingWind() {
    	checkFloatPosWind = true;
    	checkDirection = true;
    	checkL = true;
    	if(!zText.getText().equals("")) {
    		checkFloatPosWind = checkFloatPosWind && checkPosFloatValues(zText);
    	}
    	if(!speedText.getText().equals("")) {
    		checkFloatPosWind = checkFloatPosWind && checkPosFloatValues(speedText);
    	}
    	if(!directionText.getText().equals("")) {
    		checkDirection = checkDirection && checkDirectionValues(directionText);
    	}
    	if(!lText.getText().equals("")) {
    		checkL = checkL && checkLValues(lText);
    	}
    }
    
    public boolean checkPosIntValues(TextField tempField) { //check if integer is positive value
    	try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
				multAlert.setTitle("Invalid integer value");
				multAlert.setContentText("I_upper, J_upper and K_upper should be positive integers. Please check again.");
				multAlert.setHeaderText(null);
				multAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
			multAlert.setTitle("Invalid integer value");
			multAlert.setContentText("I_upper, J_upper and K_upper should be integers. Please check again.");
			multAlert.setHeaderText(null);
			multAlert.show();
			return false;
		}
    }
    
    public boolean checkPosFloatValues(TextField tempField) { //check if float is positive value
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
				multAlert.setTitle("Invalid value");
				multAlert.setContentText("DX, DY, DZ, Speed and Z_0 should be positive values. Please check again.");
				multAlert.setHeaderText(null);
				multAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
			multAlert.setTitle("Invalid value");
			multAlert.setContentText("DX, DY, DZ, Speed and Z_0 should be numerical values. Please check again.");
			multAlert.setHeaderText(null);
			multAlert.show();
			return false;
		}
    }
    
    public boolean checkDirectionValues(TextField tempField) { //check if float is between 0 and 360
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal < 0 || floatVal > 360){ //if it is not between 0 and 360
				Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
				multAlert.setTitle("Invalid value");
				multAlert.setContentText("Direction should be a positive value, between 0 and 360. Please check again.");
				multAlert.setHeaderText(null);
				multAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
			multAlert.setTitle("Invalid value");
			multAlert.setContentText("Direction should be a numerical value. Please check again.");
			multAlert.setHeaderText(null);
			multAlert.show();
			return false;
		}
    }
    
    public boolean checkLValues(TextField tempField) { //check if float is >= -500)
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal < -500){ //if it is not below -500
				Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
				multAlert.setTitle("Invalid value");
				multAlert.setContentText("L should be more than -500. Please check again.");
				multAlert.setHeaderText(null);
				multAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
			multAlert.setTitle("Invalid value");
			multAlert.setContentText("L should be a numerical value. Please check again.");
			multAlert.setHeaderText(null);
			multAlert.show();
			return false;
		}
    }
    
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesMult();
    	storeValuesWind();
    }
    
    public void storeValuesMult() throws SQLException { //store MULT values into the database
    	String mainMultIdString = Integer.toString(mainMultId);
    	String sqlMult = "INSERT INTO mult VALUES ('" + mainMultIdString + "', '" + idText.getText() + "', '" + iUpperText.getText() + "', '" + jUpperText.getText() + "', '" +
    			kUpperText.getText() + "', '" + dxText.getText() + "', '" + dyText.getText() + "', '" + dzText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMult);
    }
    
    public void storeValuesWind() throws SQLException { //store WIND values into the database
    	String sqlWind = "INSERT INTO wind VALUES ('" + zText.getText() + "', '" + directionText.getText() + "', '" + lText.getText() + "', '" + speedText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlWind);
    }
    
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoMult();
    	showInfoWind();
    }
    
    public void showInfoMult() throws SQLException { //to show the info when the page is loaded
    	String sqlMult = "SELECT * FROM mult;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlMult);
		while (rs.next()) {
			idText.setText(rs.getString(2));
			iUpperText.setText(rs.getString(3));
			jUpperText.setText(rs.getString(4));
			kUpperText.setText(rs.getString(5));
			dxText.setText(rs.getString(6));
			dyText.setText(rs.getString(7));
			dzText.setText(rs.getString(8));
		}
    }
    
    public void showInfoWind() throws SQLException { //to show the info when the page is loaded
    	String sqlWind = "SELECT * FROM wind;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlWind);
		while (rs.next()) {
			zText.setText(rs.getString(1));
			directionText.setText(rs.getString(2));
			lText.setText(rs.getString(3));
			speedText.setText(rs.getString(4));
		}
    }
}
