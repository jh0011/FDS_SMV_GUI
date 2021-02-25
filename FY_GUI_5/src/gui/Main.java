package gui;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import connectivity.ConnectionClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		try {
			Parent root;
			//root = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Should be the actual line
			root = FXMLLoader.load(getClass().getResource("Analysis.fxml")); //For testing a certain page
			Scene introScene = new Scene(root, 870, 710);
			primaryStage.setScene(introScene);
			primaryStage.setTitle("FdsWare");
			primaryStage.show();
			initDB();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initDB(){
		try {
			ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			
			//delete the values in the table
			Values.cancelForm();
			
			
			//insert an empty row
			String initHead = "INSERT INTO head(CHID, TITLE) VALUES ('', '');";
			String initTime = "INSERT INTO time (EndTime, StartTime, DT) VALUES ('', '', '');";
			String initCatf = "INSERT INTO catf(OTHER_FILES) VALUES ('');";
			String initInit = "INSERT INTO init(mainID, idText, partIdText, specIdText, npartText, "
					+ "npartCellText, massTimeText, massVolText, massFracText, xbText) "
					+ "VALUES ('1', '', '', '', '', '', '', '', '', '')";
			String initMesh = "INSERT INTO mesh (mainID, ijkText, xbText) VALUES ('1', '', '');";
			String initPart = "INSERT INTO part (mainID, SURF_ID, SPEC_ID, PROP_ID, QUANTITIES, STATIC" + 
					", MASSLESS, SAMPLING_FACTOR, DIAMETER, ID) VALUES ('1', '', '', '', '', '', '', '', '', '');";
			String initBndf = "INSERT INTO bndf (mainID, QUANTITY) VALUES ('1', '');";
			String initProp = "INSERT INTO prop (mainID, ID, PART_ID, QUANTITY, SMOKEVIEW_ID, OFFSET, PDPA_INTEGRATE, PDPA_NORMALIZE"
					+ ", OPERATING_PRESSURE, PARTICLES_PER_SECOND, PARTICLE_VELOCITY) VALUES ('1', '', '', '', '', '', '', '', '', '', '');";
			String initSpec = "INSERT INTO spec (mainID, ID, BACKGROUND) VALUES ('1', '', '');";
			String initDevc = "INSERT INTO devc (mainID, ID, PROP_ID, SPEC_ID, XYZ, QUANTITY, IOR, XB) VALUES ('1', '', '', '', '', '', '', '');";
			String initSlcf = "INSERT INTO slcf (mainID, QUANTITY, SPEC_ID, PBY, PBZ, PBX, VECTOR, CELL_CENTERED) VALUES ('1', '', '', '', '', '', '', '');";
			String initSurf = "INSERT INTO surf (mainID, ID, PART_ID, MATL_ID, VEL, TMP_FRONT, BACKING, DEFAULT_SURF, GEOMETRY, " + 
					"COLOR, HRRPUA) VALUES ('1', '', '', '', '', '', '', '', '', '', '');";
			String initVent = "INSERT INTO vent (mainID, XB, SURF_ID, MB) VALUES ('1', '', '', '');";
			String initRamp = "INSERT INTO ramp (mainID, FRACTION, TIME, ID) VALUES ('1', '', '', '');";
			String initCtrl = "INSERT INTO ctrl (mainID, INPUT_ID, RAMP_ID, ID, LATCH, FUNCTION_TYPE) VALUES ('1', '', '', '', '', '');";
			String initReac = "INSERT INTO reac (mainID, AUTO_IGNITION_TEMPERATURE, SOOT_YIELD, FUEL) VALUES ('1', '', '', '');";
			String initObst = "INSERT INTO obst (mainID, BULK_DENSITY, COLOR, SURF_ID, XB) VALUES ('1', '', '', '', '');";
			String initMisc = "INSERT INTO misc (NOISE, FREEZE_VELOCITY, HUMIDITY, Y_CO2_INFNTY, TMPA, GVEC) VALUES ('', '', '', '', '', '');";
			String initRadi = "INSERT INTO radi (RADIATION) VALUES ('');";
			String initDump = "INSERT INTO dump (MASS_FILE, SMOKE_3D, NFRAMES, DT_DEVC) VALUES ('', '', '', '');";
			String initMatl = "INSERT INTO matl (mainID, SPECIFIC_HEAT, HEAT_OF_REACTION, SPEC_ID, ID, REFERENCE_TEMPERATURE, N_REACTIONS, DENSITY, CONDUCTIVITY) "
					+ "VALUES ('1', '', '', '', '', '', '', '', '')";
			String initMult = "INSERT INTO mult (mainID, ID, I_UPPER, J_UPPER, K_UPPER, DX, DY, DZ) VALUES ('1', '', '', '', '', '', '', '');";
			String initWind = "INSERT INTO wind (Z_0, DIRECTION, L, SPEED) VALUES ('', '', '', '');";
			String initPres = "INSERT INTO pres (FISHPAK_BC, SOLVER) VALUES ('', '');";
			String initComb = "INSERT INTO comb (FIXED_MIX_TIME, EXTINCTION_MODEL) VALUES ('', '');";
			String initTabl = "INSERT INTO tabl (mainID, ID, TABLE_DATA) VALUES ('1', '', '');";
			String initClip = "INSERT INTO clip (MAXIMUM_DENSITY, MAXIMUM_TEMPERATURE, MINIMUM_DENSITY, MINIMUM_TEMPERATURE) VALUES ('', '', '', '');";
			String initHvac = "INSERT INTO hvac (ID, ROUGHNESS, DEVC_ID, LENGTH, FAN_ID, AREA, TYPE_ID) VALUES ('', '', '', '', '', '', '');";
			String initHole = "INSERT INTO hole (MESH_ID, MULT_ID, DEVC_ID, CTRL_ID, XB) VALUES ('', '', '', '', '');";
			String initIsof = "INSERT INTO isof (QUANTITY, VALUE_1, VALUE_2, VALUE_3) VALUES ('', '', '', '');";
			String initMove = "INSERT INTO move (ID, X0, Y0, Z0, ROTATION_ANGLE, AXIS) VALUES ('', '', '', '', '', '');";
			String initProf = "INSERT INTO prof (ID, XYZ, QUANTITY, IOR) VALUES ('', '', '', '');";
			String initRadf = "INSERT INTO radf (I_STEP, J_STEP, K_STEP, XB) VALUES ('', '', '', '');";
			String initTrnx = "INSERT INTO trnx (mainID, ID, MESH_NUMBER, CC, PC) VALUES ('1', '', '', '', '');";
			String initZone = "INSERT INTO zone (mainID, XYZ) VALUES ('1', '');";
			
			Statement statement;
			statement = connection.createStatement();
			
			statement.executeUpdate(initHead);
			statement.executeUpdate(initTime);
			statement.executeUpdate(initCatf);
			statement.executeUpdate(initInit);
			statement.executeUpdate(initMesh);
			statement.executeUpdate(initPart);
			statement.executeUpdate(initBndf);
			statement.executeUpdate(initProp);
			statement.executeUpdate(initSpec);
			statement.executeUpdate(initDevc);
			statement.executeUpdate(initSlcf);
			statement.executeUpdate(initSurf);
			statement.executeUpdate(initVent);
			statement.executeUpdate(initRamp);
			statement.executeUpdate(initCtrl);
			statement.executeUpdate(initReac);
			statement.executeUpdate(initObst);
			statement.executeUpdate(initMisc);
			statement.executeUpdate(initRadi);
			statement.executeUpdate(initDump);
			statement.executeUpdate(initMatl);
			statement.executeUpdate(initMult);
			statement.executeUpdate(initWind);
			statement.executeUpdate(initPres);
			statement.executeUpdate(initComb);
			statement.executeUpdate(initTabl);
			statement.executeUpdate(initClip);
			statement.executeUpdate(initHvac);
			statement.executeUpdate(initHole);
			statement.executeUpdate(initIsof);
			statement.executeUpdate(initMove);
			statement.executeUpdate(initProf);
			statement.executeUpdate(initRadf);
			statement.executeUpdate(initTrnx);
			statement.executeUpdate(initZone);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("DATABASE NOT SET CORRECTLY");
		}
	}
	
	public static void main(String[] args) throws IOException {
		launch(args);
	}
}
