package gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InitController implements Initializable{
	
	@FXML TextField idText; //string
	@FXML TextField partIdText; //string
	@FXML TextField specIdText; //string
	@FXML TextField npartText; //int
	@FXML TextField npartCellText; //int
	@FXML TextField massTimeText; //float
	@FXML TextField massVolText; //float
	@FXML TextField massFracText; //float
	@FXML TextField xbText; //array
	
	
	//button
	@FXML Button cancelBtn;
	@FXML Button initBackBtn;
	@FXML Button initNextBtn;
	@FXML Button printBtn;
	
	boolean xbFormat = true;
	boolean checkFloat = true;
	boolean checkInteger = true;
	boolean realArray = true;
	
	static int mainId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private void goToTime(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Time.fxml"));
		Parent root = loader.load();
		
		TimeController timeCont = loader.getController(); //Get the next page's controller
		timeCont.showInfo(); //Set the values of the page 
		Scene timeScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(timeScene);
		mainWindow.show();
	}
	
	@FXML
	private void goToInit(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		if (xbFormat && checkFloat && checkInteger && realArray){
			//store values
			storeValues();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Init2.fxml"));
			Parent root = loader.load();
			
			Init2Controller initCont = loader.getController(); //Get the next page's controller
			//initCont.showInfo(); //Set the values of the page 
			Scene initScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(initScene);
			mainWindow.show();
		}
		else{
			System.out.println("XB FORMAT: "+ xbFormat);
			System.out.println("check float: "+ checkFloat);
			System.out.println("check integer: "+ checkInteger);
			System.out.println("real array: "+ realArray);
		}
		
	}
	@FXML 
	private void cancelOption(ActionEvent event) throws IOException{ //CANCEL
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
	private void newInitLine(ActionEvent event) throws IOException, SQLException{
		mainId++;
		String mainIdString = Integer.toString(mainId);
		String sqlInit = "INSERT INTO init(mainId, idText, partIdText, specIdText, npartText, "
				+ "npartCellText, massTimeText, massVolText, massFracText, xbText) "
				+ "VALUES (" + mainIdString + ", '', '', '', '', '', '', '', '', '')";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlInit);
		
		String sqlShowInit = "SELECT * FROM init";
		ResultSet rs = statement.executeQuery(sqlShowInit);
		while (rs.next()){
			idText.setText(rs.getString(2));
			partIdText.setText(rs.getString(3));
			specIdText.setText(rs.getString(4));
			npartText.setText(rs.getString(5));
			npartCellText.setText(rs.getString(6));
			massTimeText.setText(rs.getString(7));
			massVolText.setText(rs.getString(8));
			massFracText.setText(rs.getString(9));
			xbText.setText(rs.getString(10));
		}
	}
	
	private void doChecking(){
		xbFormat = true;
		checkFloat = true;
		checkInteger = true;
		realArray = true;
		if (!massTimeText.getText().equals("")){
			checkFloat = checkFloat && checkFloatValues(massTimeText);
		}
		if (!massVolText.getText().equals("")){
			checkFloat = checkFloat && checkFloatValues(massVolText);
		}
		if (!massFracText.getText().equals("")){
			checkFloat = checkFloat && checkFloatValues(massFracText);
		}
		if (!npartText.getText().equals("")){
			checkInteger = checkInteger && checkIntValues(npartText);
		}
		if (!npartCellText.getText().equals("")){
			checkInteger = checkInteger && checkIntValues(npartCellText);
		}
		if (!xbText.getText().equals("")){
			xbFormat = checkXbFormat(xbText);
		}
	}
	
	private boolean checkXbFormat(TextField valueTF){
		String[] xbValues = valueTF.getText().split(",");
		float[] xbFloatValues = new float[6];
		String concatXB = "";
		boolean checkXBfloat = false;
		TextField tmpTF = new TextField();
		
		if (xbValues.length != 6){
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect XB format");
			initAlert.setContentText("There should be 6 real values.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			tmpTF.setText(xbValues[i]);
			checkXBfloat = checkFloatValues(tmpTF);
			if (checkXBfloat == false){ //check if each value is real
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Incorrect XB format");
				initAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values delimited by comma. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
			
			xbFloatValues[i] = Float.valueOf(xbValues[i]); //convert to float
			if (i==5){
				concatXB = concatXB + Float.toString(xbFloatValues[i]);
			}
			else{
				concatXB = concatXB + Float.toString(xbFloatValues[i]) + ", "; //convert to string
			}
		}
		valueTF.setText(concatXB);
		return true;
		
	}
	
	private boolean checkIntValues(TextField valueTF){
		try{
			String value = valueTF.getText();
			int valueInt = Integer.parseInt(value);
			System.out.println("VALUE INT: " + valueInt);
			if (valueInt < 0){ //check if it is negative
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Invalid init value");
				initAlert.setContentText("The values should not have negative numbers. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
			else{
				valueTF.setText(value);
				return true;
			}
		}
		catch(Exception e){ //check if it is integer
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Invalid init value");
			initAlert.setContentText("The particles and particles per cell values should be an integer. Please check again.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
	}
	
	private boolean checkFloatValues (TextField valueTF){
		try{
			String value = valueTF.getText();
			float valueFloat = Float.valueOf(value);
			if (valueFloat < 0){ //check if it is integer
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Invalid init value");
				initAlert.setContentText("The values should not have negative numbers. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
			else{
				valueTF.setText(Float.toString(valueFloat));
				return true;
			}
		}
		catch(Exception e){ //check if it is a float
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Invalid init value");
			initAlert.setContentText("The mass per time, mass per volume and XB values should be numerical. Please check again.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
	}
	
	private void storeValues() throws SQLException{
		String sqlInit = "INSERT INTO init VALUES('" + mainId + "', '" + idText.getText() +
				"', '" + partIdText.getText() + "', '" + specIdText.getText() + "', '" +
				npartText.getText() + "', '" + npartCellText.getText() + "', '" +
				massTimeText.getText() + "', '" + massVolText.getText() + "', '" + 
				massFracText.getText() + "', '" + xbText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlInit);
	}
	
	@FXML 
	private void printFile(ActionEvent event) throws IOException{ //SAMPLE TESTING
		Values.printFile();
	}

	//To take values from database and display them for the init page
	public void showInfo() throws SQLException {
		String sqlInit = "SELECT * FROM init;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlInit);
		while (rs.next()){
			idText.setText(rs.getString(2));
			partIdText.setText(rs.getString(3));
			specIdText.setText(rs.getString(4));
			npartText.setText(rs.getString(5));
			npartCellText.setText(rs.getString(6));
			massTimeText.setText(rs.getString(7));
			massVolText.setText(rs.getString(8));
			massFracText.setText(rs.getString(9));
			xbText.setText(rs.getString(10));
		}
	}
	
	

}
