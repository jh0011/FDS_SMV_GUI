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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MoveController implements Initializable{
	//move
	@FXML TextField idText; //string
    @FXML TextField xText; //float (+)
    @FXML TextField yText; //float (+)
    @FXML TextField zText; //float (+)
    @FXML TextField angleText; //float (0 - 360)
    @FXML TextField axisText; //float (3) (+)
    
    boolean checkFloatPos;
    boolean checkAngle;
    boolean checkAxis;
    
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
    private void goToHvac(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	if(checkFloatPos && checkAngle && checkAxis) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Hvac.fxml"));
    		Parent root = loader.load();
    		
    		HvacController hvacCont = loader.getController(); //Get the next page's controller
    		hvacCont.showInfo(); //Set the values of the page 
    		Scene hvacScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(hvacScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to HVAC page");
    	}
    }
    
    private void doChecking() {
    	doCheckingMove();
    }
    
    private void doCheckingMove() {
    	checkFloatPos = true;
        checkAngle = true;
        checkAxis = true;
        if(!xText.getText().equals("")) {
        	checkFloatPos = checkFloatPos && checkFloatPosValues(xText);
        }
        if(!yText.getText().equals("")) {
        	checkFloatPos = checkFloatPos && checkFloatPosValues(yText);
        }
        if(!zText.getText().equals("")) {
        	checkFloatPos = checkFloatPos && checkFloatPosValues(zText);
        }
        if(!angleText.getText().equals("")) {
        	checkAngle = checkAngle && checkAngleValues(angleText);
        }
        if(!axisText.getText().equals("")) {
        	checkAxis = checkAxis && checkAxisFormat(axisText);
        }
    }
    
    private void doCheckingProf() {
    	
    }
    
    private boolean checkFloatPosValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
				moveAlert.setTitle("Invalid value");
				moveAlert.setContentText("X0, Y0 and Z0 should be positive values. Please check again.");
				moveAlert.setHeaderText(null);
				moveAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Invalid value");
			moveAlert.setContentText("X0, Y0 and Z0 should be numerical values. Please check again.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
    }
    
    private boolean checkAngleValues(TextField tempField) { //check if float is between 0 and 360
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal < 0 || floatVal > 360){ //if it is not between 0 and 360
				Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
				multAlert.setTitle("Invalid value");
				multAlert.setContentText("Angle should be a positive value, between 0 and 360. Please check again.");
				multAlert.setHeaderText(null);
				multAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Invalid value");
			moveAlert.setContentText("Angle should be a numerical value. Please check again.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
    }
    
    private boolean checkAxisFormat(TextField tempField) { //check if 3 positive float
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect Axis format");
			moveAlert.setContentText("There should not be any whitespaces.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
		
		String[] axisValues = tempField.getText().split(",");
		String concatAxis = "";
		if (axisValues.length != 3){ //check if gvec is the correct length
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect Axis format");
			moveAlert.setContentText("There should be 3 real values, comma-separated.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float axisFloat = Float.valueOf(axisValues[i]);
				if (axisFloat < 0){ //check if axis is negative or zero
					Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
					moveAlert.setTitle("Incorrect Axis format");
					moveAlert.setContentText("The Axis values should be positive real values.");
					moveAlert.setHeaderText(null);
					moveAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the xyz string
					concatAxis = concatAxis + Float.toString(axisFloat) + ",";
				}
				else{
					concatAxis = concatAxis + Float.toString(axisFloat);
				}
			}
			tempField.setText(concatAxis);
			return true;
		}
		catch(Exception e){ //check if xyz is a number
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect Axis format");
			moveAlert.setContentText("There should be 3 real values.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			e.printStackTrace();
			return false;
		}
    }

    private void storeValues() throws SQLException { //store values into the database
    	storeValuesMove();
    }
    
	private void storeValuesMove() throws SQLException { //store MOVE values into the database
		String sqlMove = "INSERT INTO move VALUES ('" + idText.getText() + "', '" + xText.getText() + "', '" + yText.getText() + "', '" + zText.getText() + 
    			"', '" + angleText.getText() + "', '" + axisText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMove);
    }
	private void storeValuesProf() { //to show the info when the page is loaded
		
	}
	
	protected void showInfo() throws SQLException { //to show the info when the page is loaded
		showInfoMove();
	}
	
	protected void showInfoMove() throws SQLException { //to show the info when the page is loaded
		String sqlMove = "SELECT * FROM move";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlMove);
		while (rs.next()) {
			idText.setText(rs.getString(1));
			xText.setText(rs.getString(2));
			yText.setText(rs.getString(3));
			zText.setText(rs.getString(4));
			angleText.setText(rs.getString(5));
			axisText.setText(rs.getString(6));
		}
	}
}
