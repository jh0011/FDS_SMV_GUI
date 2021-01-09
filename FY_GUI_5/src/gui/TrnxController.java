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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TrnxController implements Initializable{
	/**
	 * Controller class for Trnx.fxml
	 * @author 
	 */
	
	//trnx
	@FXML TextField idText; //string
    @FXML TextField meshText; //integer (+)
    @FXML TextField ccText; //float (+)
    @FXML TextField pcText; //float (+)
    
    //zone
    @FXML TextField xyzText; //float (3) (+)

    @FXML Button addTrnxBtn;
   	@FXML Button addZoneBtn;
    
    boolean checkPosInt;
    boolean checkPosFloat;
    boolean checkXyz;
    
    static int mainTrnxId = 1;
    static int mainZoneId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip trnxTooltip = new Tooltip("Click to add another TRNX field.");
		addTrnxBtn.setTooltip(trnxTooltip);
		
		Tooltip zoneTooltip = new Tooltip("Click to add another ZONE field.");
		addZoneBtn.setTooltip(zoneTooltip);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException
	 * @throws IOException
	 */
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

	/**
	 * Go to the previous page (MOVE) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToMove(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
	    	if(checkPosInt && checkPosFloat && checkXyz) {
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
    	}catch(Exception e) {
    		e.printStackTrace();
			Values.showError();
		}
    }
    
    /**
	 * Go to the next page (Editor) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToEditor(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	try {
	    	if(checkPosInt && checkPosFloat && checkXyz) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Editor.fxml"));
	    		Parent root = loader.load();
	    		
	    		EditorController editorCont = loader.getController(); //Get the next page's controller
	    		//editorCont.showInfo(); //Set the values of the page 
	    		Scene editorScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(editorScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
    		e.printStackTrace();
			Values.showError();
		}
    }
    
    /**
	 * Add a new line for TRNX namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void newTrnxLine(ActionEvent event) throws SQLException { //ADD NEW TRNX LINE
    	doCheckingTrnx();
    	
    	if (checkPosInt && checkPosFloat) {
    		//store values
    		storeValuesTrnx();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("TRNX", true);
    		
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
    		//confirmation message for failure
			Values.printConfirmationMessage("TRNX", false);
    		//System.out.println("Unable to add new TRNX line");
    	}
    }
    
    /**
	 * Add a new line for ZONE namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void newZoneLine(ActionEvent event) throws SQLException { //ADD NEW ZONE LINE
    	doCheckingZone();
    	
    	if(checkXyz) {
    		//store values
    		storeValuesZone();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("ZONE", true);
    		
    		mainZoneId++;
        	String mainZoneIdString = Integer.toString(mainZoneId);
        	String sqlZone = "INSERT INTO zone (mainID, XYZ) VALUES ('" + mainZoneIdString + "', '');";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(sqlZone);
    		
    		showInfoZone();
    	}
    	else {
    		//confirmation message for failure
			Values.printConfirmationMessage("ZONE", false);
    		//System.out.println("Unable to add new ZONE line");
    	}
    }
    
    /**
	 * Description of TRNX namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openTrnxDesc(MouseEvent event) {
    	String content = "The TRNX (mesh stretching) namelist can be used to create non-uniform cells. By default the mesh cells that fill the "
    			+ "computational domain are uniform in size. However, it is possible to specify that the cells be non-uniform in one or two "
    			+ "of the three coordinate directions. \n\n"
    			+ "CC: Computational Coordinate should be between 0 and 1.5, inclusive.\n\n"
    			+ "Mesh_number: If you want the transformation to be applied to all meshes, set MESH_NUMBER to 0.\n\n"
    			+ "PC: Physical Coordinate should be between 0 and 1.5, inclusive.";
    	String namelist = "TRNX";
		Values.openDesc(namelist, content);
    }

    /**
	 * Description of ZONE namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openZoneDesc(MouseEvent event) {
    	String content = "A pressure zone can be any region within the computational domain that is separated from the rest of the "
    			+ "domain, or the exterior, by solid obstructions. There is currently no algorithm within FDS to identify these "
    			+ "zones based solely on your specified obstructions. Consequently, it is necessary that you identify these zones "
    			+ "explicitly in the input file. \n\n"
    			+ "XYZ: It specifies a single point that is within a sealed compartment, and it is "
    			+ "not embedded within a solid obstruction.";
    	String namelist = "ZONE";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingTrnx();
    	doCheckingZone();
    }
    
    public void doCheckingTrnx() {
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
    
    public void doCheckingZone() {
    	checkXyz = true;
    	if (!xyzText.getText().equals("")) {
    		checkXyz = checkXyz && checkXyzFormat(xyzText);
    	}
    }
    
    /**
	 * Check if the integer is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkPosIntValues(TextField tempField) { //check if integer is positive
    	String param = "Mesh_number";
    	return Values.checkPosIntValues(param, tempField);
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkPosFloatValues(TextField tempField) { //check if float is positive
    	String param = "CC and PC";
    	return Values.checkPosFloatValues(param, tempField);
    }
    
    /**
     * Check the XYZ format: <br>
     * - No white spaces <br>
     * - 3 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
    public boolean checkXyzFormat(TextField tempField) { //check if xyz is correct
    	return Values.checkXyzFormat(tempField);
    }
    
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesTrnx();
    	storeValuesZone();
    }
    
    public void storeValuesTrnx() throws SQLException { //store TRNX values into the database
    	String mainTrnxIdString = Integer.toString(mainTrnxId);
    	String sqlTrnx = "INSERT INTO trnx VALUES ('" + mainTrnxIdString + "', '" + idText.getText() + "', '" + meshText.getText() + "', '" + ccText.getText() + "', '" + pcText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlTrnx);
    }
    
    public void storeValuesZone() throws SQLException { //store ZONE values into the database
    	String mainZoneIdString = Integer.toString(mainZoneId);
    	String sqlZone = "INSERT INTO zone VALUES ('" + mainZoneIdString + "', '" + xyzText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlZone);
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoTrnx();
    	showInfoZone();
    }
    
    public void showInfoTrnx() throws SQLException { //to show the info when the page is loaded
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
    
    public void showInfoZone() throws SQLException { //to show the info when the page is loaded
    	String sqlZone = "SELECT * FROM zone";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlZone);
		while (rs.next()) {
			xyzText.setText(rs.getString(2));
		}
    }

}
