package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class FinalController implements Initializable{
	
	/**
	 * Flow of the program: 
	 * <p>
	 * reset Python scripts to the default values and delete other batch files 
     * update Python scripts 
     * update runPython.bat file 
     * run runPython.bat 
     * create batch files for each input file 
     * update runFDS.bat file 
     * run runFDS.bat 
	 */
	
	@FXML Button exitBtn;
	static int numOfFiles = 0;
	static ArrayList<String> listOfFiles = new ArrayList<String>();
	static ArrayList<String> listOfDirectories = new ArrayList<String>();
	static Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set the python scripts to the default value
		resetPythonScripts();
		//delete previous batch files
		deleteBatchFiles();
	}
	
	@FXML
    public void exitApp(ActionEvent event) throws SQLException {
		Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
		cancelAlert.setTitle("Exit application");
		cancelAlert.setContentText("Clicking on OK would exit the application. Would you like to exit?");
		cancelAlert.setHeaderText(null);
		Optional<ButtonType> userOption = cancelAlert.showAndWait();
		if (userOption.get() == ButtonType.OK){
			Values.cancelForm();
			Stage stage = (Stage) exitBtn.getScene().getWindow();
		    stage.close();
		}
    }

    @FXML
    public void goToEditor(ActionEvent event) throws IOException { //PREVIOUS SCENE
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Editor.fxml"));
		Parent root = loader.load();
		
		EditorController editorCont = loader.getController(); //Get the next page's controller
		//editorCont.showInfo(); //Set the values of the page 
		Scene editorScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(editorScene);
		mainWindow.show();
    }
    
    @FXML
    public void goToGraph(ActionEvent event) throws IOException { //NEXT SCENE
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Graph.fxml"));
		Parent root = loader.load();
		
		GraphController graphCont = loader.getController(); //Get the next page's controller
		Scene graphScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(graphScene);
		mainWindow.show();
    }
    
    @FXML
    public void runFDS(ActionEvent event) throws SQLException, IOException {
    	try {
	    	Alert runAlert = new Alert(Alert.AlertType.CONFIRMATION);
			runAlert.setTitle("Run in FDS");
			runAlert.setContentText("Clicking on OK would analyse the saved input file and create multiple files"
					+ " if specified in the in-app editor. Then, the CMDfds shell would be opened to either do a single run"
					+ " or multiple runs, depending on the number of input files created.");
			runAlert.setHeaderText(null);
			Optional<ButtonType> userOption = runAlert.showAndWait();
			if (userOption.get() == ButtonType.OK){
				
				//update python script
		    	updatePythonScript();
				
				// Update batch files
				updateRunPythonBatch(); 
				runRunPythonBatch();
				updateRunFDSBatch();
				
				
		    	Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \""
		    			+ "fdsinit.bat && cd " + path + "\\batch-files && runFDS.bat\"");
			}
    	}
    	catch(Exception e) {
    		System.out.println("Something went wrong when opening the CMDfds shell. Ensure that NIST FDS-SMV simulator is installed.");
    	}
    }
    
    /**
     * The parFDS.py is reset to its default values based on the parFDS-default.py script.
     * The helper_functions.py is reset to its default values based on the helper_functions-default.py script.
     * <p>
     * The value that needs to be reset is the directory to the input file.
     * This ensures that every time the user clicks on "Run in FDS", the directory is always updated to the 
     * correct value. 
     * This function needs to be called before the input file can be run in FDS.
     */
    public void resetPythonScripts() {
    	try {
			//reset parFDS.py
			//read the file parFDS-default.py 
			//write the values into parFDS.py
	    	File pythonScript = new File(path + "\\python-scripts\\parFDS-default.py");
	    	File pythonScriptPar = new File(path + "\\python-scripts\\parFDS.py");
	    	Scanner sc = new Scanner(pythonScript);
	    	StringBuffer buffer = new StringBuffer();
	    	while (sc.hasNextLine()) {
	    		buffer.append(sc.nextLine()+System.lineSeparator());
	    	}
	    	String fileContents = buffer.toString();
	    	sc.close();
	    	
	    	FileWriter fw;
			fw = new FileWriter(pythonScriptPar);
			fw.append(fileContents);
			fw.close();
			
			//reset helper_functions.py
			//read the file helper_functions-default.py 
			//write the values into helper_functions.py
			File pythonScript2 = new File(path + "\\python-scripts\\helper_functions-default.py");
			File pythonScript2Helper = new File(path + "\\python-scripts\\helper_functions.py");
	    	Scanner sc2 = new Scanner(pythonScript2);
	    	StringBuffer buffer2 = new StringBuffer();
	    	while (sc2.hasNextLine()) {
	    		buffer2.append(sc2.nextLine()+System.lineSeparator());
	    	}
	    	String fileContents2 = buffer2.toString();
	    	sc2.close();
	    	
	    	FileWriter fw2;
			fw2 = new FileWriter(pythonScript2Helper);
			fw2.append(fileContents2);
			fw2.close();
			
		}catch(Exception e) {
			System.out.println("Error while setting default values of the python scripts.");
		}
    }

    /**
     * In certain cases, there are more than 1 batch file that is created due 
     * to multiple runs. This function deletes all the batch files except runFDS.bat and runPython.bat.\n
     * This function needs to be called before the input file can be run in FDS.
     */
    public void deleteBatchFiles() {
    	try {
	    	String batchDirectory = path + "\\batch-files";
	    	File root = new File(batchDirectory);
	    	File[] files = root.listFiles();
	    	for (File f : files) {
	    		if (!(f.getAbsoluteFile().toString().contains("runFDS.bat") || f.getAbsoluteFile().toString().contains("runPython.bat"))) {
	    			Files.deleteIfExists(f.toPath());
	    		}
	    	}
	    	String fileContents = "";
	    	FileWriter fw;
	    	File runFDS = new File(path + "\\batch-files\\runFDS.bat");
			fw = new FileWriter(runFDS);
			fw.append(fileContents);
			fw.close();
			
			FileWriter fw2;
	    	File runPython = new File(path + "\\batch-files\\runPython.bat");
			fw2 = new FileWriter(runPython);
			fw2.append(fileContents);
			fw2.close();
    	}catch(Exception e) {
    		System.out.println("Error while deleting the batch files.");
    	}
    }
    
    /**
     * This function updates parFDS.py and helper_functions.py with the directory of the input file. This function needs 
     * to be called before parFDS.py can be run in runRunPythonBatch().
     */
    public void updatePythonScript() {
    	try {
	    	//update parFDS.py
	    	FileWriter fw;
	    	File pythonScript = new File(path + "\\python-scripts\\parFDS.py");
	    	Scanner sc = new Scanner(pythonScript);
	    	StringBuffer buffer = new StringBuffer();
	    	while (sc.hasNextLine()) {
	    		buffer.append(sc.nextLine()+System.lineSeparator());
	    	}
	    	String fileContents = buffer.toString();
	    	sc.close();
			fw = new FileWriter(pythonScript);
			String pythonFileDirectory = getDirectoryFormat(EditorController.fileDirectory);
			
			String oldContent = "input_file = 'input_file'";
			String input_file = pythonFileDirectory + "\\\\" + EditorController.CHID + ".fds";
			String newContent = "input_file = '" + input_file + "'";
			//String newContent = "input_file = '" + pythonFileDirectory + "\\\\" + EditorController.CHID + ".fds'";
			if (fileContents.contains(oldContent)) {
				fileContents = fileContents.replace(oldContent, newContent);
			}
			fw.append(fileContents);
			fw.flush();
			fw.close();
			
			
			//update helper_functions.py
			FileWriter fw2;
	    	File pythonScript2 = new File(path + "\\python-scripts\\helper_functions.py");
	    	Scanner sc2 = new Scanner(pythonScript2);
	    	StringBuffer buffer2 = new StringBuffer();
	    	while (sc2.hasNextLine()) {
	    		buffer2.append(sc2.nextLine()+System.lineSeparator());
	    	}
	    	String fileContents2 = buffer2.toString();
	    	sc2.close();
	    	
			fw2 = new FileWriter(pythonScript2);
			
			String oldContent2 = "with open('input_file', 'r') as f:";
			String input_file2 = pythonFileDirectory + "\\\\" + EditorController.CHID + ".fds";
			//String newContent2 = "with open('" + pythonFileDirectory + "\\\\" + EditorController.CHID + ".fds', 'r') as f:";
			String newContent2 = "with open('" + input_file2 + "', 'r') as f:";
			if (fileContents2.contains(oldContent2)) {
				fileContents2 = fileContents2.replace(oldContent2, newContent2);
			}
			fw2.append(fileContents2);
			fw2.flush();
			fw2.close();
    	}catch(Exception e) {
    		System.out.println("Cannot find the python scripts.");
    	}
		
    }
    
    /**
     * This function is only used to update the runPython.bat. This batch file is used to change directory (cd) into 
     * the input file's directory. And then it executes the parFDS.py script. The actual running of the Python 
     * script does not occur in this function. 
     * <p>
     * Example of runPython.bat:\n
     * cd C:\Users\dell\Desktop\couch_folder 
     * C:\Python27\python C:\Users\dell\git\FDS_SMV_GUI\FY_GUI_5\python-scripts\parFDS.py
     */
    public void updateRunPythonBatch() {
    	try {
	    	//update the runPython batch file
	    	FileWriter fw2;
	    	BufferedWriter bw2 = null;
	    	File outputFile2 = new File(path + "\\batch-files\\runPython.bat");
	    	fw2 = new FileWriter(outputFile2);
	    	bw2 = new BufferedWriter(fw2);
	    	
			bw2.write("cd " + EditorController.fileDirectory + "\n"); //cd to the initial .fds file directory
			bw2.write("C:\\Python27\\python " + path + "\\python-scripts\\parFDS.py\n"); //execute the python script
			bw2.close();
			
			
			
    	}catch(Exception e) {
    		System.out.println("Error while updating runPython.bat");
    	}
    }
    
    /**
     * The runPython.bat file is called to run in this function. This function should be called 
     * after updateRunPythonBatch(). In this function a command prompt is opened up and the runPython.bat is called. 
     * After runPython.bat executes, the command prompt will exit. The execution of the current thread will be 
     * paused for 20s so that parFDS.py can have time to be executed.
     */
    public void runRunPythonBatch() {
    	//run the runPython.bat
		try {
			Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + path + "\\batch-files\\runPython.bat\""); // && exit
			Thread.sleep(20000); //sleep for 20s to allow the runPython.bat to execute
		} catch (Exception e) {
			System.out.println("Error while running runPython.bat");
		}  
		
    }
    
    /**
     * The runFDS.bat file is updated in this function. runFDS.bat is used to call the respective batch file for each 
     * input file.This function is only used to call other batch files, one after another. Before runFDS.bat can be updated 
     * createInputBatchFiles() should be called. Based on the input batch files, the runFDS.bat can be updated to call each of 
     * these input batch files. 
     * <p>
     * Example of runFDS.bat:\n
     * call "C:\Users\dell\git\FDS_SMV_GUI\FY_GUI_5\batch-files\batchFile1.bat"
     * call "C:\Users\dell\git\FDS_SMV_GUI\FY_GUI_5\batch-files\batchFile2.bat"
     */
    public void updateRunFDSBatch() {
    	try {
	    	//update the runFDS batch file
	    	FileWriter fw;
	    	BufferedWriter bw = null;
	    	File outputFile = new File(path + "\\batch-files\\runFDS.bat");
	    	fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
			
			traverse(EditorController.fileDirectory.getPath() + "\\input_files"); //find all the input files
			
			createInputBatchFiles(); //create the batch files for the input files
			
			for (int i=0; i<listOfDirectories.size(); i++) {
				if (listOfDirectories.size() == listOfFiles.size()) {
					String batchFileName = "batchFile" + (i+1);
					bw.write("call \"" + path + "\\batch-files\\" + batchFileName + ".bat\"" + "\n"); 
				}
				else {
					//YET TO IMPLEMENT
				}
			}
			bw.close();
    	} catch(Exception e) {
    		System.out.println("Error while updating runFDS.bat");
    	}
    }
    
    /**
     * This function creates one batch file for each input file that needs to be run in FDS. This is the batch 
     * file that sends the input file into the FDS. This batch file first cd into the directory of the actual input file 
     * and then calls fds_local to run the input file in FDS. 
     * <p>
     * Example of batchFile1.bat:\n
     * cd C:\Users\dell\Desktop\couch_4\input_files\case_a
	 * fds_local C:\Users\dell\Desktop\couch_4\input_files\case_a\case_a.fds
     */
    public void createInputBatchFiles() {
    	try {
	    	for (int i=0; i<listOfFiles.size(); i++) {
	    		if (listOfFiles.size() == listOfDirectories.size()) {
	    			String batchFileName = "batchFile" + (i+1);
	    			File createBatch = new File(path + "\\batch-files\\" + batchFileName + ".bat");
	    			createBatch.createNewFile();
	    			
	    			FileWriter fw;
	    	    	BufferedWriter bw = null;
	    	    	fw = new FileWriter(createBatch);
	    			bw = new BufferedWriter(fw);
	    			
	    			bw.write("cd " + listOfDirectories.get(i) + "\n"); //cd to the case_X directory
					bw.write("fds_local " + listOfFiles.get(i) + "\n"); //run FDS with the case_X.fds file
					
					bw.close();
	    		}
	    		else {
	    			//YET TO IMPLEMENT
	    		}
	    	}
    	} catch(Exception e) {
    		System.out.println("Error while creating the batch files for each .fds input file.");
    	}
    }

    /**
     * Formatting the directory string with double slashes rather than single slashes. This value will be used 
     * by updatePythonScript() to update the directory in the Python scripts.
     * @param directory The actual directory of the file.
     * @return String The formatted driectory of the file.
     */
    public String getDirectoryFormat(File directory) { //Directory with double slashes
    	String pythonFileDirectory = "";
    	try {
	    	String dirStr =  directory.getPath();
	    	String[] directoryArray = dirStr.split("\\\\");
	    	for (int i=0; i<directoryArray.length; i++) {
	    		if (i < directoryArray.length - 1) {
	    			pythonFileDirectory = pythonFileDirectory + directoryArray[i] + "\\\\";
	    		}
	    		else {
	    			pythonFileDirectory = pythonFileDirectory + directoryArray[i];
	    		}
	    	}
    	} catch(Exception e) {
    		System.out.println("Directory format error.");
    	}
		return pythonFileDirectory;
    }
    
    /**
     * This function is used to get a list of directories and paths to the input files. These values are added into 2 arraylists - 
     * listOfDirectories and listOfFiles respectively. 
     * @param path The "root" directory is the path to input_files (which is created after parFDS.py is run)
     */
    public void traverse(String path) {
    	try {
	    	File root = new File(path);
	    	File[] files = root.listFiles();
	    	for (File f : files) {
	    		if (f.isDirectory()) {
	    			traverse(f.getAbsolutePath());
					listOfDirectories.add(f.getAbsoluteFile().toString()); //update the list of directories to traverse
	    		}
	    		else {
	    			if (f.getAbsoluteFile().toString().contains(".fds")) {
	    				listOfFiles.add(f.getAbsoluteFile().toString()); //update the list of files to read
	    			}
	    		}
	    	}
    	}catch (Exception e){
    		System.out.println("Error while traversing for input files. Cannot find the input files. This could occur "
    				+ "if the Python script does not execute successfully.");
    	}
    }
}
