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
	
	protected static final int ARRAY = 7;
	protected static String[] paramName = new String[ARRAY];
	protected static String[][] allStrings = new String[ARRAY][2];
	protected static FileWriter fw;
	protected static BufferedWriter bw = null;
	
	
	//HEAD 
	protected static String CHID = "";
	protected static String TITLE = "";
	
	//TIME
	protected static String T_END = "";
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
	protected static String files = "";
	
	
	
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
		/*allStrings[7][0] = T_END;
		allStrings[8][0] = T_END;
		allStrings[9][0] = T_END;
		allStrings[10][0] = T_END;
		allStrings[11][0] = T_END;
		allStrings[12][0] = T_END;*/
	}
	
	protected static void cancelForm(){
		/*chid = "";
		title = "";
		T_END = "";
		DT = "";
		DT_END_FILL = "";
		DT_END_MINIMUM = "";
		EVAC_DT_FLOWFIELD = "";
		EVAC_DT_STEADY_STATE = "";
		LIMITING_DT_RATIO = "";
		LOCK_TIME_STEP = "";
		RESTRICT_TIME_STEP = "";
		T_BEGIN = "";
		TIME_SHRINK_FACTOR = "";
		WALL_INCREMENT = "";
		WALL_INCREMENT_HT3D = "";
		files = "";*/
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
			File outputFile = new File("C:\\Users\\dell\\Desktop\\" + allStrings[0][0] + ".fds");
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
		for (int i=0; i<ARRAY; i++){
			for (int j=0; j<2; j++){
				if (allStrings[i][j].equals("")){
					break;
				}
				else if (allStrings[i][1].equals("HEAD")){
					headCount++;
					if (headCount==1){
						bw.write("&" + allStrings[i][1]);
						printFileValues(0, 2);
					}
					
				}
				else if (allStrings[i][1].equals("TIME")){
					timeCount++;
					if (timeCount == 1){
						bw.write("&" + allStrings[i][1]);
						printFileValues(2, 7);
					}
				}
			}
		}
		
		bw.close();
		
		
	}
	
	protected static void printFileValues(int startIndex, int endIndex) throws IOException{
		for (int i=startIndex; i<endIndex; i++){
			if (!allStrings[i][0].equals("")){
				if (isNumber(allStrings[i][0])){
					bw.newLine();
					bw.write("\t" + paramName[i] + "=" + allStrings[i][0] + ". ");
				}
				else{
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
	
}
