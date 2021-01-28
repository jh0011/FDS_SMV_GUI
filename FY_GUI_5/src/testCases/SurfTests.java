package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.SurfController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class SurfTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	SurfController surfTest = new SurfController();
	
	@Test
	public void testXb() { //check 6 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb2() { //check 6 values with white spaces for XB
		TextField xbText = new TextField("1, 2,3,4,5,6");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}

	@Test
	public void testXb3() { //check 5 values for XB
		TextField xbText = new TextField("1,2,3,4,5");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb4() { //check 7 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6,7");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb5() { //check 6 values with negatives for XB
		TextField xbText = new TextField("1,2,3,-4,5,6");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb6() { //check 6 values with alphanumeric for XB
		TextField xbText = new TextField("1,2,3,abc,5,6");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb7() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1;2;3;4;5;6");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb8() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1,2,3,4,,5,6");
		boolean checkXbText = surfTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = surfTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = surfTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for float with negative
		TextField floatText = new TextField("-23");
		boolean checkFloatText = surfTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float with alphanumeric
		TextField floatText = new TextField("abc");
		boolean checkFloatText = surfTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float 
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = surfTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testAllFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = surfTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = surfTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testAllFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = surfTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = surfTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testAllFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = surfTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testMatl() { //check matl value
		TextField matlText = new TextField("GypsumID");
		boolean checkMatlText = surfTest.checkMatlValues(matlText);
		assertTrue(checkMatlText);
	}
	
	@Test
	public void testMatl2() { //check matl value
		TextField matlText = new TextField("Gypsum ID 2");
		boolean checkMatlText = surfTest.checkMatlValues(matlText);
		assertTrue(checkMatlText);
	}
	
	@Test
	public void testMatl3() { //check 2 matl values
		TextField matlText = new TextField("Gypsum ID 2, FoamID");
		boolean checkMatlText = surfTest.checkMatlValues(matlText);
		assertFalse(checkMatlText);
	}
	
	@Test
	public void testMatl4() { //check 2 matl values
		TextField matlText = new TextField("Gypsum ID 2; FoamID");
		boolean checkMatlText = surfTest.checkMatlValues(matlText);
		assertFalse(checkMatlText);
	}

}
