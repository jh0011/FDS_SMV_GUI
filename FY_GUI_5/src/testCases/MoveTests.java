package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.MoveController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class MoveTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	MoveController moveTest = new MoveController();

	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = moveTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for int
		TextField floatText = new TextField("23");
		boolean checkFloatText = moveTest.checkFloatPosValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for int negative
		TextField floatText = new TextField("-23");
		boolean checkFloatText = moveTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float alphanumeric
		TextField floatText = new TextField("abc");
		boolean checkFloatText = moveTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float negative
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = moveTest.checkFloatPosValues(floatText);
		assertFalse(checkFloatText);
	}

	@Test
	public void testDirection() { //check direction int
		TextField directionText = new TextField("40");
		boolean checkDirectionText = moveTest.checkAngleValues(directionText);
		assertTrue(checkDirectionText);
	}

	@Test
	public void testDirection2() { //check direction float
		TextField directionText = new TextField("40.5");
		boolean checkDirectionText = moveTest.checkAngleValues(directionText);
		assertTrue(checkDirectionText);
	}
	
	@Test
	public void testDirection3() { //check direction negative int
		TextField directionText = new TextField("-40");
		boolean checkDirectionText = moveTest.checkAngleValues(directionText);
		assertFalse(checkDirectionText);
	}
	
	@Test
	public void testDirection4() { //check direction beyond range
		TextField directionText = new TextField("361");
		boolean checkDirectionText = moveTest.checkAngleValues(directionText);
		assertFalse(checkDirectionText);
	}
	
	@Test
	public void testDirection5() { //check direction float with alphanumeric
		TextField directionText = new TextField("abc");
		boolean checkDirectionText = moveTest.checkAngleValues(directionText);
		assertFalse(checkDirectionText);
	}
	
	@Test
	public void testAxis() { //check 3 integer values
		TextField AxisText = new TextField("1,2,3");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertTrue(checkAxisText);
	}
	
	@Test
	public void testAxis2() { //check 3 float values
		TextField AxisText = new TextField("1.0,2.2,3");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertTrue(checkAxisText);
	}

	@Test
	public void testAxis3() { //check 3 values with white spaces
		TextField AxisText = new TextField("1, 2, 3");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertFalse(checkAxisText);
	}
	
	@Test
	public void testAxis4() { //check 3 values with negative value
		TextField AxisText = new TextField("1,-2,3");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertFalse(checkAxisText);
	}
	
	@Test
	public void testAxis5() { //check 3 values with alphanumeric
		TextField AxisText = new TextField("1, abc, 3");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertFalse(checkAxisText);
	}
	
	@Test
	public void testAxis6() { //check 2 values Axis
		TextField AxisText = new TextField("1,2");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertFalse(checkAxisText);
	}
	
	@Test
	public void testAxis7() { //check 4 values Axis
		TextField AxisText = new TextField("1,2,3,4");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertFalse(checkAxisText);
	}
	
	@Test
	public void testAxis8() { //check 3 values with incorrect delimiter
		TextField AxisText = new TextField("1,2,,3");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertFalse(checkAxisText);
	}
	
	@Test
	public void testAxis9() { //check 3 values with incorrect delimiter
		TextField AxisText = new TextField("1;2;3");
		boolean checkAxisText = moveTest.checkAxisFormat(AxisText);
		assertFalse(checkAxisText);
	}
	
	@Test
	public void testXyz() { //check 3 integer values
		TextField xyzText = new TextField("1,2,3");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}
	
	@Test
	public void testXyz2() { //check 3 float values
		TextField xyzText = new TextField("1.0,2.2,3");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}

	@Test
	public void testXyz3() { //check 3 values with white spaces
		TextField xyzText = new TextField("1, 2, 3");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz4() { //check 3 values with negative value
		TextField xyzText = new TextField("1,-2,3");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertTrue(checkXyzText);
	}
	
	@Test
	public void testXyz5() { //check 3 values with alphanumeric
		TextField xyzText = new TextField("1, abc, 3");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz6() { //check 2 values xyz
		TextField xyzText = new TextField("1,2");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz7() { //check 4 values xyz
		TextField xyzText = new TextField("1,2,3,4");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz8() { //check 3 values with incorrect delimiter
		TextField xyzText = new TextField("1,2,,3");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testXyz9() { //check 3 values with incorrect delimiter
		TextField xyzText = new TextField("1;2;3");
		boolean checkXyzText = moveTest.checkXyzFormat(xyzText);
		assertFalse(checkXyzText);
	}
	
	@Test
	public void testInt() { //check for integer
		TextField intText = new TextField("23");
		boolean checkIntText = moveTest.checkPosIntValues(intText);
		assertTrue(checkIntText);
	}
	
	@Test
	public void testInt2() { //check for negative integer
		TextField intText = new TextField("-23");
		boolean checkIntText = moveTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt3() { //check for integer with alphanumeric
		TextField intText = new TextField("abc");
		boolean checkIntText = moveTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt4() { //check for integer with alphanumeric
		TextField intText = new TextField("23abc");
		boolean checkIntText = moveTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt5() { //check for integer with float
		TextField intText = new TextField("23.4");
		boolean checkIntText = moveTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt6() { //check for integer with float
		TextField intText = new TextField("23.0");
		boolean checkIntText = moveTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testXb() { //check 6 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb2() { //check 6 values with white spaces for XB
		TextField xbText = new TextField("1, 2,3,4,5,6");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}

	@Test
	public void testXb3() { //check 5 values for XB
		TextField xbText = new TextField("1,2,3,4,5");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb4() { //check 7 values for XB
		TextField xbText = new TextField("1,2,3,4,5,6,7");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb5() { //check 6 values with negatives for XB
		TextField xbText = new TextField("1,2,3,-4,5,6");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertTrue(checkXbText);
	}
	
	@Test
	public void testXb6() { //check 6 values with alphanumeric for XB
		TextField xbText = new TextField("1,2,3,abc,5,6");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb7() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1;2;3;4;5;6");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
	
	@Test
	public void testXb8() { //check 6 values with incorrect delimiter for XB
		TextField xbText = new TextField("1,2,3,4,,5,6");
		boolean checkXbText = moveTest.checkXbFormat(xbText);
		assertFalse(checkXbText);
	}
}
