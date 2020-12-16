package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import connectivity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class PartController implements Initializable{
	/**
	 * Controller class for Part.fxml
	 * @author 
	 */
	
	//part
	@FXML TextField surfIdText; //string
	@FXML TextField specIdText; //string
	@FXML TextField propIdText; //string
	@FXML TextField qtyPartText; //string
	@FXML ComboBox staticCombo; //boolean
	@FXML ComboBox masslessCombo; //boolean
	@FXML TextField sampleText; //integer
	@FXML TextField diameterText; //float
	@FXML TextField idText; //string
	
	//bndf
	@FXML TextField qtyBndfText; //string
	
	
	@FXML Button addPartBtn;
	@FXML Button addBndfBtn;
	
	boolean intCheck;
	boolean floatCheck;
	
	static String staticSelection = "";
	static String masslessSelection = "";
	
	static int mainPartId = 1;
	static int mainBndfId = 1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		Tooltip partTooltip = new Tooltip("Click to add another PART field.");
		addPartBtn.setTooltip(partTooltip);
		Tooltip bndfTooltip = new Tooltip("Click to add another BNDF field.");
		addBndfBtn.setTooltip(bndfTooltip);
		
		ObservableList<String> staticList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		staticCombo.setItems(staticList);
		
		ObservableList<String> masslessList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		masslessCombo.setItems(masslessList);
		
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML 
	public void cancelOption(ActionEvent event) throws IOException, SQLException{ //CANCEL
		if (Values.cancelWarning()){
			Values.cancelForm();
			Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
			Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			
			mainWindow.setScene(introScene);
			mainWindow.show();
		}
	}
	
	/**
	 * Go to the previous page (INIT) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToInit(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		try {
			if (intCheck && floatCheck){
				//store the values
				storeValues();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Init.fxml"));
				Parent root = loader.load();
				
				InitController initCont = loader.getController(); //Get the next page's controller
				initCont.showInfo(); //Set the values of the page 
				Scene initScene = new Scene(root);
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
				mainWindow.setScene(initScene);
				mainWindow.show();
			}
		}catch(Exception e) {
			Values.showError();
		}
	}
	
	/**
	 * Go to the next page (PROP) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToProp(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		try {
			if (intCheck && floatCheck){
				//store the values
				storeValues();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Prop.fxml"));
				Parent root = loader.load();
				
				PropController propCont = loader.getController(); //Get the next page's controller
				propCont.showInfo(); //Set the values of the page 
				Scene propScene = new Scene(root);
				Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
				mainWindow.setScene(propScene);
				mainWindow.show();
			}
		}catch(Exception e) {
			Values.showError();
		}
		
	}
	
	/**
	 * Add a new line for PART namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void newPartLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW PART LINE
		doCheckingPart();
		
		if (intCheck && floatCheck){
			//store the values
			storeValuesPart();
			
			//confirmation message for success
			Values.printConfirmationMessage("PART", true);
			
			mainPartId++;
			String mainPartIdString = Integer.toString(mainPartId);
			String sqlPart = "INSERT INTO part (mainID, SURF_ID, SPEC_ID, PROP_ID, QUANTITIES, STATIC" + 
					", MASSLESS, SAMPLING_FACTOR, DIAMETER, ID) VALUES ('" + mainPartIdString + "', '', '', '', '', '', '', '', '', '');";
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlPart);
			
			showInfoPart();
		}
		else {
			//confirmation message for failure
			Values.printConfirmationMessage("PART", false);
			System.out.println("Unable to add a new PART line");
		}
	}
	
	/**
	 * Add a new line for BNDF namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void newBndfLine(ActionEvent event) throws IOException, SQLException{ //ADD NEW BNDF LINE
		storeValuesBndf();
		
		//confirmation message for success
		Values.printConfirmationMessage("BNDF", true);
		
		mainBndfId++;
		String mainBndfIdString = Integer.toString(mainBndfId);
		String sqlBndf = "INSERT INTO bndf (mainID, QUANTITY) VALUES ('" + mainBndfIdString + "', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlBndf);
		
		showInfoBndf();
	}
	
	@FXML
	public void staticSelect(ActionEvent event) {
		staticSelection = staticCombo.getSelectionModel().getSelectedItem().toString();
		staticCombo.setValue(staticSelection);
    }
	
	@FXML
	public void masslessSelect(ActionEvent event) {
		masslessSelection = masslessCombo.getSelectionModel().getSelectedItem().toString();
		masslessCombo.setValue(masslessSelection);
    }
	
	/**
	 * Call the checking methods for the different namelists
	 */
	public void doChecking(){
		doCheckingPart();
	}
	
	public void doCheckingPart() {
		intCheck = true;
		floatCheck = true;
		
		if (!sampleText.getText().equals("")){
			intCheck = intCheck && checkIntegerValues(sampleText);
		}
		if (!diameterText.getText().equals("")){
			floatCheck = floatCheck && checkFloatValues(diameterText);
		}
	}
	
//	public boolean checkBooleanValues(TextField tempField){
//		if (tempField.getText().equalsIgnoreCase("true")){
//			tempField.setText("TRUE");
//			return true;
//		}
//		else if (tempField.getText().equalsIgnoreCase("false")){
//			tempField.setText("FALSE");
//			return true;
//		}
//		else{
//			Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
//			partAlert.setTitle("Invalid logical value");
//			partAlert.setContentText("Static and Massless should be logical values. Please check again.");
//			partAlert.setHeaderText(null);
//			partAlert.show();
//			return false;
//		}
//	}
	
	/**
	 * Check if the integer is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkIntegerValues(TextField tempField){
		try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
				partAlert.setTitle("Invalid integer value");
				partAlert.setContentText("Sampling factor should be a positive integer. Please check again.");
				partAlert.setHeaderText(null);
				partAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
			partAlert.setTitle("Invalid integer value");
			partAlert.setContentText("Sampling factor should be an integer. Please check again.");
			partAlert.setHeaderText(null);
			partAlert.show();
			return false;
		}
	}
	
	/**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public boolean checkFloatValues(TextField tempField){
		try{
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive value
				Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
				partAlert.setTitle("Invalid value");
				partAlert.setContentText("Diameter should be a positive value. Please check again.");
				partAlert.setHeaderText(null);
				partAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch(Exception e){ //if it is not a float
			Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
			partAlert.setTitle("Invalid value");
			partAlert.setContentText("Diameter should be a numerical value. Please check again.");
			partAlert.setHeaderText(null);
			partAlert.show();
			return false;
		}
		
	}
	
	/**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
	public void storeValues() throws SQLException{ //store values into the database
		storeValuesPart();
		storeValuesBndf();
	}
	
	public void storeValuesPart() throws SQLException{ //store PART values into the database
		String mainPartIdString = Integer.toString(mainPartId);
		String sqlPart = "INSERT INTO part VALUES('" + mainPartIdString + "', '" + surfIdText.getText() + "', '" 
				+ specIdText.getText() + "', '" + propIdText.getText() + "', '" + qtyPartText.getText() + "', '"
				+ staticSelection + "', '" + masslessSelection + "', '" + sampleText.getText() +
				"', '" + diameterText.getText() + "', '" + idText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlPart);
	}
	
	public void storeValuesBndf() throws SQLException{ //store BNDF values into the database
		String mainBndfIdString = Integer.toString(mainBndfId);
		String sqlBndf = "INSERT INTO bndf VALUES('" + mainBndfIdString + "', '" + qtyBndfText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlBndf);
	}
	
	/**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
	public void showInfo() throws SQLException{ //to show the info when the page is loaded
		showInfoPart();
		showInfoBndf();
	}
	
	public void showInfoPart() throws SQLException{ //to show the info when the page is loaded
		String sqlPart = "SELECT * FROM part;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlPart);
		while (rs.next()){
			surfIdText.setText(rs.getString(2));
			specIdText.setText(rs.getString(3));
			propIdText.setText(rs.getString(4));
			qtyPartText.setText(rs.getString(5));
			staticSelection = rs.getString(6);
			staticCombo.setValue(staticSelection);
			masslessSelection = rs.getString(7);
			masslessCombo.setValue(masslessSelection);
			sampleText.setText(rs.getString(8));
			diameterText.setText(rs.getString(9));
			idText.setText(rs.getString(10));
		}
	}
	
	public void showInfoBndf() throws SQLException{ //to show the info when the page is loaded
		String sqlBndf = "SELECT * FROM bndf;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		
		ResultSet rs2 = statement.executeQuery(sqlBndf);
		while (rs2.next()){
			qtyBndfText.setText(rs2.getString(2));
		}
	}
}
