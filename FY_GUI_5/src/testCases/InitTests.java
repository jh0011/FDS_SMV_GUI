package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.InitController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class InitTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	InitController initTest = new InitController();
	
	@Test
	public void testXb() { //check 6 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb2() { //check 6 values with white spaces for XB
		TextField xbText = new TextField("1, 2,3,4,5,6");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}

	@Test
	public void testXb3() { //check 5 values for XB
		TextField xbText = new TextField("1,2,3,4,5");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb4() { //check 7 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6,7");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb5() { //check 6 values with negatives for XB
		TextField xbText = new TextField("1,2,3,-4,5,6");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb6() { //check 6 values with alphanumeric for XB
		TextField xbText = new TextField("1,2,3,abc,5,6");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb7() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1;2;3;4;5;6");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb8() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1,2,3,4,,5,6");
		boolean checkXbText = initTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testInt() { //check for integer
		TextField intText = new TextField("23");
		boolean checkIntText = initTest.checkIntValues(intText);
		assertTrue(checkIntText);
	}
	
	@Test
	public void testInt2() { //check for negative integer
		TextField intText = new TextField("-23");
		boolean checkIntText = initTest.checkIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt3() { //check for integer with alphanumeric
		TextField intText = new TextField("abc");
		boolean checkIntText = initTest.checkIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt4() { //check for integer with alphanumeric
		TextField intText = new TextField("23abc");
		boolean checkIntText = initTest.checkIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt5() { //check for integer with float
		TextField intText = new TextField("23.4");
		boolean checkIntText = initTest.checkIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt6() { //check for integer with float
		TextField intText = new TextField("23.0");
		boolean checkIntText = initTest.checkIntValues(intText);
		assertFalse(checkIntText);
	}
}
