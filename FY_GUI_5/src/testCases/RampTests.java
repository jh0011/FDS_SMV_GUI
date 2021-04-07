package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.RampController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

/**
 * Test cases for the RampController
 */
public class RampTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	RampController rampTest = new RampController();

	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = rampTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = rampTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for float with negative
		TextField floatText = new TextField("-23");
		boolean checkFloatText = rampTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float with alphanumeric
		TextField floatText = new TextField("abc");
		boolean checkFloatText = rampTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float 
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = rampTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testAllFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = rampTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = rampTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testAllFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = rampTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = rampTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testAllFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = rampTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

}
