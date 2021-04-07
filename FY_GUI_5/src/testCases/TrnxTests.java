package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.TrnxController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

/**
 * Test cases for the TrnxController
 */
public class TrnxTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	TrnxController trnxTest = new TrnxController();

	@Test
	public void testInt() { //check for integer
		TextField intText = new TextField("23");
		boolean checkIntText = trnxTest.checkPosIntValues(intText);
		assertTrue(checkIntText);
	}
	
	@Test
	public void testInt2() { //check for negative integer
		TextField intText = new TextField("-23");
		boolean checkIntText = trnxTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt3() { //check for integer with alphanumeric
		TextField intText = new TextField("abc");
		boolean checkIntText = trnxTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt4() { //check for integer with alphanumeric
		TextField intText = new TextField("23abc");
		boolean checkIntText = trnxTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt5() { //check for integer with float
		TextField intText = new TextField("23.4");
		boolean checkIntText = trnxTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt6() { //check for integer with float
		TextField intText = new TextField("23.0");
		boolean checkIntText = trnxTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = trnxTest.checkPosFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for int
		TextField floatText = new TextField("23");
		boolean checkFloatText = trnxTest.checkPosFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for int negative
		TextField floatText = new TextField("-23");
		boolean checkFloatText = trnxTest.checkPosFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float alphanumeric
		TextField floatText = new TextField("abc");
		boolean checkFloatText = trnxTest.checkPosFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float negative
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = trnxTest.checkPosFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testXyz() { //check 3 integer values
		TextField xyzText = new TextField("1,2,3");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}
	
	@Test
	public void testXyz2() { //check 3 float values
		TextField xyzText = new TextField("1.0,2.2,3");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}

	@Test
	public void testXyz3() { //check 3 values with white spaces
		TextField xyzText = new TextField("1, 2, 3");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz4() { //check 3 values with negative value
		TextField xyzText = new TextField("1,-2,3");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}
	
	@Test
	public void testXyz5() { //check 3 values with alphanumeric
		TextField xyzText = new TextField("1, abc, 3");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz6() { //check 2 values xyz
		TextField xyzText = new TextField("1,2");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz7() { //check 4 values xyz
		TextField xyzText = new TextField("1,2,3,4");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz8() { //check 3 values with incorrect delimiter
		TextField xyzText = new TextField("1,2,,3");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz9() { //check 3 values with incorrect delimiter
		TextField xyzText = new TextField("1;2;3");
		boolean checkXyzText = trnxTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
}
