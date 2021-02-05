package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class GraphController implements Initializable{
	
	@FXML private LineChart<?, ?> hrrpuvLine;
    @FXML private CategoryAxis x;
    @FXML private NumberAxis y;
    @FXML private Button exitBtn;
    
    private static ArrayList<Double> timeStepList = new ArrayList<Double>();
    private static ArrayList<Double> hrrpuvList = new ArrayList<Double>();
    private static ArrayList<Series> seriesList = new ArrayList<Series>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		findCSVfiles();
		plotGraph();
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
    public void goToFinal(ActionEvent event) throws IOException { //PREVIOUS SCENE
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Final.fxml"));
		Parent root = loader.load();
		
		FinalController finalCont = loader.getController(); //Get the next page's controller
		Scene finalScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(finalScene);
		mainWindow.show();
    }
	
	public void findCSVfiles() {
		for (int i=0; i<FinalController.listOfDirectories.size(); i++) {
			readCSVfiles(FinalController.listOfDirectories.get(i));
		}
	}
	
	public void readCSVfiles(String csvPath) {
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
			
			
			BufferedReader reader = new BufferedReader(new FileReader(csvFilePath)); //CHANGE THE FILE NAME //csvPath + "\\pressure_boundary_hrr.csv"
			String line = "";
			int skipCounter = 0;
			timeStepList.clear();
			hrrpuvList.clear();
			while ((line = reader.readLine()) != null){  
				skipCounter++;
				if (skipCounter > 2) {
					String[] eachLine = line.split(","); 
					timeStepList.add(Double.parseDouble(eachLine[0]));
					hrrpuvList.add(Double.parseDouble(eachLine[1]));
				}
			}
			reader.close();
			XYChart.Series series = new XYChart.Series();
			System.out.println("New series here");
			plotGraphValues(series);
		} catch (IOException e) {
			System.out.println("Error while finding the _hrr CSV file. Not able to find the file. Not able to read the file.");
		}
	}
	
	public void plotGraphValues(Series series) {
		//XYChart.Series series = new XYChart.Series();
		for (int i=0; i<timeStepList.size(); i++) {
			series.getData().add(new XYChart.Data(Double.toString(timeStepList.get(i)), hrrpuvList.get(i).floatValue()));
		}
		
		seriesList.add(series);
	}
	
	public void plotGraph() {
		System.out.println("Plotting the actual graph");
		Series[] seriesFinalList = new Series[seriesList.size()];
		for (int i=0; i<seriesList.size(); i++) {
			seriesFinalList[i] = seriesList.get(i);
		}
		
		hrrpuvLine.getData().addAll(seriesFinalList);
	}
	
	//For each input file
		//Find _hrr.csv file
		//Get time steps
		//Get HRRPUV
		//Plot the graph

}
