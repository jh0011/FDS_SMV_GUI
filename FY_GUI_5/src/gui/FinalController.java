package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLException;
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
				// Update batch file (single run)
		    	updateBatch();
		    	updatePythonScript();
		    	//Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
		    	Process p = Runtime.getRuntime().exec("cmd /c start fdsinit.bat");
			}
    	}
    	catch(Exception e) {
    		System.out.println("Something went wrong when opening the CMDfds shell. Ensure that NIST FDS-SMV simulator is installed.");
    	}
		
    	
    }
    
    public void updateBatch() throws IOException {
    	//update the batch file
    	FileWriter fw;
    	BufferedWriter bw = null;
    	File outputFile = new File("runFDS.bat");
    	
    	fw = new FileWriter(outputFile);
		bw = new BufferedWriter(fw);
		Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
		bw.write("cd " + EditorController.fileDirectory + "\n");
		//bw.write("C:\\Python27\\python " + path + "\\parFDS.py");
		bw.write("fds_local " + EditorController.CHID + ".fds\n");
		bw.close();
		
    }
    
    public void updatePythonScript() throws IOException {
    	//update parFDS.py
    	FileWriter fw;
    	File pythonScript = new File("parFDS.py");
    	Scanner sc = new Scanner(pythonScript);
    	StringBuffer buffer = new StringBuffer();
    	while (sc.hasNextLine()) {
    		buffer.append(sc.nextLine()+System.lineSeparator());
    	}
    	String fileContents = buffer.toString();
    	sc.close();
		fw = new FileWriter(pythonScript);
		String pythonFileDirectory = getDirectoryFormat(EditorController.fileDirectory);
		
		String oldContent = "input_file = 'C:\\\\Users\\\\dell\\\\Desktop\\\\couch_modified_try\\\\couch_modified.fds'";
		String newContent = "input_file = '" + pythonFileDirectory + "\\\\" + EditorController.CHID + ".fds'";
		if (fileContents.contains(oldContent)) {
			fileContents = fileContents.replace(oldContent, newContent);
		}
		fw.append(fileContents);
		fw.flush();
		fw.close();
		
		
		//update helper_functions.py
		FileWriter fw2;
    	File pythonScript2 = new File("helper_functions.py");
    	Scanner sc2 = new Scanner(pythonScript2);
    	StringBuffer buffer2 = new StringBuffer();
    	while (sc2.hasNextLine()) {
    		buffer2.append(sc2.nextLine()+System.lineSeparator());
    	}
    	String fileContents2 = buffer2.toString();
    	sc2.close();
    	
		fw2 = new FileWriter(pythonScript2);
		
		String oldContent2 = "with open('C:\\\\Users\\\\dell\\\\Desktop\\\\couch_modified_try\\\\couch_modified.fds', 'r') as f:";
		String newContent2 = "with open('" + pythonFileDirectory + "\\\\" + EditorController.CHID + ".fds', 'r') as f:";
		if (fileContents2.contains(oldContent2)) {
			fileContents2 = fileContents2.replace(oldContent2, newContent2);
		}
		fw2.append(fileContents2);
		fw2.flush();
		fw2.close();
		
    }
    
    public String getDirectoryFormat(File directory) {
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
    
    
    //exit
    
    //run in FDS (single run)
    //--> Get directory of CMDfds shortcut (dir1)
    //--> Run dir1 with extension .lnk to get the CMDfds shell
    //--> Directory of the .fds file (dir2)
    //--> cd to the directory
    //--> Execute fds_local command
    
    //run in FDS (multiple runs)
    //--> Get directory of CMDfds shortcut (dir1)
    //--> Run dir1 with extension .lnk to get the CMDfds shell
    //--> Directory of the .fds file (dir2)
    //--> cd to the directory
    //--> Execute the python script (parFDS.py)
    //--> Iterate through each sub-folder and execute fds_local command

}
