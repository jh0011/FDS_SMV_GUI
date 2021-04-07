package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.PartController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

/**
 * Test cases for the PartController
 */
public class PartTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	PartController partTest = new PartController();
	
	@Test
	public void testInt() { //check for integer
		TextField intText = new TextField("23");
		boolean checkIntText = partTest.checkIntegerValues(intText);
		assertTrue(checkIntText);
	}
	
	@Test
	public void testInt2() { //check for negative integer
		TextField intText = new TextField("-23");
		boolean checkIntText = partTest.checkIntegerValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt3() { //check for integer with alphanumeric
		TextField intText = new TextField("abc");
		boolean checkIntText = partTest.checkIntegerValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt4() { //check for integer with alphanumeric
		TextField intText = new TextField("23abc");
		boolean checkIntText = partTest.checkIntegerValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt5() { //check for integer with float
		TextField intText = new TextField("23.4");
		boolean checkIntText = partTest.checkIntegerValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt6() { //check for integer with float
		TextField intText = new TextField("23.0");
		boolean checkIntText = partTest.checkIntegerValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = partTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = partTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = partTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = partTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = partTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
}
