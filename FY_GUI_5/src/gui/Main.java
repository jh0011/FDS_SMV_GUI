package gui;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
			Values.initValues();
			Parent root;
			//root = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Should be the actual line
			root = FXMLLoader.load(getClass().getResource("Pres.fxml")); //For testing a certain page
			Scene introScene = new Scene(root, 870, 710);
			primaryStage.setScene(introScene);
			primaryStage.setTitle("FDS-SMV GUI");
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
			//delete the table
			String sqlHead = "DELETE FROM head;";
			String sqlTime = "DELETE FROM time;";
			String sqlCatf = "DELETE FROM catf;";
			String sqlInit = "DELETE FROM init;";
			String sqlMesh = "DELETE FROM mesh;";
			String sqlPart = "DELETE FROM part;";
			String sqlBndf = "DELETE FROM bndf;";
			String sqlProp = "DELETE FROM prop;";
			String sqlSpec = "DELETE FROM spec;";
			String sqlDevc = "DELETE FROM devc;";
			String sqlSlcf = "DELETE FROM slcf;";
			String sqlSurf = "DELETE FROM surf;";
			String sqlVent = "DELETE FROM vent;";
			String sqlRamp = "DELETE FROM ramp;";
			String sqlCtrl = "DELETE FROM ctrl;";
			String sqlReac = "DELETE FROM reac;";
			String sqlObst = "DELETE FROM obst;";
			String sqlMisc = "DELETE FROM misc;";
			String sqlRadi = "DELETE FROM radi;";
			String sqlDump = "DELETE FROM dump;";
			String sqlMatl = "DELETE FROM matl;";
			String sqlMult = "DELETE FROM mult;";
			String sqlWind = "DELETE FROM wind;";
			String sqlPres = "DELETE FROM pres;";
			String sqlComb = "DELETE FROM comb;";
			String sqlTabl = "DELETE FROM tabl;";
			String sqlClip = "DELETE FROM clip;";
			
			//insert an empty row
			String initHead = "INSERT INTO head(CHID, TITLE) VALUES ('', '');";
			String initTime = "INSERT INTO time (EndTime, StartTime, DT) VALUES ('', '', '');";
			String initCatf = "INSERT INTO catf(OTHER_FILES) VALUES ('');";
			String initInit = "INSERT INTO init(mainID, idText, partIdText, specIdText, npartText, "
					+ "npartCellText, massTimeText, massVolText, massFracText, xbText) "
					+ "VALUES ('1', '', '', '', '', '', '', '', '', '')";
			String initMesh = "INSERT INTO mesh (mainID, ijkText, xbText) VALUES ('', '', '');";
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
			
			Statement statement;
			statement = connection.createStatement();
			statement.executeUpdate(sqlHead);
			statement.executeUpdate(sqlTime);
			statement.executeUpdate(sqlCatf);
			statement.executeUpdate(sqlInit);
			statement.executeUpdate(sqlMesh);
			statement.executeUpdate(sqlPart);
			statement.executeUpdate(sqlBndf);
			statement.executeUpdate(sqlProp);
			statement.executeUpdate(sqlSpec);
			statement.executeUpdate(sqlDevc);
			statement.executeUpdate(sqlSlcf);
			statement.executeUpdate(sqlSurf);
			statement.executeUpdate(sqlVent);
			statement.executeUpdate(sqlRamp);
			statement.executeUpdate(sqlCtrl);
			statement.executeUpdate(sqlReac);
			statement.executeUpdate(sqlObst);
			statement.executeUpdate(sqlMisc);
			statement.executeUpdate(sqlRadi);
			statement.executeUpdate(sqlDump);
			statement.executeUpdate(sqlMatl);
			statement.executeUpdate(sqlMult);
			statement.executeUpdate(sqlWind);
			statement.executeUpdate(sqlPres);
			statement.executeUpdate(sqlComb);
			statement.executeUpdate(sqlTabl);
			statement.executeUpdate(sqlClip);
			
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
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("DATABASE NOT SET CORRECTLY");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
