package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controller class for Analysis.fxml
 */
public class AnalysisController implements Initializable{
	
	@FXML private Button finalBackBtn;
    @FXML private Button exitBtn;
    private static String filename = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
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
     * Go to the previous page (Graph)
     * @param event Back button is clicked.
     * @throws IOException If page cannot be displayed
     */
    @FXML
    public void goToGraph(ActionEvent event) throws IOException { //PREVIOUS SCENE
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Graph.fxml"));
		Parent root = loader.load();
		GraphController graphCont = loader.getController(); //Get the next page's controller
		Scene graphScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(graphScene);
		mainWindow.show();
    }
    
    /**
     * This function is used to download the analysis report (FdsWare.pdf). The report is saved in the same directory as the .fds file 
	 * that the user originally saved the file in.
     * @param event The "Download" button is clicked.
     */
    @FXML
    public void downloadReport(ActionEvent event) {
    	writeToPdf();
    	File pdfFile = new File(EditorController.fileDirectory.getPath() + "\\FdsWare.pdf");
    	if (pdfFile.exists()) {
    		Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
			trnxAlert.setTitle("Successful file creation");
			trnxAlert.setContentText("FdsWare analysis report has been created in directory: " + EditorController.fileDirectory.getPath());
			trnxAlert.setHeaderText(null);
			ImageView icon = new ImageView("Fire2.jpg");
			icon.setFitHeight(48);
	        icon.setFitWidth(48);
	        trnxAlert.getDialogPane().setGraphic(icon);
			trnxAlert.show();
    	}
    	else {
    		Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
			trnxAlert.setTitle("Unsuccessful file creation");
			trnxAlert.setContentText("Not able to create FdsWare analysis report in directory: " + EditorController.fileDirectory.getPath());
			trnxAlert.setHeaderText(null);
			ImageView icon = new ImageView("Fire2.jpg");
			icon.setFitHeight(48);
	        icon.setFitWidth(48);
	        trnxAlert.getDialogPane().setGraphic(icon);
			trnxAlert.show();
    	}
    }
    
    /**
     * This function is used to round a value to 2 decimal places.
     * @param value The double value that needs to be rounded to 2 decimal places.
     * @return The rounded off value.
     */
    public double round2dp(double value) {
    	return Math.round(value * 100.0) / 100.0;
    }
    
    /**
     * This function is used to format all the mean HRR values comma-separated within square brackets. The meanHrrList is referenced 
     * from GraphController.java.
     * @return The formatted string.
     */
    public String formatMeanHRR() {
    	String formattedMeanHrr = "";
    	int hrrListSize = GraphController.meanHrrList.size();
    	for (int i=0; i<hrrListSize; i++) {
    		if (hrrListSize == 1) { //only 1 element
    			formattedMeanHrr += "[ " + round2dp(GraphController.meanHrrList.get(i)) + " ]";
    		}
    		else if (i == 0) { //first element
    			formattedMeanHrr += "[ " + round2dp(GraphController.meanHrrList.get(i)) + ", ";
    		}
    		else if (i == hrrListSize - 1) { //last element
    			formattedMeanHrr += round2dp(GraphController.meanHrrList.get(i)) + " ]";
    		}
    		else { //all other middle elements
    			formattedMeanHrr += round2dp(GraphController.meanHrrList.get(i)) + ", ";
    		}
    	}
    	//System.out.println("Formatted mean hrr list: " + formattedMeanHrr);
    	return formattedMeanHrr;
    }
    
    /**
     * This function is used to find the minimum mean HRR value.
     * @return The minimum mean HRR, rounded off to 2 dp.
     */
    public double compareMeanHrr() {
    	double minMeanHrr = GraphController.meanHrrList.get(0);
    	for (int i=0; i<GraphController.meanHrrList.size(); i++) {
    		if (GraphController.meanHrrList.get(i) < minMeanHrr) {
    			minMeanHrr = GraphController.meanHrrList.get(i);
    		}
    	}
    	//System.out.println("MIN MEAN HRR: " + minMeanHrr);
    	return round2dp(minMeanHrr);
    }
    
