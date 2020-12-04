package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.PresController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class PresTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	PresController presTest = new PresController();

	@Test
	public void testFishpak() { //check 3 integer values
		TextField FishpakText = new TextField("1,2,3");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertTrue(checkFishpakText);
	}
	
	@Test
	public void testFishpak2() { //check 3 float values
		TextField FishpakText = new TextField("1.0,2.2,3");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}

	@Test
	public void testFishpak3() { //check 3 values with white spaces
		TextField FishpakText = new TextField("1, 2, 3");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}
	
	@Test
	public void testFishpak4() { //check 3 values with negative value
		TextField FishpakText = new TextField("1,-2,3");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}
	
	@Test
	public void testFishpak5() { //check 3 values with alphanumeric
		TextField FishpakText = new TextField("1, abc, 3");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}
	
	@Test
	public void testFishpak6() { //check 2 values Fishpak
		TextField FishpakText = new TextField("1,2");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}
	
	@Test
	public void testFishpak7() { //check 4 values Fishpak
		TextField FishpakText = new TextField("1,2,3,4");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}
	
	@Test
	public void testFishpak8() { //check 3 values with incorrect delimiter
		TextField FishpakText = new TextField("1,2,,3");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}
	
	@Test
	public void testFishpak9() { //check 3 values with incorrect delimiter
		TextField FishpakText = new TextField("1;2;3");
		boolean checkFishpakText = presTest.checkFishpakFormat(FishpakText);
		assertFalse(checkFishpakText);
	}
	
	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = presTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for int
		TextField floatText = new TextField("23");
		boolean checkFloatText = presTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for int negative
		TextField floatText = new TextField("-23");
		boolean checkFloatText = presTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float alphanumeric
		TextField floatText = new TextField("abc");
		boolean checkFloatText = presTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float negative
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = presTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testTable() { //check 6 values for Table
		TextField TableText = new TextField("1,2,3,4,5,6");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable2() { //check 6 values with white spaces for Table
		TextField TableText = new TextField("1, 2,3,4,5,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}

	@Test
	public void testTable3() { //check 5 values for Table
		TextField TableText = new TextField("1,2,3,4,5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable4() { //check 7 values for Table
		TextField TableText = new TextField("1,2,3,4,5,6,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable5() { //check 6 values with negatives for Table
		TextField TableText = new TextField("1,2,3,-4,5,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable6() { //check 6 values with alphanumeric for Table
		TextField TableText = new TextField("1,2,3,abc,5,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable7() { //check 6 values with incorrect delimiter for Table
		TextField TableText = new TextField("1;2;3;4;5;0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable8() { //check 6 values with incorrect delimiter for Table
		TextField TableText = new TextField("1,2,3,4,,5,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable9() { //check 6 values for Table
		TextField TableText = new TextField("1,2,3,4,5,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertTrue(checkTableText);
	}
	
	@Test
	public void testTable10() { //check 6 values beyond range for Table
		TextField TableText = new TextField("1,2,3,361,5,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testTable11() { //check 6 values float for Table
		TextField TableText = new TextField("1,2,30.4,4,5,0.5");
		boolean checkTableText = presTest.checkTableDataFormat(TableText);
		assertFalse(checkTableText);
	}
	
	@Test
	public void testAllFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = presTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = presTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testAllFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = presTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = presTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testAllFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = presTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

}
