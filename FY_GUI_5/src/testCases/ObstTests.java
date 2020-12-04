package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.ObstController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class ObstTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	ObstController obstTest = new ObstController();

	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = obstTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = obstTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for float with negative
		TextField floatText = new TextField("-23");
		boolean checkFloatText = obstTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float with alphanumeric
		TextField floatText = new TextField("abc");
		boolean checkFloatText = obstTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float 
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = obstTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testXb() { //check 6 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb2() { //check 6 values with white spaces for XB
		TextField xbText = new TextField("1, 2,3,4,5,6");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}

	@Test
	public void testXb3() { //check 5 values for XB
		TextField xbText = new TextField("1,2,3,4,5");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb4() { //check 7 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6,7");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb5() { //check 6 values with negatives for XB
		TextField xbText = new TextField("1,2,3,-4,5,6");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb6() { //check 6 values with alphanumeric for XB
		TextField xbText = new TextField("1,2,3,abc,5,6");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb7() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1;2;3;4;5;6");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb8() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1,2,3,4,,5,6");
		boolean checkXbText = obstTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testGvec() { //check 3 integer values
		TextField GvecText = new TextField("1,2,3");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertTrue(checkGvecText);
	}
	
	@Test
	public void testGvec2() { //check 3 float values
		TextField GvecText = new TextField("1.0,2.2,3");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertTrue(checkGvecText);
	}

	@Test
	public void testGvec3() { //check 3 values with white spaces
		TextField GvecText = new TextField("1, 2, 3");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertFalse(checkGvecText);
	}
	
	@Test
	public void testGvec4() { //check 3 values with negative value
		TextField GvecText = new TextField("1,-2,3");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertTrue(checkGvecText);
	}
	
	@Test
	public void testGvec5() { //check 3 values with alphanumeric
		TextField GvecText = new TextField("1, abc, 3");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertFalse(checkGvecText);
	}
	
	@Test
	public void testGvec6() { //check 2 values Gvec
		TextField GvecText = new TextField("1,2");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertFalse(checkGvecText);
	}
	
	@Test
	public void testGvec7() { //check 4 values Gvec
		TextField GvecText = new TextField("1,2,3,4");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertFalse(checkGvecText);
	}
	
	@Test
	public void testGvec8() { //check 3 values with incorrect delimiter
		TextField GvecText = new TextField("1,2,,3");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertFalse(checkGvecText);
	}
	
	@Test
	public void testGvec9() { //check 3 values with incorrect delimiter
		TextField GvecText = new TextField("1;2;3");
		boolean checkGvecText = obstTest.checkGvecFormat(GvecText);
		assertFalse(checkGvecText);
	}
	
	@Test
	public void testAllFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = obstTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = obstTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testAllFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = obstTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testAllFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = obstTest.checkFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testAllFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = obstTest.checkFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testPercent() { //check percentage int
		TextField percentText = new TextField("40");
		boolean checkPercentText = obstTest.checkPercentValues(percentText);
		assertTrue(checkPercentText);
	}

	@Test
	public void testPercent2() { //check percentage float
		TextField percentText = new TextField("40.4");
		boolean checkPercentText = obstTest.checkPercentValues(percentText);
		assertTrue(checkPercentText);
	}
	
	@Test
	public void testPercent3() { //check percentage negative int
		TextField percentText = new TextField("-40");
		boolean checkPercentText = obstTest.checkPercentValues(percentText);
		assertFalse(checkPercentText);
	}
	
	@Test
	public void testPercent4() { //check percentage negative float
		TextField percentText = new TextField("-40.4");
		boolean checkPercentText = obstTest.checkPercentValues(percentText);
		assertFalse(checkPercentText);
	}
	
	@Test
	public void testPercent5() { //check percentage more than 100
		TextField percentText = new TextField("150");
		boolean checkPercentText = obstTest.checkPercentValues(percentText);
		assertFalse(checkPercentText);
	}
	
	@Test
	public void testPercent6() { //check percentage alphanumeric
		TextField percentText = new TextField("abc");
		boolean checkPercentText = obstTest.checkPercentValues(percentText);
		assertFalse(checkPercentText);
	}
	
	@Test
	public void testPercent7() { //check percentage negative int
		TextField percentText = new TextField("-40%");
		boolean checkPercentText = obstTest.checkPercentValues(percentText);
		assertFalse(checkPercentText);
	}
}
