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
import javafx.stage.Stage;

public class DevcController implements Initializable{
	
	@FXML TextField devcIdText; //string
	@FXML TextField propIdText; //string
	@FXML TextField specIdText; //string
	@FXML TextField xyzText; //float (3)
	@FXML TextField quantityText; //string
	@FXML ComboBox iorCombo;
	@FXML TextField xbText; //float (6)
	
	@FXML Button addDevcBtn;
	
	boolean checkXb;
	boolean checkXyz;
	
	static String iorSelection = "";
	static int mainDevcId = 1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<String> iorList = FXCollections.observableArrayList("1", "-1", "2", "-2", "3", "-3");
		iorCombo.setItems(iorList);
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
	private void goToProp(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		if (checkXyz && checkXb) {
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
		
	}
	
	@FXML
    private void iorSelect(ActionEvent event) {
		iorSelection = iorCombo.getSelectionModel().getSelectedItem().toString();
		iorCombo.setValue(iorSelection);
    }
	
	private void doChecking() {
		checkXb = true;
		checkXyz = true;
	
		if(!xbText.getText().equals("")) {
			checkXb = checkXb && checkXbFormat(xbText);
		}
		if(!xyzText.getText().equals("")) {
			checkXyz = checkXyz && checkXyzFormat(xyzText);
		}
	}
	
	@FXML
	private void newDevcLine(ActionEvent event) throws IOException, SQLException{
		mainDevcId++;
		String mainDevcIdString = Integer.toString(mainDevcId);
		iorSelection = "Select one (optional)"; //set the combo box when a new DEVC line is added
		String sqlDevc = "INSERT INTO devc (mainID, ID, PROP_ID, SPEC_ID, XYZ, QUANTITY, IOR, XB) VALUES ('" + mainDevcIdString + "', '', '', '', '', '', '" + iorSelection + "', '');";
		//String sqlDevc = "INSERT INTO devc (mainID, ID, PROP_ID, SPEC_ID, XYZ, QUANTITY, IOR, XB) VALUES ('" + mainDevcIdString + "', '', '', '', '', '', '', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlDevc);
		
		showInfo();
	}
	
	private boolean checkXbFormat(TextField tempField) {
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XB format");
			devcAlert.setContentText("There should not be any whitespaces.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		String[] xbValues = tempField.getText().split(",");
		float[] xbFloatValues = new float[6];
		String concatXB = "";
		
		if (xbValues.length != 6){ //check if it is the correct length
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XB format");
			devcAlert.setContentText("There should be 6 real values.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				Float.valueOf(xbValues[i]);
			}
			catch(Exception e){//check if each value is real
			
				Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
				devcAlert.setTitle("Incorrect XB format");
				devcAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.");
				devcAlert.setHeaderText(null);
				devcAlert.show();
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
		tempField.setText(concatXB);
		return true;
	}
	
	private boolean checkXyzFormat(TextField tempField) {
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XYZ format");
			devcAlert.setContentText("There should not be any whitespaces.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		
		String[] xyzValues = tempField.getText().split(",");
		String concatXYZ = "";
		if (xyzValues.length != 3){ //check if ijk is the correct length
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XYZ format");
			devcAlert.setContentText("There should be 3 positive real values, comma-separated.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float xyzFloat = Float.valueOf(xyzValues[i]);
				if (xyzFloat < 0){ //check if xyz is negative or zero
					Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
					devcAlert.setTitle("Incorrect XYZ format");
					devcAlert.setContentText("The XYZ values should be positive real values.");
					devcAlert.setHeaderText(null);
					devcAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the xyz string
					concatXYZ = concatXYZ + Float.toString(xyzFloat) + ",";
				}
				else{
					concatXYZ = concatXYZ + Float.toString(xyzFloat);
				}
			}
			tempField.setText(concatXYZ);
			return true;
		}
		catch(Exception e){ //check if xyz is a number
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect xyz format");
			devcAlert.setContentText("There should be 3 positive real values.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			e.printStackTrace();
			return false;
		}
	}
	
	
	private void storeValues() throws SQLException { //store values into the database
		String mainDevcIdString = Integer.toString(mainDevcId);
		String sqlDevc = "INSERT INTO devc VALUES('" + mainDevcIdString + "', '" + devcIdText.getText() + "', '" + propIdText.getText() + "', '" +
				specIdText.getText() + "', '" + xyzText.getText() + "', '" + quantityText.getText() + "', '" + iorSelection + "', '" + xbText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlDevc);
	}
	
	protected void showInfo() throws SQLException { //to show the info when the page is loaded
		String sqlDevc = "SELECT * FROM devc;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlDevc);
		while (rs.next()) {
			devcIdText.setText(rs.getString(2));
			propIdText.setText(rs.getString(3));
			specIdText.setText(rs.getString(4));
			xyzText.setText(rs.getString(5));
			quantityText.setText(rs.getString(6));
			iorSelection = rs.getString(7);
			//System.out.println("IOR SELECTION: " + iorSelection);
			iorCombo.setValue(iorSelection);
			xbText.setText(rs.getString(8));
		}
	}

}
