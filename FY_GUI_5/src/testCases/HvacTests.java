package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.HvacController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class HvacTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	HvacController hvacTest = new HvacController();

	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = hvacTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for int
		TextField floatText = new TextField("23");
		boolean checkFloatText = hvacTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for int negative
		TextField floatText = new TextField("-23");
		boolean checkFloatText = hvacTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float alphanumeric
		TextField floatText = new TextField("abc");
		boolean checkFloatText = hvacTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float negative
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = hvacTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testXb() { //check 6 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb2() { //check 6 values with white spaces for XB
		TextField xbText = new TextField("1, 2,3,4,5,6");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}

	@Test
	public void testXb3() { //check 5 values for XB
		TextField xbText = new TextField("1,2,3,4,5");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb4() { //check 7 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6,7");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb5() { //check 6 values with negatives for XB
		TextField xbText = new TextField("1,2,3,-4,5,6");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb6() { //check 6 values with alphanumeric for XB
		TextField xbText = new TextField("1,2,3,abc,5,6");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb7() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1;2;3;4;5;6");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb8() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1,2,3,4,,5,6");
		boolean checkXbText = hvacTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testAllFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = hvacTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = hvacTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testAllFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = hvacTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = hvacTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testAllFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = hvacTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
}
