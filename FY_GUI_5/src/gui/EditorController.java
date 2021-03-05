package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class EditorController implements Initializable{
	
	@FXML TextArea editorText;
	static String tempEditorString = "";
	static String path = "";
	//static String CHID = "";
	static String CHID = "default1";
	static FileWriter fw;
	static BufferedWriter bw = null;
	static File fileDirectory;
	//static File fileDirectory = new File("C:\\Users\\dell\\Desktop\\couch_modified_try");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showFile();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to show Editor");
		}
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
	 * Go to the next page (Final) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    public void goToFinal(ActionEvent event) throws IOException { //NEXT SCENE
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Final.fxml"));
		Parent root = loader.load();
		
		FinalController finalCont = loader.getController(); //Get the next page's controller
		Scene finalScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(finalScene);
		mainWindow.show();
    }
    
    /**
     * This function handles the saving of the file to the user's system.
     * If the file already exists in the directory, the user has the option of overwriting it or choosing a different directory.
     * If the file does not exist in the directory, the user will be able to save the file.
     * A confirmation message is displayed after the file is saved.
     * @param event The "Save" button is clicked.
     * @throws IOException
     */
    @FXML
    public void saveFile(ActionEvent event) { //save to the file system
    	try { 
	    	//store the updated text area
	    	tempEditorString = editorText.getText();
	    	
	    	DirectoryChooser directoryChooser = new DirectoryChooser();
	    	directoryChooser.setTitle("Save FDS input file");
	    	File selectedDirectory = directoryChooser.showDialog(null);
	    	//System.out.println(selectedDirectory.getAbsolutePath());
	    	File outputFile = new File(selectedDirectory + "\\" + CHID + ".fds");
	    	fileDirectory = selectedDirectory;
	    	boolean isFileCreated = false;
	    	if (!outputFile.exists()){
	    		isFileCreated = outputFile.createNewFile();
	    		if (isFileCreated) { //confirmation message for CREATION of the file
	        		Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
	    			trnxAlert.setTitle("Successful file creation");
	    			trnxAlert.setContentText("File has been created in directory: " + selectedDirectory);
	    			trnxAlert.setHeaderText(null);
	    			ImageView icon = new ImageView("Fire2.jpg");
	    			icon.setFitHeight(48);
	    	        icon.setFitWidth(48);
	    	        trnxAlert.getDialogPane().setGraphic(icon);
	    			trnxAlert.show();
	    			
	    			fw = new FileWriter(outputFile);
	    			bw = new BufferedWriter(fw);
	    			bw.write(editorText.getText());
	    			bw.close();
	    			
	    			//proceed to the final page
	    	    	goToFinal(event);
	        	}
			}
	    	else { //confirmation to overwrite the existing file
				Alert trnxAlert = new Alert(Alert.AlertType.CONFIRMATION);
				trnxAlert.setTitle("File already exists");
				trnxAlert.setContentText("File with the same name already exists in directory: " + selectedDirectory + 
						"\nClick on OK to overwrite the existing file. Click on Cancel to choose another directory.");
				trnxAlert.setHeaderText(null);
				ImageView icon = new ImageView("Fire2.jpg");
				icon.setFitHeight(48);
		        icon.setFitWidth(48);
		        trnxAlert.getDialogPane().setGraphic(icon);
		        Optional<ButtonType> userOption = trnxAlert.showAndWait();
		        if (userOption.get() == ButtonType.OK){
		        	fw = new FileWriter(outputFile);
		    		bw = new BufferedWriter(fw);
		    		bw.write(editorText.getText());
		    		bw.close();
		    		
		    		//proceed to the final page
		        	goToFinal(event);
				}
			}
    	}
    	catch(IOException e) {
    		System.out.println("Unable to save the .fds file to the file system.");
    	}
    }
    
    /**
     * Print the values into the in-app editor.
     * @throws SQLException
     */
    public void showFile() throws SQLException {
    	if (!tempEditorString.equals("")) {
    		editorText.setText(tempEditorString);
    	}
    	else {
    		if (checkIfFilled("head")) {
    			printValuesHead();
    		}
    		if (checkIfFilled("mesh")) {
    			printValuesMesh();
    		}
    		if (checkIfFilled("time")) {
    			printValuesTime();
    		}
    		if (checkIfFilled("init")) {
    			printValuesInit();
    		}
    		if (checkIfFilled("surf")) {
    			printValuesSurf();
    		}
    		if (checkIfFilled("reac")) {
    			printValuesReac();
    		}
    		if (checkIfFilled("vent")) {
    			printValuesVent();
    		}
    		if (checkIfFilled("part")) {
    			printValuesPart();
    		}
    		if (checkIfFilled("slcf")) {
    			printValuesSlcf();
    		}
    		if (checkIfFilled("matl")) {
    			printValuesMatl();
    		}
    		if (checkIfFilled("obst")) {
    			printValuesObst();
    		}
    		if (checkIfFilled("spec")) {
    			printValuesSpec();
    		}
    		if (checkIfFilled("devc")) {
    			printValuesDevc();
    		}
    		if (checkIfFilled("ctrl")) {
    			printValuesCtrl();
    		}
    		if (checkIfFilled("prop")) {
    			printValuesProp();
    		}
    		if (checkIfFilled("radi")) {
    			printValuesRadi();
    		}
    		if (checkIfFilled("radf")) {
    			printValuesRadf();
    		}
    		if (checkIfFilled("mult")) {
    			printValuesMult();
    		}
    		if (checkIfFilled("pres")) {
    			printValuesPres();
    		}
    		if (checkIfFilled("move")) {
    			printValuesMove();
    		}
    		if (checkIfFilled("isof")) {
    			printValuesIsof();
    		}
    		if (checkIfFilled("hvac")) {
    			printValuesHvac();
    		}
    		if (checkIfFilled("hole")) {
    			printValuesHole();
    		}
    		if (checkIfFilled("misc")) {
    			printValuesMisc();
    		}
    		if (checkIfFilled("trnx")) {
    			printValuesTrnx();
    		}
    		if (checkIfFilled("bndf")) {
    			printValuesBndf();
    		}
    		if (checkIfFilled("catf")) {
    			printValuesCatf();
    		}
    		if (checkIfFilled("prof")) {
    			printValuesProf();
    		}
    		if (checkIfFilled("ramp")) {
    			printValuesRamp();
    		}
    		if (checkIfFilled("clip")) {
    			printValuesClip();
    		}
    		if (checkIfFilled("comb")) {
    			printValuesComb();
    		}
    		if (checkIfFilled("dump")) {
    			printValuesDump();
    		}
    		if (checkIfFilled("wind")) {
    			printValuesWind();
    		}
    		if (checkIfFilled("tabl")) {
    			printValuesTabl();
    		}
    		if (checkIfFilled("zone")) {
    			printValuesZone();
    		}
    		editorText.appendText("&TAIL /");
    	}
    	tempEditorString = editorText.getText();
    }
    
    /**
     * Check if the table is filled.
     * @param tableName The name of the table in the database.
     * @return Boolean on whether it is filled.
     * @throws SQLException
     */
    public boolean checkIfFilled(String tableName) throws SQLException {
    	String sqlQuery = "SELECT * FROM " + tableName;
		ResultSet rs = getStatement().executeQuery(sqlQuery);
    	ResultSetMetaData metaData = rs.getMetaData();
    	int count = metaData.getColumnCount();
    	String columnNames[] = new String[count];
    	String columnValues[] = new String[count];
    	for (int i=0; i<count; i++) {
    		columnNames[i] = metaData.getColumnLabel(i + 1);
    	}
    	
    	if (!columnNames[0].equals("mainID")) {
    		while (rs.next()) {
        		for (int i=0; i<count; i++) {
            		columnValues[i] = rs.getString(i + 1);
            	}
        	}
    		
    		return checkIfRowFilled(columnNames, columnValues);
    	}
    	else {
    		String sqlString = "SELECT * FROM " + tableName + " GROUP BY mainID"; //Check for each mainID
        	ResultSet rs2 = getStatement().executeQuery(sqlString);
        	String mainID = "";
        	while (rs2.next()) {
        		mainID = rs2.getString(1);
        	}
        	for (int i=1; i <= Integer.parseInt(mainID); i++) {
        		sqlString = "SELECT * FROM " + tableName + " WHERE mainID='" + i + "';";
        		rs2 = getStatement().executeQuery(sqlString);
        		while (rs2.next()) {
        			for (int j=0; j<count; j++) {
                		columnValues[j] = rs2.getString(j + 1);
                	}
        		}
        		if (checkIfRowFilled(columnNames, columnValues)) {
        			return true;
        		}
        	}
        	
    		return false;
    	}
    	
    	
    }
    
    /**
     * Check if a particular row in the table is filled.
     * @param columnNames An array of the column names.
     * @param columnValues An array of te corresponding column values.
     * @return Boolean to check if the row is filled.
     */
    public boolean checkIfRowFilled(String[] columnNames, String[] columnValues){
    	int count = columnNames.length;
    	
		//check if the row is filled
		for (int i=0; i<count; i++) {
			if (!columnNames[i].equals("mainID")) {
				if (!columnValues[i].equals("")) {
	    			return true;
	    		}
			}
    	}
		return false;
    }
    
    /**
     * Print the values for the HEAD namelist.
     * @throws SQLException
     */
    public void printValuesHead() throws SQLException { //head
    	String sqlHead = "SELECT * FROM head";
    	String TITLE = "";
		ResultSet rs = getStatement().executeQuery(sqlHead);
		while (rs.next()) {
			CHID = rs.getString(1);
			TITLE = rs.getString(2);
		}
		editorText.appendText("&HEAD" + "\n");
		appendToEditorString("CHID", CHID);
		appendToEditorString("TITLE", TITLE);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the MESH namelist.
     * @throws SQLException
     */
    public void printValuesMesh() throws SQLException { //mesh
    	//get the number of MESH lines
    	String sqlMesh = "SELECT mainID FROM mesh GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlMesh);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each MESH line
	    	String meshArray[] = new String[2];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlMesh = "SELECT * FROM mesh WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlMesh);
	        	while (rs.next()) {
	        		meshArray[0] = rs.getString(2);
	        		meshArray[1] = rs.getString(3);
	        	}
	        	if (isArrayFilled(meshArray)) {
	        		editorText.appendText("&MESH" + "\n");
		        	appendToEditorNumber("IJK", meshArray[0]);
		        	appendToEditorNumber("XB", meshArray[1]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the TIME namelist.
     * @throws SQLException
     */
    public void printValuesTime() throws SQLException { //time
    	String sqlTime = "SELECT * FROM time";
    	String endTime = "";
    	String startTime = "";
    	String dt = "";
		ResultSet rs = getStatement().executeQuery(sqlTime);
		while (rs.next()) {
			endTime = rs.getString(1);
			startTime = rs.getString(2);
			dt = rs.getString(3);
		}
		editorText.appendText("&TIME" + "\n");
		appendToEditorNumber("T_END", endTime);
		appendToEditorNumber("T_BEGIN", startTime);
		appendToEditorNumber("DT", dt);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the INIT namelist.
     * @throws SQLException
     */
    public void printValuesInit() throws SQLException {
    	//get the number of INIT lines
    	String sqlInit = "SELECT mainID FROM init GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlInit);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each INIT line
	    	String initArray[] = new String[9];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlInit = "SELECT * FROM init WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlInit);
	        	while (rs.next()) {
	        		initArray[0] = rs.getString(2);
	        		initArray[1] = rs.getString(3);
	        		initArray[2] = rs.getString(4);
	        		initArray[3] = rs.getString(5);
	        		initArray[4] = rs.getString(6);
	        		initArray[5] = rs.getString(7);
	        		initArray[6] = rs.getString(8);
	        		initArray[7] = rs.getString(9);
	        		initArray[8] = rs.getString(10);
	        	}
	        	if (isArrayFilled(initArray)) {
	        		editorText.appendText("&INIT" + "\n");
		        	appendToEditorString("ID", initArray[0]);
		        	appendToEditorString("PART_ID", initArray[1]);
		        	appendToEditorString("SPEC_ID", initArray[2]);
		        	appendToEditorNumber("N_PARTICLES", initArray[3]);
		        	appendToEditorNumber("N_PARTICLES_PER_CELL", initArray[4]);
		        	appendToEditorNumber("MASS_PER_TIME", initArray[5]);
		        	appendToEditorNumber("MASS_PER_VOLUME", initArray[6]);
		        	appendToEditorNumber("MASS_FRACTION", initArray[7]);
		        	appendToEditorNumber("XB", initArray[8]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the SURF namelist.
     * @throws SQLException
     */
    public void printValuesSurf() throws SQLException { //surf
    	//get the number of SURF lines
    	String sqlSurf = "SELECT mainID FROM surf GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSurf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each SURF line
	    	String surfArray[] = new String[10];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSurf = "SELECT * FROM surf WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSurf);
	        	while (rs.next()) {
	        		surfArray[0] = rs.getString(2);
	        		surfArray[1] = rs.getString(3);
	        		surfArray[2] = rs.getString(4);
	        		surfArray[3] = rs.getString(5);
	        		surfArray[4] = rs.getString(6);
	        		surfArray[5] = rs.getString(7);
	        		surfArray[6] = rs.getString(8);
	        		surfArray[7] = rs.getString(9);
	        		surfArray[8] = rs.getString(10);
	        		surfArray[9] = rs.getString(11);
	        	}
	        	if (isArrayFilled(surfArray)) {
	        		editorText.appendText("&SURF" + "\n");
		        	appendToEditorString("ID", surfArray[0]);
		        	appendToEditorString("PART_ID", surfArray[1]);
		        	appendToEditorString("MATL_ID", surfArray[2]);
		        	appendToEditorNumber("VEL", surfArray[3]);
		        	appendToEditorNumber("TMP_FRONT", surfArray[4]);
		        	appendToEditorString("BACKING", surfArray[5]);
		        	appendToEditorBoolean("DEFAULT", surfArray[6]);
		        	appendToEditorString("GEOMETRY", surfArray[7]);
		        	appendToEditorString("COLOR", surfArray[8]);
		        	appendToEditorNumber("HRRPUA", surfArray[9]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the REAC namelist.
     * @throws SQLException
     */
    public void printValuesReac() throws SQLException { //reac
    	//get the number of REAC lines
    	String sqlReac = "SELECT mainID FROM reac GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlReac);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each REAC line
	    	String reacArray[] = new String[3];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlReac = "SELECT * FROM reac WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlReac);
	        	while (rs.next()) {
	        		reacArray[0] = rs.getString(2);
	        		reacArray[1] = rs.getString(3);
	        		reacArray[2] = rs.getString(4);
	        	}
	        	if (isArrayFilled(reacArray)) {
	        		editorText.appendText("&REAC" + "\n");
		        	appendToEditorNumber("AUTO_IGNITION_TEMPERATURE", reacArray[0]);
		        	appendToEditorNumber("SOOT_YIELD", reacArray[1]);
		        	appendToEditorString("FUEL", reacArray[2]);
		        	editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the VENT namelist.
     * @throws SQLException
     */
    public void printValuesVent() throws SQLException{ //vent
    	//get the number of VENT lines
    	String sqlVent = "SELECT mainID FROM vent GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlVent);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each VENT line
	    	String ventArray[] = new String[3];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlVent = "SELECT * FROM vent WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlVent);
	        	while (rs.next()) {
	        		ventArray[0] = rs.getString(2);
	        		ventArray[1] = rs.getString(3);
	        		ventArray[2] = rs.getString(4);
	        	}
	        	if (isArrayFilled(ventArray)) {
	        		editorText.appendText("&VENT" + "\n");
		        	appendToEditorNumber("XB", ventArray[0]);
		        	appendToEditorString("SURF_ID", ventArray[1]);
		        	appendToEditorString("MB", ventArray[2]);
		        	editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the PART namelist.
     * @throws SQLException
     */
    public void printValuesPart() throws SQLException{ //part
    	//get the number of PART lines
    	String sqlPart = "SELECT mainID FROM part GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlPart);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each PART line
	    	String partArray[] = new String[9];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlPart = "SELECT * FROM part WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlPart);
	        	while (rs.next()) {
	        		partArray[0] = rs.getString(2);
	        		partArray[1] = rs.getString(3);
	        		partArray[2] = rs.getString(4);
	        		partArray[3] = rs.getString(5);
	        		partArray[4] = rs.getString(6);
	        		partArray[5] = rs.getString(7);
	        		partArray[6] = rs.getString(8);
	        		partArray[7] = rs.getString(9);
	        		partArray[8] = rs.getString(10);
	        	}
	        	if (isArrayFilled(partArray)) {
	        		editorText.appendText("&PART" + "\n");
		        	appendToEditorString("SURF_ID", partArray[0]);
		        	appendToEditorString("SPEC_ID", partArray[1]);
		        	appendToEditorString("PROP_ID", partArray[2]);
		        	appendToEditorString("QUANTITIES", partArray[3]);
		        	appendToEditorBoolean("STATIC", partArray[4]);
		        	appendToEditorBoolean("MASSLESS", partArray[5]);
		        	appendToEditorNumber("SAMPLING_FACTOR", partArray[6]);
		        	appendToEditorNumber("DIAMETER", partArray[7]);
		        	appendToEditorString("ID", partArray[8]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the SLCF namelist.
     * @throws SQLException
     */
    public void printValuesSlcf() throws SQLException { //slcf
    	//get the number of SLCF lines
    	String sqlSlcf = "SELECT mainID FROM slcf GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSlcf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each SLCF line
	    	String slcfArray[] = new String[7];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSlcf = "SELECT * FROM slcf WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSlcf);
	        	while (rs.next()) {
	        		slcfArray[0] = rs.getString(2);
	        		slcfArray[1] = rs.getString(3);
	        		slcfArray[2] = rs.getString(4);
	        		slcfArray[3] = rs.getString(5);
	        		slcfArray[4] = rs.getString(6);
	        		slcfArray[5] = rs.getString(7);
	        		slcfArray[6] = rs.getString(8);
	        	}
	        	if (isArrayFilled(slcfArray)) {
	        		editorText.appendText("&SLCF" + "\n");
		        	appendToEditorString("QUANTITY", slcfArray[0]);
		        	appendToEditorString("SPEC_ID", slcfArray[1]);
		        	appendToEditorNumber("PBY", slcfArray[2]);
		        	appendToEditorNumber("PBZ", slcfArray[3]);
		        	appendToEditorNumber("PBX", slcfArray[4]);
		        	appendToEditorBoolean("VECTOR", slcfArray[5]);
		        	appendToEditorBoolean("CELL_CENTERED", slcfArray[6]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the MATL namelist.
     * @throws SQLException
     */
    public void printValuesMatl() throws SQLException {
    	//get the number of MATL lines
    	String sqlMatl = "SELECT mainID FROM matl GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlMatl);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each MATL line
	    	String matlArray[] = new String[8];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlMatl = "SELECT * FROM matl WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlMatl);
	        	while (rs.next()) {
	        		matlArray[0] = rs.getString(2);
	        		matlArray[1] = rs.getString(3);
	        		matlArray[2] = rs.getString(4);
	        		matlArray[3] = rs.getString(5);
	        		matlArray[4] = rs.getString(6);
	        		matlArray[5] = rs.getString(7);
	        		matlArray[6] = rs.getString(8);
	        		matlArray[7] = rs.getString(9);
	        	}
	        	if (isArrayFilled(matlArray)) {
	        		editorText.appendText("&MATL" + "\n");
		        	appendToEditorNumber("SPECIFIC_HEAT", matlArray[0]);
		        	appendToEditorNumber("HEAT_OF_REACTION", matlArray[1]);
		        	appendToEditorString("SPEC_ID", matlArray[2]);
		        	appendToEditorString("ID", matlArray[3]);
		        	appendToEditorNumber("REFERENCE_TEMPERATURE", matlArray[4]);
		        	appendToEditorNumber("N_REACTIONS", matlArray[5]);
		        	appendToEditorNumber("DENSITY", matlArray[6]);
		        	appendToEditorNumber("CONDUCTIVITY", matlArray[7]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the OBST namelist.
     * @throws SQLException
     */
    public void printValuesObst() throws SQLException { //obst
    	//get the number of OBST lines
    	String sqlObst = "SELECT mainID FROM obst GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlObst);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each OBST line
	    	String obstArray[] = new String[4];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlObst = "SELECT * FROM obst WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlObst);
	        	while (rs.next()) {
	        		obstArray[0] = rs.getString(2);
	        		obstArray[1] = rs.getString(3);
	        		obstArray[2] = rs.getString(4);
	        		obstArray[3] = rs.getString(5);
	        	}
	        	if (isArrayFilled(obstArray)) {
	        		editorText.appendText("&OBST" + "\n");
		        	appendToEditorNumber("BULK_DENSITY", obstArray[0]);
		        	appendToEditorString("COLOR", obstArray[1]);
		        	appendToEditorString("SURF_ID", obstArray[2]);
		        	appendToEditorNumber("XB", obstArray[3]);
		        	editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the SPEC namelist.
     * @throws SQLException
     */
    public void printValuesSpec() throws SQLException { //spec
    	//get the number of SPEC lines
    	String sqlSpec = "SELECT mainID FROM spec GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSpec);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each SPEC line
	    	String specArray[] = new String[2];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSpec = "SELECT * FROM spec WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSpec);
	        	while (rs.next()) {
	        		specArray[0] = rs.getString(2);
	        		specArray[1] = rs.getString(3);
	        	}
	        	if (isArrayFilled(specArray)) {
	        		editorText.appendText("&SPEC" + "\n");
		        	appendToEditorString("ID", specArray[0]);
		        	appendToEditorBoolean("BACKGROUND", specArray[1]);
		        	editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the DEVC namelist.
     * @throws SQLException
     */
    public void printValuesDevc() throws SQLException { //devc
    	//get the number of DEVC lines
    	String sqlDevc = "SELECT mainID FROM devc GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlDevc);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each DEVC line
	    	String devcArray[] = new String[7];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlDevc = "SELECT * FROM devc WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlDevc);
	        	while (rs.next()) {
	        		devcArray[0] = rs.getString(2);
	        		devcArray[1] = rs.getString(3);
	        		devcArray[2] = rs.getString(4);
	        		devcArray[3] = rs.getString(5);
	        		devcArray[4] = rs.getString(6);
	        		devcArray[5] = rs.getString(7);
	        		devcArray[6] = rs.getString(8);
	        	}
	        	if (isArrayFilled(devcArray)) {
	        		editorText.appendText("&DEVC" + "\n");
		        	appendToEditorString("ID", devcArray[0]);
		        	appendToEditorString("PROP_ID", devcArray[1]);
		        	appendToEditorString("SPEC_ID", devcArray[2]);
		        	appendToEditorNumber("XYZ", devcArray[3]);
		        	appendToEditorString("QUANTITY", devcArray[4]);
		        	appendToEditorNumber("IOR", devcArray[5]);
		        	appendToEditorNumber("XB", devcArray[6]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the CTRL namelist.
     * @throws SQLException
     */
    public void printValuesCtrl() throws SQLException { //ctrl
    	//get the number of CTRL lines
    	String sqlCtrl = "SELECT mainID FROM ctrl GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlCtrl);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each CTRL line
	    	String ctrlArray[] = new String[5];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlCtrl = "SELECT * FROM ctrl WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlCtrl);
	        	while (rs.next()) {
	        		ctrlArray[0] = rs.getString(2);
	        		ctrlArray[1] = rs.getString(3);
	        		ctrlArray[2] = rs.getString(4);
	        		ctrlArray[3] = rs.getString(5);
	        		ctrlArray[4] = rs.getString(6);
	        	}
	        	if (isArrayFilled(ctrlArray)) {
	        		editorText.appendText("&CTRL" + "\n");
		        	appendToEditorString("INPUT_ID", ctrlArray[0]);
		        	appendToEditorString("RAMP_ID", ctrlArray[1]);
		        	appendToEditorString("ID", ctrlArray[2]);
		        	appendToEditorBoolean("LATCH", ctrlArray[3]);
		        	appendToEditorString("FUNCTION_TYPE", ctrlArray[4]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the PROP namelist.
     * @throws SQLException
     */
    public void printValuesProp() throws SQLException { //prop
    	//get the number of PROP lines
    	String sqlSurf = "SELECT mainID FROM prop GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlSurf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each PROP line
	    	String propArray[] = new String[10];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlSurf = "SELECT * FROM prop WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlSurf);
	        	while (rs.next()) {
	        		propArray[0] = rs.getString(2);
	        		propArray[1] = rs.getString(3);
	        		propArray[2] = rs.getString(4);
	        		propArray[3] = rs.getString(5);
	        		propArray[4] = rs.getString(6);
	        		propArray[5] = rs.getString(7);
	        		propArray[6] = rs.getString(8);
	        		propArray[7] = rs.getString(9);
	        		propArray[8] = rs.getString(10);
	        		propArray[9] = rs.getString(11);
	        	}
	        	if (isArrayFilled(propArray)) {
	        		editorText.appendText("&PROP" + "\n");
		        	appendToEditorString("ID", propArray[0]);
		        	appendToEditorString("PART_ID", propArray[1]);
		        	appendToEditorString("QUANTITY", propArray[2]);
		        	appendToEditorString("SMOKEVIEW_ID", propArray[3]);
		        	appendToEditorNumber("OFFSET", propArray[4]);
		        	appendToEditorBoolean("PDPA_INTEGRATE", propArray[5]);
		        	appendToEditorBoolean("PDPA_NORMALIZE", propArray[6]);
		        	appendToEditorNumber("OPERATING_PRESSURE", propArray[7]);
		        	appendToEditorNumber("PARTICLES_PER_SECOND", propArray[8]);
		        	appendToEditorNumber("PARTICLE_VELOCITY", propArray[9]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the RADI namelist.
     * @throws SQLException
     */
    public void printValuesRadi() throws SQLException { //radi
    	String sqlRadi = "SELECT * FROM radi";
    	String RADIATION = "";
		ResultSet rs = getStatement().executeQuery(sqlRadi);
		while (rs.next()) {
			RADIATION = rs.getString(1);
		}
		editorText.appendText("&RADI" + "\n");
		appendToEditorBoolean("RADIATION", RADIATION);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the RADF namelist.
     * @throws SQLException
     */
    public void printValuesRadf() throws SQLException { //radf
    	String sqlRadf = "SELECT * FROM radf";
    	String I_STEP = "";
    	String J_STEP = "";
    	String K_STEP = "";
    	String XB = "";
		ResultSet rs = getStatement().executeQuery(sqlRadf);
		while (rs.next()) {
			I_STEP = rs.getString(1);
			J_STEP = rs.getString(2);
			K_STEP = rs.getString(3);
			XB = rs.getString(4);
		}
		editorText.appendText("&RADF" + "\n");
		appendToEditorNumber("I_STEP", I_STEP);
		appendToEditorNumber("J_STEP", J_STEP);
		appendToEditorNumber("K_STEP", K_STEP);
		appendToEditorNumber("XB", XB);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the MULT namelist.
     * @throws SQLException
     */
    public void printValuesMult() throws SQLException { //mult
    	//get the number of MULT lines
    	String sqlMult = "SELECT mainID FROM mult GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlMult);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each MULT line
	    	String multArray[] = new String[7];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlMult = "SELECT * FROM mult WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlMult);
	        	while (rs.next()) {
	        		multArray[0] = rs.getString(2);
	        		multArray[1] = rs.getString(3);
	        		multArray[2] = rs.getString(4);
	        		multArray[3] = rs.getString(5);
	        		multArray[4] = rs.getString(6);
	        		multArray[5] = rs.getString(7);
	        		multArray[6] = rs.getString(8);
	        	}
	        	if (isArrayFilled(multArray)) {
	        		editorText.appendText("&MULT" + "\n");
		        	appendToEditorString("ID", multArray[0]);
		        	appendToEditorNumber("I_UPPER", multArray[1]);
		        	appendToEditorNumber("J_UPPER", multArray[2]);
		        	appendToEditorNumber("K_UPPER", multArray[3]);
		        	appendToEditorNumber("DX", multArray[4]);
		        	appendToEditorNumber("DY", multArray[5]);
		        	appendToEditorNumber("DZ", multArray[6]);
		    		editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the PRES namelist.
     * @throws SQLException
     */
    public void printValuesPres() throws SQLException { //pres
    	String sqlPres = "SELECT * FROM pres";
    	String FISHPAK_BC = "";
    	String SOLVER = "";
		ResultSet rs = getStatement().executeQuery(sqlPres);
		while (rs.next()) {
			FISHPAK_BC = rs.getString(1);
			SOLVER = rs.getString(2);
		}
		editorText.appendText("&PRES" + "\n");
		appendToEditorNumber("FISHPAK_BC", FISHPAK_BC);
		appendToEditorString("SOLVER", SOLVER);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the MOVE namelist.
     * @throws SQLException
     */
    public void printValuesMove() throws SQLException { //move
    	String sqlMove = "SELECT * FROM move";
    	String ID = "";
    	String X0 = "";
    	String Y0 = "";
    	String Z0 = "";
    	String ROTATION_ANGLE = "";
    	String AXIS = "";
		ResultSet rs = getStatement().executeQuery(sqlMove);
		while (rs.next()) {
			ID = rs.getString(1);
			X0 = rs.getString(2);
			Y0 = rs.getString(3);
			Z0 = rs.getString(4);
			ROTATION_ANGLE = rs.getString(5);
			AXIS = rs.getString(6);
		}
		editorText.appendText("&MOVE" + "\n");
		appendToEditorString("ID", ID);
		appendToEditorNumber("X0", X0);
		appendToEditorNumber("Y0", Y0);
		appendToEditorNumber("Z0", Z0);
		appendToEditorNumber("ROTATION_ANGLE", ROTATION_ANGLE);
		appendToEditorNumber("AXIS", AXIS);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the ISOF namelist.
     * @throws SQLException
     */
    public void printValuesIsof() throws SQLException { //isof
    	String sqlIsof = "SELECT * FROM isof";
    	String QUANTITY = "";
    	String VALUE_1 = "";
    	String VALUE_2 = "";
    	String VALUE_3 = "";
		ResultSet rs = getStatement().executeQuery(sqlIsof);
		while (rs.next()) {
			QUANTITY = rs.getString(1);
			VALUE_1 = rs.getString(2);
			VALUE_2 = rs.getString(3);
			VALUE_3 = rs.getString(4);
		}
		editorText.appendText("&ISOF" + "\n");
		appendToEditorString("QUANTITY", QUANTITY);
		appendToEditorNumber("VALUE_1", VALUE_1);
		appendToEditorNumber("VALUE_2", VALUE_2);
		appendToEditorNumber("VALUE_3", VALUE_3);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the HVAC namelist.
     * @throws SQLException
     */
    public void printValuesHvac() throws SQLException { //hvac
    	String sqlHvac = "SELECT * FROM hvac";
    	String ID = "";
    	String ROUGHNESS = "";
    	String DEVC_ID = "";
    	String LENGTH = "";
    	String FAN_ID = "";
    	String AREA = "";
    	String TYPE_ID = "";
		ResultSet rs = getStatement().executeQuery(sqlHvac);
		while (rs.next()) {
			ID = rs.getString(1);
			ROUGHNESS = rs.getString(2);
			DEVC_ID = rs.getString(3);
			LENGTH = rs.getString(4);
			FAN_ID = rs.getString(5);
			AREA = rs.getString(6);
			TYPE_ID = rs.getString(7);
		}
		editorText.appendText("&HVAC" + "\n");
		appendToEditorString("ID", ID);
		appendToEditorNumber("ROUGHNESS", ROUGHNESS);
		appendToEditorString("DEVC_ID", DEVC_ID);
		appendToEditorNumber("LENGTH", LENGTH);
		appendToEditorString("FAN_ID", FAN_ID);
		appendToEditorNumber("AREA", AREA);
		appendToEditorString("TYPE_ID", TYPE_ID);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the HOLE namelist.
     * @throws SQLException
     */
    public void printValuesHole() throws SQLException { //hole
    	String sqlHole = "SELECT * FROM hole";
    	String MESH_ID = "";
    	String MULT_ID = "";
    	String DEVC_ID = "";
    	String CTRL_ID = "";
    	String XB = "";
		ResultSet rs = getStatement().executeQuery(sqlHole);
		while (rs.next()) {
			MESH_ID = rs.getString(1);
			MULT_ID = rs.getString(2);
			DEVC_ID = rs.getString(3);
			CTRL_ID = rs.getString(4);
			XB = rs.getString(5);
		}
		editorText.appendText("&HOLE" + "\n");
		appendToEditorString("MESH_ID", MESH_ID);
		appendToEditorString("MULT_ID", MULT_ID);
		appendToEditorString("DEVC_ID", DEVC_ID);
		appendToEditorString("CTRL_ID", CTRL_ID);
		appendToEditorNumber("XB", XB);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the MISC namelist.
     * @throws SQLException
     */
    public void printValuesMisc() throws SQLException { //misc
    	String sqlMisc = "SELECT * FROM misc";
    	String NOISE = "";
    	String FREEZE_VELOCITY = "";
    	String HUMIDITY = "";
    	String Y_CO2_INFNTY = "";
    	String TMPA = "";
    	String GVEC = "";
		ResultSet rs = getStatement().executeQuery(sqlMisc);
		while (rs.next()) {
			NOISE = rs.getString(1);
			FREEZE_VELOCITY = rs.getString(2);
			HUMIDITY = rs.getString(3);
			Y_CO2_INFNTY = rs.getString(4);
			TMPA = rs.getString(5);
			GVEC = rs.getString(6);
		}
		editorText.appendText("&MISC" + "\n");
		appendToEditorBoolean("NOISE", NOISE);
		appendToEditorBoolean("FREEZE_VELOCITY", FREEZE_VELOCITY);
		appendToEditorNumber("HUMIDITY", HUMIDITY);
		appendToEditorNumber("Y_CO2_INFTY", Y_CO2_INFNTY);
		appendToEditorNumber("TMPA", TMPA);
		appendToEditorNumber("GVEC", GVEC);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the TRNX namelist.
     * @throws SQLException
     */
    public void printValuesTrnx() throws SQLException { //trnx
    	//get the number of TRNX lines
    	String sqlTrnx = "SELECT mainID FROM trnx GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlTrnx);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each TRNX line
	    	String trnxArray[] = new String[4];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlTrnx = "SELECT * FROM trnx WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlTrnx);
	        	while (rs.next()) {
	        		trnxArray[0] = rs.getString(2);
	        		trnxArray[1] = rs.getString(3);
	        		trnxArray[2] = rs.getString(4);
	        		trnxArray[3] = rs.getString(5);
	        	}
	        	if (isArrayFilled(trnxArray)) {
	        		editorText.appendText("&TRNX" + "\n");
		        	appendToEditorString("ID", trnxArray[0]);
		        	appendToEditorNumber("MESH_NUMBER", trnxArray[1]);
		        	appendToEditorNumber("CC", trnxArray[2]);
		        	appendToEditorNumber("PC", trnxArray[3]);
		        	editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the BNDF namelist.
     * @throws SQLException
     */
    public void printValuesBndf() throws SQLException { //bndf
    	//get the number of BNDF lines
    	String sqlBndf = "SELECT mainID FROM bndf GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlBndf);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each BNDF line
	    	String bndfArray[] = new String[1];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlBndf = "SELECT * FROM bndf WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlBndf);
	        	while (rs.next()) {
	        		bndfArray[0] = rs.getString(2);
	        	}
	        	if (isArrayFilled(bndfArray)) {
	        		editorText.appendText("&BNDF" + "\n");
		        	appendToEditorString("ID", bndfArray[0]);
		        	editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the CATF namelist.
     * @throws SQLException
     */
    public void printValuesCatf() throws SQLException { //catf
    	String sqlCatf = "SELECT * FROM catf";
    	String OTHER_FILES = "";
		ResultSet rs = getStatement().executeQuery(sqlCatf);
		while (rs.next()) {
			OTHER_FILES = rs.getString(1);
		}
		String[] files = OTHER_FILES.split("\\n");
		String concat_OTHER_FILES = "";
		for (int i=0; i<files.length; i++) {
			if (files.length == 1){ //if only 1 file
				concat_OTHER_FILES = "'" + files[i] + "'";
			}
			else if (i == 0){ //for the first file
				concat_OTHER_FILES = "'" + files[i] + "', ";
			}
			else if (i <= files.length - 2){ //until the second last file
				concat_OTHER_FILES = concat_OTHER_FILES + "'" + files[i] + "', ";
			}
			else{ //for the last file
				concat_OTHER_FILES = concat_OTHER_FILES + "'" + files[i] + "'";
			}
		}
		editorText.appendText("&CATF" + "\n");
		appendToEditorNumber("OTHER_FILES", concat_OTHER_FILES);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the PROF namelist.
     * @throws SQLException
     */
    public void printValuesProf() throws SQLException { //prof
    	String sqlProf = "SELECT * FROM prof";
    	String ID = "";
    	String XYZ = "";
    	String QUANTITY = "";
    	String IOR = "";
		ResultSet rs = getStatement().executeQuery(sqlProf);
		while (rs.next()) {
			ID = rs.getString(1);
			XYZ = rs.getString(2);
			QUANTITY = rs.getString(3);
			IOR = rs.getString(4);
		}
		editorText.appendText("&PROF" + "\n");
		appendToEditorString("ID", ID);
		appendToEditorNumber("XYZ", XYZ);
		appendToEditorString("QUANTITY", QUANTITY);
		appendToEditorNumber("IOR", IOR);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the RAMP namelist.
     * @throws SQLException
     */
    public void printValuesRamp() throws SQLException { //ramp
    	//get the number of RAMP lines
    	String sqlRamp = "SELECT mainID FROM ramp GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlRamp);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each RAMP line
	    	String rampArray[] = new String[3];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlRamp = "SELECT * FROM ramp WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlRamp);
	        	while (rs.next()) {
	        		rampArray[0] = rs.getString(2);
	        		rampArray[1] = rs.getString(3);
	        		rampArray[2] = rs.getString(4);
	        	}
	        	if (isArrayFilled(rampArray)) {
	        		editorText.appendText("&RAMP" + "\n");
		        	appendToEditorNumber("F", rampArray[0]);
		        	appendToEditorNumber("T", rampArray[1]);
		        	appendToEditorString("ID", rampArray[2]);
		        	editorText.appendText("/" + "\n");
	        	}
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the CLIP namelist.
     * @throws SQLException
     */
    public void printValuesClip() throws SQLException { //clip
    	String sqlClip = "SELECT * FROM clip";
    	String MAXIMUM_DENSITY = "";
    	String MAXIMUM_TEMPERATURE = "";
    	String MINIMUM_DENSITY = "";
    	String MINIMUM_TEMPERATURE = "";
		ResultSet rs = getStatement().executeQuery(sqlClip);
		while (rs.next()) {
			MAXIMUM_DENSITY = rs.getString(1);
			MAXIMUM_TEMPERATURE = rs.getString(2);
			MINIMUM_DENSITY = rs.getString(3);
			MINIMUM_TEMPERATURE = rs.getString(4);
		}
		editorText.appendText("&CLIP" + "\n");
		appendToEditorNumber("MAXIMUM_DENSITY", MAXIMUM_DENSITY);
		appendToEditorNumber("MAXIMUM_TEMPERATURE", MAXIMUM_TEMPERATURE);
		appendToEditorNumber("MINIMUM_DENSITY", MINIMUM_DENSITY);
		appendToEditorNumber("MINIMUM_TEMPERATURE", MINIMUM_TEMPERATURE);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the COMB namelist.
     * @throws SQLException
     */
    public void printValuesComb() throws SQLException { //comb
    	String sqlComb = "SELECT * FROM comb";
    	String FIXED_MIX_TIME = "";
    	String EXTINCTION_MODEL = "";
		ResultSet rs = getStatement().executeQuery(sqlComb);
		while (rs.next()) {
			FIXED_MIX_TIME = rs.getString(1);
			EXTINCTION_MODEL = rs.getString(2);
		}
		editorText.appendText("&COMB" + "\n");
		appendToEditorNumber("FIXED_MIX_TIME", FIXED_MIX_TIME);
		appendToEditorString("EXTINCTION_MODEL", EXTINCTION_MODEL);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the DUMP namelist.
     * @throws SQLException
     */
    public void printValuesDump() throws SQLException { //dump
    	String sqlDump = "SELECT * FROM dump";
    	String MASS_FILE = "";
    	String SMOKE3D = "";
    	String NFRAMES = "";
    	String DT_DEVC = "";
		ResultSet rs = getStatement().executeQuery(sqlDump);
		while (rs.next()) {
			MASS_FILE = rs.getString(1);
			SMOKE3D = rs.getString(2);
			NFRAMES = rs.getString(3);
			DT_DEVC = rs.getString(4);
		}
		editorText.appendText("&DUMP" + "\n");
		appendToEditorBoolean("MASS_FILE", MASS_FILE);
		appendToEditorBoolean("SMOKE3D", SMOKE3D);
		appendToEditorNumber("NFRAMES", NFRAMES);
		appendToEditorNumber("DT_DEVC", DT_DEVC);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the WIND namelist.
     * @throws SQLException
     */
    public void printValuesWind() throws SQLException { //wind
    	String sqlWind = "SELECT * FROM wind";
    	String Z_0 = "";
    	String DIRECTION = "";
    	String L = "";
    	String SPEED = "";
		ResultSet rs = getStatement().executeQuery(sqlWind);
		while (rs.next()) {
			Z_0 = rs.getString(1);
			DIRECTION = rs.getString(2);
			L = rs.getString(3);
			SPEED = rs.getString(4);
		}
		editorText.appendText("&WIND" + "\n");
		appendToEditorNumber("Z_0", Z_0);
		appendToEditorNumber("DIRECTION", DIRECTION);
		appendToEditorNumber("L", L);
		appendToEditorNumber("SPEED", SPEED);
		editorText.appendText("/" + "\n");
    }
    
    /**
     * Print the values for the TABL namelist.
     * @throws SQLException
     */
    public void printValuesTabl() throws SQLException { //tabl
    	//get the number of TABL lines
    	String sqlTabl = "SELECT mainID FROM tabl GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlTabl);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each TABL line
	    	String tablArray[] = new String[2];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlTabl = "SELECT * FROM tabl WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlTabl);
	        	while (rs.next()) {
	        		tablArray[0] = rs.getString(2);
	        		tablArray[1] = rs.getString(3);
	        	}
	        	if (isArrayFilled(tablArray)) {
	        		editorText.appendText("&TABL" + "\n");
		        	appendToEditorString("ID", tablArray[0]);
		        	appendToEditorNumber("TABLE_DATA", tablArray[1]);
		        	editorText.appendText("/" + "\n");
	        	}
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Print the values for the ZONE namelist.
     * @throws SQLException
     */
    public void printValuesZone() throws SQLException {//zone
    	//get the number of ZONE lines
    	String sqlZone = "SELECT mainID FROM zone GROUP BY mainID";
    	ResultSet rs = getStatement().executeQuery(sqlZone);
    	String mainID = "";
    	while (rs.next()) {
    		mainID = rs.getString(1);
    	}
    	
    	try {
	    	//print each ZONE line
	    	String zoneArray[] = new String[1];
	    	for (int i=1; i<=Integer.parseInt(mainID); i++) {
	    		sqlZone = "SELECT * FROM zone WHERE mainID='" + i + "';";
	        	rs = getStatement().executeQuery(sqlZone);
	        	
	        	while (rs.next()) {
	        		zoneArray[0] = rs.getString(2);
	        	}
	        	if (isArrayFilled(zoneArray)) {
	        		editorText.appendText("&ZONE" + "\n");
		        	appendToEditorNumber("XYZ", zoneArray[0]);
		        	editorText.appendText("/" + "\n");
	        	}
	        	
	    	}
    	} catch(Exception e) {
    		System.out.println("Nothing to print");
    	}
    }
    
    /**
     * Check if the array is filled. This function is called before appending values to the editor.
     * If it returns false, nothing will be written for that particular namelist.
     * If it return true, the namelist parameters will be filled in.
     * @param tempArray The array to be checked.
     * @return Boolean on whether the array is filled.
     */
    public boolean isArrayFilled(String[] tempArray) {
    	for (int i=0; i<tempArray.length; i++) {
    		if (!tempArray[i].equals("")) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * The format for the printing of a String value. It requires single quotes around its value.
     * @param paramName The parameter name.
     * @param value The value for that parameter.
     */
    public void appendToEditorString(String paramName, String value) { //add single quotes for string
    	if (!value.equals("")) {
    		editorText.appendText("\t" + paramName + "='" + value + "'" + "\n");
    	}
    }
    
    /**
     * The format for the printing of a numeric value. No symbols / special characters required for numeric values.
     * @param paramName The parameter name.
     * @param value The value for that parameter.
     */
    public void appendToEditorNumber(String paramName, String value) { //no single quotes for numerical values
    	if (!value.equals("")) {
    		editorText.appendText("\t" + paramName + "=" + value + "\n");
    	}
    }
    
    /**
     * The format for the printing of a Boolean value. It requires the full stop punctuation around its value.
     * @param paramName The parameter name.
     * @param value The value for that parameter.
     */
    public void appendToEditorBoolean(String paramName, String value) { //.. for logical values
    	if (!value.equals("")) {
    		editorText.appendText("\t" + paramName + "=." + value + "." + "\n");
    	}
    }
    
    private Statement getStatement() throws SQLException {
    	ConnectionClass connectionClass = new ConnectionClass();
    	Connection connection = connectionClass.getConnection();
    	Statement statement = connection.createStatement();
    	return statement;
    }
}
