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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class GraphController implements Initializable{
	
    @FXML private Button exitBtn;
    
    @FXML private AreaChart<Number, Number> hrrStacked;
    
    private static ArrayList<Double> timeStepList = new ArrayList<Double>();
    private static ArrayList<Double> hrrpuvList = new ArrayList<Double>();
    private static ArrayList<Series> seriesList = new ArrayList<Series>();
    public static ArrayList<Double> meanHrrList = new ArrayList<Double>();
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		timeStepList.clear();
		hrrpuvList.clear();
		seriesList.clear();
		meanHrrList.clear();
		
		findCSVfiles();
		plotGraph();
		
		//trialPlot();
		
	}
	
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
	
	@FXML
    public void goToAnalysis(ActionEvent event) throws IOException { //NEXT SCENE
		takeSnapshot();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Analysis.fxml"));
		Parent root = loader.load();
		
		AnalysisController analysisCont = loader.getController(); //Get the next page's controller
		Scene analysisScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(analysisScene);
		mainWindow.show();
    }
	
	public void findCSVfiles() {
		int numDirectories = FinalController.listOfDirectories.size();
		
		
		for (int i=0; i<numDirectories; i++) {
			readCSVfiles(FinalController.listOfDirectories.get(i));
		}
	}
	
	public void readCSVfiles(String csvPath) {
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
			String seriesName = csvPath.substring(csvPath.length() - 6);
			
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
	
	public void plotGraphValues(Series series, String seriesName) {
		series.getData().clear();
		series.setName(seriesName);
		for (int i=0; i<timeStepList.size(); i++) {
			series.getData().add(new XYChart.Data(timeStepList.get(i).floatValue(), hrrpuvList.get(i).floatValue()));
		}
		getMeanHrr();
		seriesList.add(series);
	}
	
	public void plotGraph() {
		Series[] seriesFinalList = new Series[seriesList.size()];
		for (int i=0; i<seriesList.size(); i++) {
			seriesFinalList[i] = seriesList.get(i);
		}
		hrrStacked.getData().clear();
		hrrStacked.getData().addAll(seriesFinalList);
	}
	
	public void getMeanHrr() {
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
	
	public void takeSnapshot() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Graph.fxml"));
		Parent root = loader.load();
		Scene graphScene = new Scene(root);
		
    	WritableImage writableImage = new WritableImage(870, 710);
    	graphScene.snapshot(writableImage);
    	
    	File file = new File(EditorController.fileDirectory.getPath() + "\\Graph-Snapshot.png"); 
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            //System.out.println("Snapshot saved: " + file.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Error while taking a snapshot of the graph");
        }
    }
	
	public void trialPlot() {
		ArrayList<Series> seriesArrayList = new ArrayList<Series>();
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("Series 1");
		series.getData().add(new XYChart.Data(1, 1));
		series.getData().add(new XYChart.Data(3, 3));
		series.getData().add(new XYChart.Data(5, 35));
		series.getData().add(new XYChart.Data(7, 7));
		series.getData().add(new XYChart.Data(9, 9));
		seriesArrayList.add(series);
		
		XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();
		series2.setName("Series 1");
		series2.getData().add(new XYChart.Data(2.2, 10));
		series2.getData().add(new XYChart.Data(3, 30));
		series2.getData().add(new XYChart.Data(5, 25));
		series2.getData().add(new XYChart.Data(7, 33));
		series2.getData().add(new XYChart.Data(9, 29));
		seriesArrayList.add(series2);
		
		Series[] seriesFinalList = new Series[seriesArrayList.size()];
		for (int i=0; i<seriesArrayList.size(); i++) {
			seriesFinalList[i] = seriesArrayList.get(i);
			System.out.println(seriesArrayList.get(i).getData());
		}
		hrrStacked.getData().clear();
		hrrStacked.getData().addAll(seriesFinalList);
		
		
//		for(XYChart.Data data : series.getData()) {
//			System.out.println(series.getData());
//			//System.out.println(hrrStacked.getXAxis().toNumericValue(hrrStacked.getXAxis().getValueForDisplay(1)));
//			System.out.println((Number)data.getYValue());
//		}	
	}
	
	//For each input file
		//Find _hrr.csv file
		//Get time steps
		//Get HRRPUV
		//Plot the graph
		//Take a snapshot of the graph before Analysis.fxml

}
