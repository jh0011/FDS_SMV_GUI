package gui;

import java.util.Optional;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Values {
	
	//Store all the values that the user has initialised
	//ALl are static variables.
	
	protected static final int ARRAY = 16;
	protected static final String PATH = "C:\\Users\\dell\\Desktop\\";
	protected static String[] paramName = new String[ARRAY];
	protected static String[][] allStrings = new String[ARRAY][2];
	protected static FileWriter fw;
	protected static BufferedWriter bw = null;
	
	
	//HEAD 
	protected static String CHID = ""; //0
	protected static String TITLE = "";
	
	//TIME
	protected static String T_END = ""; //2
	protected static String T_BEGIN = "";
	protected static String DT = "";
	protected static String DT_END_FILL = "";
	protected static String DT_END_MINIMUM = "";
	protected static String EVAC_DT_FLOWFIELD = "";
	protected static String EVAC_DT_STEADY_STATE = "";
	protected static String LIMITING_DT_RATIO = "";
	protected static String LOCK_TIME_STEP = "";
	protected static String RESTRICT_TIME_STEP = "";
	protected static String TIME_SHRINK_FACTOR = "";
	protected static String WALL_INCREMENT = "";
	protected static String WALL_INCREMENT_HT3D = "";
	
	//CATF
	protected static String OTHER_FILES = ""; //15
	
	
	
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
		allStrings[5][0] = DT_END_FILL;
		allStrings[5][1] = "TIME";
		paramName[5] = "DT_END_FILL";
		allStrings[6][0] = DT_END_MINIMUM;
		allStrings[6][1] = "TIME";
		paramName[6] = "DT_END_MINIMUM";
		allStrings[7][0] = EVAC_DT_FLOWFIELD;
		allStrings[7][1] = "TIME";
		paramName[7] = "EVAC_DT_FLOWFIELD";
		allStrings[8][0] = EVAC_DT_STEADY_STATE;
		allStrings[8][1] = "TIME";
		paramName[8] = "EVAC_DT_STEADY_STATE";
		allStrings[9][0] = LIMITING_DT_RATIO;
		allStrings[9][1] = "TIME";
		paramName[9] = "LIMITING_DT_RATIO";
		allStrings[10][0] = LOCK_TIME_STEP;
		allStrings[10][1] = "TIME";
		paramName[10] = "LOCK_TIME_STEP";
		allStrings[11][0] = RESTRICT_TIME_STEP;
		allStrings[11][1] = "TIME";
		paramName[11] = "RESTRICT_TIME_STEP";
		allStrings[12][0] = TIME_SHRINK_FACTOR;
		allStrings[12][1] = "TIME";
		paramName[12] = "TIME_SHRINK_FACTOR";
		allStrings[13][0] = WALL_INCREMENT;
		allStrings[13][1] = "TIME";
		paramName[13] = "WALL_INCREMENT";
		allStrings[14][0] = WALL_INCREMENT_HT3D;
		allStrings[14][1] = "TIME";
		paramName[14] = "WALL_INCREMENT_HT3D";
		
		//CATF
		allStrings[15][0] = OTHER_FILES;
		allStrings[15][1] = "CATF";
		paramName[15] = "OTHER_FILES";
		
	}
	
	protected static void cancelForm(){
		for (int i=0; i<ARRAY; i++){
			allStrings[i][0] = "";
		}
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
						printFileValues(0, 2); //Pass in the index values
					}
					
				}
				else if (allStrings[i][1].equals("TIME")){
					timeCount++;
					if (timeCount == 1){
						bw.write("&" + allStrings[i][1]);
						printFileValues(2, 14); //Pass in the index values
					}
				}
				else if(allStrings[i][1].equals("CATF")){
					catfCount++;
					if (catfCount == 1){
						bw.write("&" + allStrings[i][1]);
						printFileValues(15, 15); //Pass in the index values
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
}
