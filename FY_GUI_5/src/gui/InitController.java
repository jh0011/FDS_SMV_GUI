package gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.xml.datatype.Duration;

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
import javafx.scene.control.Tooltip;
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
	
	@FXML TextField ijkText; //integer array
	@FXML TextField xbMeshText; //float array
	
	
	//button
	@FXML Button cancelBtn;
	@FXML Button initBackBtn;
	@FXML Button initNextBtn;
	@FXML Button addInitBtn;
	@FXML Button addMeshBtn;
	@FXML Button printBtn;
	
	boolean xbFormat = true;
	boolean checkFloat = true;
	boolean checkInteger = true;
	boolean realArray = true;
	boolean checkIJK = true;
	boolean checkMeshXBformat = true;
	
	static int mainInitId = 1;
	static int mainMeshId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Tooltip initTooltip = new Tooltip("Click to add another INIT field.");
		addInitBtn.setTooltip(initTooltip);
		Tooltip meshTooltip = new Tooltip("Click to add another MESH field.");
		addMeshBtn.setTooltip(meshTooltip);
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
	private void goToPart(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		if (xbFormat && checkFloat && checkInteger && realArray && checkIJK && checkMeshXBformat){
			//store values
			storeValues();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Part.fxml"));
			Parent root = loader.load();
			
			PartController partCont = loader.getController(); //Get the next page's controller
			partCont.showInfo(); //Set the values of the page 
			Scene partScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(partScene);
			mainWindow.show();
		}
		else{
//			System.out.println("XB FORMAT: "+ xbFormat);
//			System.out.println("check float: "+ checkFloat);
//			System.out.println("check integer: "+ checkInteger);
//			System.out.println("real array: "+ realArray);
//			System.out.println("ijk format: "+ checkIJK);
			System.out.println("Unable to proceed to the PART page");
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
	private void newInitLine(ActionEvent event) throws IOException, SQLException{
		mainInitId++;
		String mainInitIdString = Integer.toString(mainInitId);
		String sqlInit = "INSERT INTO init(mainID, idText, partIdText, specIdText, npartText, "
				+ "npartCellText, massTimeText, massVolText, massFracText, xbText) "
				+ "VALUES ('" + mainInitIdString + "', '', '', '', '', '', '', '', '', '')";
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
	
	@FXML
	private void addMeshLine(ActionEvent event) throws IOException, SQLException{
		mainMeshId++;
		String mainMeshIdString = Integer.toString(mainMeshId);
		String sqlMesh = "INSERT INTO mesh (mainID, ijkText, xbText) VALUES ('"
				+ mainMeshIdString + "', '', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlMesh);
		
		String sqlShowMesh = "SELECT * FROM init";
		ResultSet rs = statement.executeQuery(sqlShowMesh);
		while (rs.next()){
			ijkText.setText(rs.getString(2));
			xbMeshText.setText(rs.getString(3));
		}
	}
	
	
	
	private void doChecking(){
		xbFormat = true;
		checkFloat = true;
		checkInteger = true;
		realArray = true;
		checkIJK = true;
		checkMeshXBformat = true;
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
			xbFormat = xbFormat && checkXbFormat(xbText);
		}
		
		checkMeshXBformat = checkMeshXB(xbMeshText);
		checkIJK = checkIJKformat(ijkText);
	}
	
	private boolean checkXbFormat(TextField valueTF){
		if (valueTF.getText().contains(" ")){ //check if there are any white spaces
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect XB format");
			initAlert.setContentText("There should not be any whitespaces.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		String[] xbValues = valueTF.getText().split(",");
		float[] xbFloatValues = new float[6];
		String concatXB = "";
		
		if (xbValues.length != 6){
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect XB format");
			initAlert.setContentText("There should be 6 real values.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				Float.valueOf(xbValues[i]);
			}
			catch(Exception e){//check if each value is real
			
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Incorrect XB format");
				initAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
			
			xbFloatValues[i] = Float.valueOf(xbValues[i]); //convert to float
			if (i==5){
				concatXB = concatXB + Float.toString(xbFloatValues[i]);
			}
			else{
				concatXB = concatXB + Float.toString(xbFloatValues[i]) + ","; //convert to string
			}
		}
		valueTF.setText(concatXB);
		return true;
		
	}
	
	private boolean checkIntValues(TextField valueTF){
		try{
			String value = valueTF.getText();
			int valueInt = Integer.parseInt(value);
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
	
	private boolean checkMeshXB(TextField valueTF){
		if (valueTF.getText().equals("")){
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Empty MESH XB values");
			meshAlert.setContentText("MESH XB is a required value.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			return false;
		}
		return checkXbFormat(valueTF);
		
	}
	
	private boolean checkIJKformat(TextField valueTF){ 
		if (valueTF.getText().equals("")){ //check if ijk is empty
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Empty MESH IJK values");
			meshAlert.setContentText("MESH IJK is a required value.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			return false;
		}
		if (valueTF.getText().contains(" ")){ //check if there are any white spaces
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect IJK format");
			initAlert.setContentText("There should not be any whitespaces.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		
		String[] ijkValues = valueTF.getText().split(",");
		String concatIJK = "";
		if (ijkValues.length != 3){ //check if ijk is the correct length
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Incorrect IJK format");
			meshAlert.setContentText("There should be 3 integer values, comma-separated.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				int ijkInt = Integer.parseInt(ijkValues[i]);
				if (ijkInt <= 0){ //check if ijk is negative or zero
					Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
					meshAlert.setTitle("Incorrect IJK format");
					meshAlert.setContentText("The IJK values should be more than zero.");
					meshAlert.setHeaderText(null);
					meshAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the ijk string
					concatIJK = concatIJK + String.valueOf(ijkInt) + ",";
				}
				else{
					concatIJK = concatIJK + String.valueOf(ijkInt);
				}
			}
			valueTF.setText(concatIJK);
			return true;
		}
		catch(Exception e){ //check if ijk is an integer
			Alert meshAlert = new Alert(Alert.AlertType.INFORMATION);
			meshAlert.setTitle("Incorrect IJK format");
			meshAlert.setContentText("There should be 3 integer values.");
			meshAlert.setHeaderText(null);
			meshAlert.show();
			e.printStackTrace();
			return false;
		}
	}
	
	private void storeValues() throws SQLException{
		String mainInitIdString = Integer.toString(mainInitId);
		String sqlInit = "INSERT INTO init VALUES('" + mainInitIdString + "', '" + idText.getText() +
				"', '" + partIdText.getText() + "', '" + specIdText.getText() + "', '" +
				npartText.getText() + "', '" + npartCellText.getText() + "', '" +
				massTimeText.getText() + "', '" + massVolText.getText() + "', '" + 
				massFracText.getText() + "', '" + xbText.getText() + "');";
		String mainMeshIdString = Integer.toString(mainMeshId);
		String sqlMesh = "INSERT INTO mesh VALUES('" + mainMeshIdString + "', '" + ijkText.getText() +
				"', '" + xbMeshText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlInit);
		statement.executeUpdate(sqlMesh);
	}
	
	@FXML 
	private void printFile(ActionEvent event) throws IOException{ //SAMPLE TESTING
		Values.printFile();
	}

	//To take values from database and display them for the init page
	public void showInfo() throws SQLException {
		String sqlInit = "SELECT * FROM init;";
		String sqlMesh = "SELECT * FROM mesh;";
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
		
		ResultSet rs2 = statement.executeQuery(sqlMesh);
		while (rs2.next()){
			ijkText.setText(rs2.getString(2));
			xbMeshText.setText(rs2.getString(3));
		}
	}
	
	

}
