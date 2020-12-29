package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FinalController implements Initializable{
	
	@FXML Button exitBtn;
	static int numOfFiles = 0;
	static ArrayList<String> listOfFiles = new ArrayList<String>();
	static ArrayList<String> listOfDirectories = new ArrayList<String>();
	static Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
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
    public void runFDS(ActionEvent event) throws SQLException, IOException {
    	try {
	    	Alert runAlert = new Alert(Alert.AlertType.CONFIRMATION);
			runAlert.setTitle("Run in FDS");
			runAlert.setContentText("Clicking on OK would open the CMDfds shell. Type 'runFDS.bat' into "
					+ "the command line interface to run the batch file - which would then run the input file.");
			runAlert.setHeaderText(null);
			Optional<ButtonType> userOption = runAlert.showAndWait();
			if (userOption.get() == ButtonType.OK){
				// Update batch files
				updateRunPythonBatch(); 
				updateRunFDSBatch();
				
				//update python script
		    	updatePythonScript();
		    	
		    	
		    	Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \""
		    			+ "fdsinit.bat && cd " + path + "\\batch-files && runFDS.bat\"");
			}
    	}
    	catch(Exception e) {
    		System.out.println("Something went wrong when opening the CMDfds shell. Ensure that NIST FDS-SMV simulator is installed.");
    	}
		
    }

    
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
			
			//run the runPython.bat
			Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + path + "\\batch-files\\runPython.bat\"");  //&& exit
			Thread.sleep(20000); //sleep for 20s to allow the runPython.bat to execute
    	}catch(Exception e) {
    		System.out.println("Error while updating runPython.bat");
    	}
    }
    
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

    
    public void updatePythonScript() throws IOException {
    	//update parFDS.py
    	FileWriter fw;
    	File pythonScript = new File(path + "\\python-scripts\\parFDS.py");
    	System.out.println("Python script directory: " + pythonScript.getAbsolutePath());
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
		
    }
    
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
	    				listOfFiles.add(f.getAbsoluteFile().toString()); //update the list of directories to traverse 
	    			}
	    		}
	    	}
    	}catch (Exception e){
    		System.out.println("Error while traversing for input files.");
    	}
    }
    
    
    
    
    //update runPython file
    //run runPython.bat
    //create batch files for each input file
    //update runFDS file
    //run runFDS.bat

}
