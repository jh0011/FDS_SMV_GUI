package gui;

import java.util.ArrayList;
import java.util.Optional;

import connectivity.ConnectionClass;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Values {
	
	protected static String concatFiles = "";
	
	/**
	 * This function is called to delete the values from all the tables.
	 * This function is called when the desktop application first starts and when 
	 * users cancel the creation of the .fds file.
	 * @throws SQLException
	 */
	protected static void cancelForm() throws SQLException{
		
		//delete the values in the tables
		String sqlHead = "DELETE FROM head;";
		String sqlTime = "DELETE FROM time;";
		String sqlCatf = "DELETE FROM catf;";
		String sqlInit = "DELETE FROM init;";
		String sqlMesh = "DELETE FROM mesh;";
		String sqlPart = "DELETE FROM part;";
		String sqlBndf = "DELETE FROM bndf;";
		String sqlProp = "DELETE FROM prop;";
		String sqlSpec = "DELETE FROM spec;";
		String sqlDevc = "DELETE FROM devc;";
		String sqlSlcf = "DELETE FROM slcf;";
		String sqlSurf = "DELETE FROM surf;";
		String sqlVent = "DELETE FROM vent;";
		String sqlRamp = "DELETE FROM ramp;";
		String sqlCtrl = "DELETE FROM ctrl;";
		String sqlReac = "DELETE FROM reac;";
		String sqlObst = "DELETE FROM obst;";
		String sqlMisc = "DELETE FROM misc;";
		String sqlRadi = "DELETE FROM radi;";
		String sqlDump = "DELETE FROM dump;";
		String sqlMatl = "DELETE FROM matl;";
		String sqlMult = "DELETE FROM mult;";
		String sqlWind = "DELETE FROM wind;";
		String sqlPres = "DELETE FROM pres;";
		String sqlComb = "DELETE FROM comb;";
		String sqlTabl = "DELETE FROM tabl;";
		String sqlClip = "DELETE FROM clip;";
		String sqlHvac = "DELETE FROM hvac;";
		String sqlHole = "DELETE FROM hole;";
		String sqlIsof = "DELETE FROM isof;";
		String sqlMove = "DELETE FROM move;";
		String sqlProf = "DELETE FROM prof;";
		String sqlRadf = "DELETE FROM radf;";
		String sqlTrnx = "DELETE FROM trnx;";
		String sqlZone = "DELETE FROM zone;";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement;
		statement = connection.createStatement();
		statement.executeUpdate(sqlHead);
		statement.executeUpdate(sqlTime);
		statement.executeUpdate(sqlCatf);
		statement.executeUpdate(sqlInit);
		statement.executeUpdate(sqlMesh);
		statement.executeUpdate(sqlPart);
		statement.executeUpdate(sqlBndf);
		statement.executeUpdate(sqlProp);
		statement.executeUpdate(sqlSpec);
		statement.executeUpdate(sqlDevc);
		statement.executeUpdate(sqlSlcf);
		statement.executeUpdate(sqlSurf);
		statement.executeUpdate(sqlVent);
		statement.executeUpdate(sqlRamp);
		statement.executeUpdate(sqlCtrl);
		statement.executeUpdate(sqlReac);
		statement.executeUpdate(sqlObst);
		statement.executeUpdate(sqlMisc);
		statement.executeUpdate(sqlRadi);
		statement.executeUpdate(sqlDump);
		statement.executeUpdate(sqlMatl);
		statement.executeUpdate(sqlMult);
		statement.executeUpdate(sqlWind);
		statement.executeUpdate(sqlPres);
		statement.executeUpdate(sqlComb);
		statement.executeUpdate(sqlTabl);
		statement.executeUpdate(sqlClip);
		statement.executeUpdate(sqlHvac);
		statement.executeUpdate(sqlHole);
		statement.executeUpdate(sqlIsof);
		statement.executeUpdate(sqlMove);
		statement.executeUpdate(sqlProf);
		statement.executeUpdate(sqlRadf);
		statement.executeUpdate(sqlTrnx);
		statement.executeUpdate(sqlZone);
	}
	
	/**
	 * This function gets the user's confirmation before deleting the .fds file.
	 * @return Boolean value to decide if the .fds file creation should be deleted or not.
	 */
	protected static boolean cancelWarning(){
		Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
		cancelAlert.setTitle("Cancel");
		cancelAlert.setContentText("Clicking on OK would cancel the creation of the input file. Would you like to cancel?");
		cancelAlert.setHeaderText(null);
		Optional<ButtonType> userOption = cancelAlert.showAndWait();
		if (userOption.get() == ButtonType.OK){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * This function provides a confirmation to the user on whether or not a new field line has been added. 
	 * @param namelist The name of the Namelist
	 * @param isSuccess Boolean to know if a new line can be added.
	 */
	public static void printConfirmationMessage(String namelist, boolean isSuccess) {
		if (isSuccess) {
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle(namelist + " line added");
			initAlert.setContentText("A new " + namelist + " line has been added successfully.");
			initAlert.setHeaderText(null);
			initAlert.show();
		}
		else {
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle(namelist + " line not added");
			initAlert.setContentText("A new " + namelist + " line is not able to be added. Please check the input values for " + namelist + ".");
			initAlert.setHeaderText(null);
			initAlert.show();
		}
	}
	
	/**
	 * This function is used to open the description box for each Namelist's heading.
	 * @param namelist The name of the Namelist.
	 * @param content The content of the description for that Namelist.
	 */
	public static void openDesc(String namelist, String content) {
		Alert headAlert = new Alert(Alert.AlertType.INFORMATION);
		headAlert.setTitle(namelist + " namelist");
		headAlert.setContentText(content);
		headAlert.setHeaderText(null);
		ImageView icon = new ImageView("Fire2.jpg");
		icon.setFitHeight(48);
        icon.setFitWidth(48);
        headAlert.getDialogPane().setGraphic(icon);
		headAlert.show();
	}
	
	/**
	 * Error message when an exception is thrown which prevents the page from proceeding.
	 */
	public static void showError() {
		Alert chidAlert = new Alert(Alert.AlertType.INFORMATION);
		chidAlert.setTitle("Unable to proceed");
		chidAlert.setContentText("Please check that there are no invalid characters in the inputs.");
		chidAlert.setHeaderText(null);
		chidAlert.show();
	}
	
	/**
	 * Display the error message during the input validation process.
	 * @param title The title of the error message box.
	 * @param error The actual error to be displayed.
	 */
	public static void displayErrorMsg(String title, String error) {
		Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
		initAlert.setTitle(title);
		initAlert.setContentText(error);
		initAlert.setHeaderText(null);
		initAlert.show();
	}
	
	
	
	/**
     * Check the XB format: <br>
     * - No white spaces <br>
     * - 6 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
	public static boolean checkXbFormat(TextField tempField) {
		String title = "";
		String error = "";
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			title = "Incorrect XB format";
			error = "There should not be any whitespaces.";
			displayErrorMsg(title, error);
			return false;
		}
		String[] xbValues = tempField.getText().split(",");
		String concatXB = "";
		
		if (xbValues.length != 6){
			title = "Incorrect XB format";
			error = "There should be 6 real values.";
			displayErrorMsg(title, error);
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				float floatVal = Float.valueOf(xbValues[i]);
				if (floatVal < 0) { //check if the float is negative
					title = "Invalid XB value";
					error = "The values should not have negative numbers. Please check again.";
					displayErrorMsg(title, error);
					return false;
				}
				if (i==5){
					concatXB = concatXB + Float.toString(floatVal);
				}
				else{
					concatXB = concatXB + Float.toString(floatVal) + ","; //convert to string
				}
			}
			catch(Exception e){//check if each value is real
				title = "Incorrect XB format";
				error = "The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.";
				displayErrorMsg(title, error);
				return false;
			}
		}
		tempField.setText(concatXB);
		return true;
	}
	
	
	/**
     * Check the XYZ format: <br>
     * - No white spaces <br>
     * - 3 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
	public static boolean checkXyzFormat(TextField tempField) {
		String title = "";
		String error = "";
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			title = "Incorrect XYZ format";
			error = "There should not be any whitespaces.";
			displayErrorMsg(title, error);
			return false;
		}
		
		String[] xyzValues = tempField.getText().split(",");
		String concatXYZ = "";
		if (xyzValues.length != 3){ //check if xyz is the correct length
			title = "Incorrect XYZ format";
			error = "There should be 3 positive real values, comma-separated.";
			displayErrorMsg(title, error);
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float xyzFloat = Float.valueOf(xyzValues[i]);
				if (xyzFloat < 0){ //check if xyz is negative or zero
					title = "Incorrect XYZ format";
					error = "The XYZ values should be positive real values.";
					displayErrorMsg(title, error);
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
			title = "Incorrect XYZ format";
			error = "There should be 3 positive real values.";
			displayErrorMsg(title, error);
			return false;
		}
	}
	
	/**
	 * Check the IJK format and if it is filled 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public static boolean checkIJKformat(TextField valueTF) {
		String title = "";
		String error = "";
		if (valueTF.getText().contains(" ")){ //check if there are any white spaces
			title = "Incorrect IJK format";
			error = "There should not be any whitespaces.";
			displayErrorMsg(title, error);
			return false;
		}
		
		String[] ijkValues = valueTF.getText().split(",");
		String concatIJK = "";
		if (ijkValues.length != 3){ //check if ijk is the correct length
			title = "Incorrect IJK format";
			error = "There should be 3 integer values, comma-separated.";
			displayErrorMsg(title, error);
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				int ijkInt = Integer.parseInt(ijkValues[i]);
				if (ijkInt <= 0){ //check if ijk is negative or zero
					title = "Incorrect IJK format";
					error = "The IJK values should be more than zero.";
					displayErrorMsg(title, error);
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
			title = "Incorrect IJK format";
			error = "There should be 3 integer values.";
			displayErrorMsg(title, error);
			return false;
		}
	}
	
	/**
     * Check the Gvec format: <br>
     * - No white spaces <br>
     * - 3 values <br>
     * - Integers 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
	public static boolean checkGvecFormat(TextField tempField) {
		String title = "";
		String error = "";
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			title = "Incorrect Gvec format";
			error = "There should not be any whitespaces.";
			displayErrorMsg(title, error);
			return false;
		}
		
		String[] gvecValues = tempField.getText().split(",");
		String concatGvec = "";
		if (gvecValues.length != 3){ //check if gvec is the correct length
			title = "Incorrect Gvec format";
			error = "There should be 3 real values, comma-separated.";
			displayErrorMsg(title, error);
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float gvecFloat = Float.valueOf(gvecValues[i]);
				if (i==0 || i==1){ //concatenate to format the xyz string
					concatGvec = concatGvec + Float.toString(gvecFloat) + ",";
				}
				else{
					concatGvec = concatGvec + Float.toString(gvecFloat);
				}
			}
			tempField.setText(concatGvec);
			return true;
		}
		catch(Exception e){ //check if xyz is a number
			title = "Incorrect Gvec format";
			error = "There should be 3 real values.";
			displayErrorMsg(title, error);
			return false;
		}
	}
	
	/**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public static boolean checkPosFloatValues(String param, TextField tempField) {
		String title = "";
		String error = "";
		try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal < 0){ //if it is not a positive float
				title = "Invalid float value(s)";
				error = param + " should not be negative value(s). Please check again.";
				displayErrorMsg(title, error);
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			title = "Invalid float value(s)";
			error = param + " should be numerical value(s). Please check again.";
			displayErrorMsg(title, error);
			return false;
		}
	}
	
	/**
	 * Check if the integer is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public static boolean checkPosIntValues(String param, TextField tempField) {
		String title = "";
		String error = "";
		try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				title = "Invalid integer value(s)";
				error = param + " should be positive integer(s). Please check again.";
				displayErrorMsg(title, error);
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			title = "Invalid integer value(s)";
			error = param + " should be integer(s). Please check again.";
			displayErrorMsg(title, error);
			return false;
		}
	}
	
	/**
	 * Check if the value is a float 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
	public static boolean checkFloatValues(String param, TextField tempField) {
		String title = "";
		String error = "";
		try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			title = "Invalid float value(s)";
			error = param + " should be numerical values. Please check again.";
			displayErrorMsg(title, error);
			return false;
		}
	}
}
