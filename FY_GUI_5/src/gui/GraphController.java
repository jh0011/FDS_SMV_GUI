package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

/**
 * Controller class for Graph.fxml
 * 
 */
public class GraphController implements Initializable{
	
    @FXML private Button exitBtn;
    
    @FXML private AreaChart<Number, Number> hrrStacked;
    
    private static ArrayList<Double> timeStepList = new ArrayList<Double>();
    private static ArrayList<Double> hrrpuvList = new ArrayList<Double>();
    private static ArrayList<Series> seriesList = new ArrayList<Series>();
    private static int caseNum = -1;
    public static ArrayList<Double> meanHrrList = new ArrayList<Double>();
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		timeStepList.clear();
		hrrpuvList.clear();
		seriesList.clear();
		meanHrrList.clear();
		caseNum = -1;
		
		findCSVfiles();
		plotGraph();
		
		
	}
	
	/**
	 * When the Exit button is clicked to exit the app
	 * @param event Exit button is clicked
	 * @throws SQLException If database access error
	 */
	@FXML
    public void exitApp(ActionEvent event) throws SQLException { //EXIT
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
	
	/**
	 * Go to the previous page (Final)
	 * @param event Back button is clicked
	 * @throws IOException If cannot display the page
	 */
	@FXML
    public void goToFinal(ActionEvent event) throws IOException { //PREVIOUS SCENE
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Final.fxml"));
		Parent root = loader.load();
		Scene finalScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(finalScene);
		mainWindow.show();
    }
	
	/**
     * Go to the next page (Analysis)
     * @param event Next button is clicked.
     * @throws IOException If cannot display the page
     */
	@FXML
    public void goToAnalysis(ActionEvent event) throws IOException { //NEXT SCENE
		takeSnapshot();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Analysis.fxml"));
		Parent root = loader.load();
		
		Scene analysisScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(analysisScene);
		mainWindow.show();
    }
	
	/**
	 * This function will update the listOfDirectories ArrayList to find the directories of the 
	 * _hrr.csv files that are generated after each .fds file is run in the FDS-SMV simulator.
	 */
	public void findCSVfiles() {
		int numDirectories = FinalController.listOfDirectories.size();
		
		for (int i=0; i<numDirectories; i++) {
			caseNum++;
			readCSVfiles(FinalController.listOfDirectories.get(i));
		}
	}
	
	/**
	 * This function reads the actual _hrr.csv file. The first column of the .csv file contains the time steps 
	 * while the second column of the .csv file contains the HRR values. The values from these columns are stored 
	 * in timeStepList and hrrpuvList respectively.
	 * @param csvPath The directory in which the _hrr.csv file is stored in.
	 */
	public void readCSVfiles(String csvPath) {
		// clear the 2 arraylists, as all the series use the same arraylists
		timeStepList.clear();
		hrrpuvList.clear();
		String csvFilePath = "";
		try {
			File root = new File(csvPath);
			File[] files = root.listFiles();
			for (File f : files) {
				if (f.getAbsoluteFile().toString().contains("_hrr.csv")) {
					csvFilePath = f.getAbsoluteFile().toString();
					break;
				}
			}
			String seriesName = "case_" + getCaseName(caseNum).toLowerCase();
			
			BufferedReader reader = new BufferedReader(new FileReader(csvFilePath)); 
			String line = "";
			int skipCounter = 0;
			while ((line = reader.readLine()) != null){  
				skipCounter++;
				if (skipCounter > 2) {
					String[] eachLine = line.split(","); 
					timeStepList.add(Double.parseDouble(eachLine[0]));
					hrrpuvList.add(Double.parseDouble(eachLine[1]));
				}
			}
			reader.close();
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			plotGraphValues(series, seriesName);
		} catch (IOException e) {
			System.out.println("Error while finding the _hrr CSV file. Not able to find the file. Not able to read the file.");
		}
	}
	
	/**
	 * This function is used to declare the values for the Series, based on the values from timeStepList and hrrpuvList. 
	 * Each Series is added to seriesList, and ArrayList of Series.
	 * @param series A new Series is created for each test case.
	 * @param seriesName The unique name given to each Series.
	 */
	public void plotGraphValues(Series series, String seriesName) {
		series.getData().clear();
		series.setName(seriesName);
		for (int i=0; i<timeStepList.size(); i++) {
			series.getData().add(new XYChart.Data(timeStepList.get(i).floatValue(), hrrpuvList.get(i).floatValue()));
		}
		setMeanHrr();
		seriesList.add(series);
	}
	
	/**
	 * This function is used to plot the graph based on all the Series.
	 */
	public void plotGraph() {
		Series[] seriesFinalList = new Series[seriesList.size()];
		for (int i=0; i<seriesList.size(); i++) {
			seriesFinalList[i] = seriesList.get(i);
		}
		hrrStacked.getData().clear();
		hrrStacked.getData().addAll(seriesFinalList);
	}
	
	/**
	 * This function is used to calculate the mean HRR values for each Series. The mean value is stored in meanHrrList,
	 * an ArrayList of double values.
	 */
	public void setMeanHrr() {
		double totalHrr = 0;
		double meanHrr = 0;
		int i = 0;
		for (i=0; i<timeStepList.size(); i++) {
			totalHrr += hrrpuvList.get(i);
		}
		double timeDiff = timeStepList.get(timeStepList.size() - 1) - timeStepList.get(0);
		double numTimeSteps = timeStepList.size();
		meanHrr = totalHrr / numTimeSteps;
		meanHrrList.add(meanHrr);
	}
	
	/**
	 * This function is used to take a snapshot of the Scene. The snapshot is saved as a .png file in the same directory as the .fds file 
	 * that the user originally saved the file in.
	 * @throws IOException If cannot display the page
	 */
	public void takeSnapshot() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Graph.fxml"));
		Parent root = loader.load();
		Scene graphScene = new Scene(root);
		
		SnapshotParameters param = new SnapshotParameters();
	    param.setDepthBuffer(true);
    	WritableImage writableImage = new WritableImage(870, 710); 
    	//hrrStacked.snapshot(param, writableImage);
    	graphScene.snapshot(writableImage);
    	
    	File file = new File(EditorController.fileDirectory.getPath() + "\\Graph-Snapshot.png"); 
    	
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            //System.out.println("Snapshot saved: " + file.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Error while taking a snapshot of the graph");
        }
    }
	
	/**
     * This function is used to convert the index to an alphabet. For example, 0 is a, 1 is 2 etc.
     * @param i The index value.
     * @return The alphabet.
     */
    public String getCaseName(int i) {
    	int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);
        if( quot == 0 ) {
            return ""+letter;
        } 
        else {
            return getCaseName(quot-1) + letter;
        }
    }
	

}