    /**
     * Within the meanHrrList, this function is used to get the index of the minimum mean HRR value.
     * @return The index of the ArrayList.
     */
    public int getMinIndex() {
    	double minMeanHrr = GraphController.meanHrrList.get(0);
    	int z = 0;
    	for (int i=0; i<GraphController.meanHrrList.size(); i++) {
    		if (GraphController.meanHrrList.get(i) < minMeanHrr) {
    			z = i;
    			minMeanHrr = GraphController.meanHrrList.get(i);
    		}
    	}
    	return z;
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
    
    /**
     * This function writes to the FdsWare.pdf analysis report.
     */
    public void writeToPdf() {
    	String FILE_NAME = EditorController.fileDirectory.getPath() + "\\FdsWare.pdf"; 
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));
            document.open();
            
            pdfHeader(document);
            pdfIntro(document);
            pdfMinimum(document);
            document.newPage();
            
            document.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * This function writes the header into the pdf.
     * @param document The FdsWare.pdf
     * @throws DocumentException If pdf document cannot be opened.
     * @throws IOException If file cannot be opened
     */
    public void pdfHeader(Document document) throws DocumentException, IOException {
    	Font f = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.ORANGE);
        
        String para1 = "FdsWare Analysis\n\n";
    	Paragraph p = new Paragraph(para1, f);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        
        
        Image image = Image.getInstance(EditorController.fileDirectory.getPath() + "\\Graph-Snapshot.png");
        image.scalePercent(40f);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
    }
    
    /**
     * This function writes the intro into the pdf.
     * @param document The FdsWare.pdf
     * @throws DocumentException If pdf document cannot be opened.
     */
    public void pdfIntro(Document document) throws DocumentException {
    	Font f = new Font(Font.FontFamily.TIMES_ROMAN);
        f.setStyle(Font.NORMAL);
        f.setSize(11);
        
    	String para1 = "\n\nFdsWare is a tool which can be used to generate .fds input file for the NIST FDS-SMV simulator tool. FdsWare is also"
    			+ " able to take into account multiple runs with a single input file, by using the required syntax, such as SWEEP or LIST. "
    			+ "Doing multiple runs is built upon the FDS-SMV's third-party tool, ParFDS.\n";
    	Paragraph p = new Paragraph(para1, f);
    	
    	String para3 = "After analysis of all the test cases generated, the mean HRR of the case(s) are " + formatMeanHRR() + " respectively.\n\n";
    	Paragraph p3 = new Paragraph(para3, f);
    	
        document.add(p);
        document.add(p3);
    }
    
    /**
     * This function writes the minimum value + ending into the pdf.
     * @param document The FdsWare.pdf
     * @throws DocumentException If pdf document cannot be opened.
     */
    public void pdfMinimum(Document document) throws DocumentException {
    	Font f = new Font(Font.FontFamily.TIMES_ROMAN);
        f.setStyle(Font.BOLD);
        f.setSize(11);
        
        Font f2 = new Font(Font.FontFamily.TIMES_ROMAN);
        f2.setStyle(Font.NORMAL);
        f2.setSize(11);
        
        String para1 = "Lowest mean Heat Release Rate (HRR)";
        Paragraph p1 = new Paragraph(para1, f);
        
        filename = "case_" + getCaseName(getMinIndex()).toLowerCase() + ".fds";
        String para2 = "Case: " + filename + "\nValue: " + compareMeanHrr() + 
        		"\nDirectory: " + EditorController.fileDirectory.getPath() + "\\input_files\\" + filename;
        Paragraph p2 = new Paragraph(para2, f2);
        
        String para3 = "\nFor the analysis of the spread of fire, the optimum test case would be the one with the lowest mean HRR values. "
    			+ "The mean HRR values are calculated by taking the sum of the HRR at each time value and dividing it across the"
    			+ " number of time steps.\n\n";
        String para4 = "\n\nThank you for using FdsWare to generate the .fds input file(s). \nGitHub repository: https://github.com/jh0011/FDS_SMV_GUI\n"
        		+ "FdsWare website: https://fds-gui-download.herokuapp.com/";
        Paragraph p3 = new Paragraph(para3, f2);
        Paragraph p4 = new Paragraph(para4, f);
        p4.setAlignment(Element.ALIGN_CENTER);
        
        document.add(p1);
        document.add(p2);
        document.add(p3);
        document.add(p4);
    }
    
}
