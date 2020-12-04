package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.DevcController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class DevcTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	DevcController devcTest = new DevcController();

	@Test
	public void testXb() { //check 6 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb2() { //check 6 values with white spaces for XB
		TextField xbText = new TextField("1, 2,3,4,5,6");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}

	@Test
	public void testXb3() { //check 5 values for XB
		TextField xbText = new TextField("1,2,3,4,5");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb4() { //check 7 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6,7");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb5() { //check 6 values with negatives for XB
		TextField xbText = new TextField("1,2,3,-4,5,6");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb6() { //check 6 values with alphanumeric for XB
		TextField xbText = new TextField("1,2,3,abc,5,6");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb7() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1;2;3;4;5;6");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb8() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1,2,3,4,,5,6");
		boolean checkXbText = devcTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXyz() { //check 3 integer values
		TextField xyzText = new TextField("1,2,3");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}
	
	@Test
	public void testXyz2() { //check 3 float values
		TextField xyzText = new TextField("1.0,2.2,3");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}

	@Test
	public void testXyz3() { //check 3 values with white spaces
		TextField xyzText = new TextField("1, 2, 3");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz4() { //check 3 values with negative value
		TextField xyzText = new TextField("1,-2,3");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz5() { //check 3 values with alphanumeric
		TextField xyzText = new TextField("1, abc, 3");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz6() { //check 2 values xyz
		TextField xyzText = new TextField("1,2");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz7() { //check 4 values xyz
		TextField xyzText = new TextField("1,2,3,4");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz8() { //check 3 values with incorrect delimiter
		TextField xyzText = new TextField("1,2,,3");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz9() { //check 3 values with incorrect delimiter
		TextField xyzText = new TextField("1;2;3");
		boolean checkXyzText = devcTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = devcTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = devcTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = devcTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = devcTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = devcTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
}
