package gui;

import java.util.Optional;

import connectivity.ConnectionClass;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Values {
	
	//Store all the values that the user has initialised
	//ALl are static variables.
	
	protected static final int ARRAY = 14;
	protected static final String PATH = "C:\\Users\\dell\\Desktop\\";
	protected static String[] paramName = new String[ARRAY];
	protected static String[][] allStrings = new String[ARRAY][2];
	protected static FileWriter fw;
	protected static BufferedWriter bw = null;
	
	
	//HEAD 
	protected static String CHID = ""; //0
	protected static String TITLE = ""; //1
	
	//TIME
	protected static String T_END = ""; //2
	protected static String T_BEGIN = "";
	protected static String DT = ""; //4
	
	
	//INIT
	protected static String PART_ID= ""; //5
	protected static String SPEC_ID = ""; 
	protected static String N_PARTICLES= "";
	protected static String N_PARTICLES_PER_CELL= ""; 
	protected static String MASS_FRACTION = "";
	protected static String MASS_PER_TIME= "";
	protected static String MASS_PER_VOLUME= "";
	protected static String XB = ""; //12
		
	//CATF
	protected static String OTHER_FILES = ""; //13
	protected static String concatFiles = ""; //formatted already
	
	
	
	
	protected static void initValues(){
		
		//HEAD
		allStrings[0][0] = CHID;
		allStrings[0][1] = "HEAD";
		paramName[0] = "CHID";
		allStrings[1][0] = TITLE;
		allStrings[1][1] = "HEAD";
		paramName[1] = "TITLE";
		
		//TIME
		allStrings[2][0] = T_END;
		allStrings[2][1] = "TIME";
		paramName[2] = "T_END";
		allStrings[3][0] = T_BEGIN;
		allStrings[3][1] = "TIME";
		paramName[3] = "T_BEGIN";
		allStrings[4][0] = DT;
		allStrings[4][1] = "TIME";
		paramName[4] = "DT";
		
		//INIT
		allStrings[5][0] = PART_ID;
		allStrings[5][1] = "INIT";
		paramName[5] = "PART_ID";
		allStrings[6][0] = SPEC_ID;
		allStrings[6][1] = "INIT";
		paramName[6] = "SPEC_ID";
		allStrings[7][0] = N_PARTICLES;
		allStrings[7][1] = "INIT";
		paramName[7] = "N_PARTICLES";
		allStrings[8][0] = N_PARTICLES_PER_CELL;
		allStrings[8][1] = "INIT";
		paramName[8] = "N_PARTICLES_PER_CELL";
		allStrings[9][0] = MASS_FRACTION;
		allStrings[9][1] = "INIT";
		paramName[9] = "MASS_FRACTION";
		allStrings[10][0] = MASS_PER_TIME;
		allStrings[10][1] = "INIT";
		paramName[10] = "MASS_PER_TIME";
		allStrings[11][0] = MASS_PER_VOLUME;
		allStrings[11][1] = "INIT";
		paramName[11] = "MASS_PER_VOLUME";
		allStrings[12][0] = XB;
		allStrings[12][1] = "INIT";
		paramName[12] = "XB";
		
		
		//CATF
		allStrings[13][0] = OTHER_FILES;
		allStrings[13][1] = "CATF";
		paramName[13] = "OTHER_FILES";
		
		
		
	}
	
	protected static void cancelForm() throws SQLException{
//		for (int i=0; i<ARRAY; i++){
//			allStrings[i][0] = "";
//		}
		
		//delete the table
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
	}
	
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
	
	protected static void printFile() throws IOException{
		try{
			File outputFile = new File(PATH + allStrings[0][0] + ".fds");
			if (!outputFile.exists()){
				outputFile.createNewFile();
			}
			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
		} catch(Exception e){
			System.out.println("Error creating the file");
		}
		int headCount = 0;
		int timeCount = 0;
		int catfCount = 0;
		for (int i=0; i<ARRAY; i++){
			for (int j=0; j<2; j++){
				if (allStrings[i][j].equals("")){
					break;
				}
				else if (allStrings[i][1].equals("HEAD")){
					headCount++;
					if (headCount==1){
						bw.write("&" + allStrings[i][1]);
						printFileValues(0, 1); //Pass in the index values
					}
					
				}
				else if (allStrings[i][1].equals("TIME")){
					timeCount++;
					if (timeCount == 1){
						bw.write("&" + allStrings[i][1]);
						printFileValues(2, 4); //Pass in the index values
					}
				}
				else if(allStrings[i][1].equals("CATF")){
					catfCount++;
					if (catfCount == 1){
						bw.write("&" + allStrings[i][1]);
						printFileValues(13, 13); //Pass in the index values
					}
				}
			}
		}
		bw.write("&TAIL /");
		bw.close();
	}
	
	protected static void printFileValues(int startIndex, int endIndex) throws IOException{
		for (int i=startIndex; i<endIndex + 1; i++){
			if (!allStrings[i][0].equals("")){
				if (isNumber(allStrings[i][0])){
					//System.out.println("PRINTING NUMBER");
					bw.newLine();
					bw.write("\t" + paramName[i] + "=" + allStrings[i][0]);
				}
				else if (isBoolean(allStrings[i][0])){
					//System.out.println("PRINTING BOOLEAN");
					bw.newLine();
					bw.write("\t" + paramName[i] + "=" + "." + allStrings[i][0].toUpperCase() + ". ");
				}
				else{
					//System.out.println("PRINTING STRING");
					bw.newLine();
					bw.write("\t" + paramName[i] + "='" + allStrings[i][0] + "' ");
				}
				
			}
		}
		bw.write(" /");
		bw.newLine();
	}
	
	protected static boolean isNumber(String value){
		try{
			Float.valueOf(value);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	protected static boolean isInteger(String value){
		try{
			int intValue = Integer.parseInt(value);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	protected static boolean isBoolean(String value){
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
			return true;
		}
		return false;
	}
	
	protected static void checkPositiveFloatValue(TextField tempField) {
		
	}
}
