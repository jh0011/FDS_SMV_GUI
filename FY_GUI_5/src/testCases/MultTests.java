package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.MultController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

/**
 * Test cases for the MultController
 */
public class MultTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	MultController multTest = new MultController();

	@Test
	public void testInt() { //check for integer
		TextField intText = new TextField("23");
		boolean checkIntText = multTest.checkPosIntValues(intText);
		assertTrue(checkIntText);
	}
	
	@Test
	public void testInt2() { //check for negative integer
		TextField intText = new TextField("-23");
		boolean checkIntText = multTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt3() { //check for integer with alphanumeric
		TextField intText = new TextField("abc");
		boolean checkIntText = multTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt4() { //check for integer with alphanumeric
		TextField intText = new TextField("23abc");
		boolean checkIntText = multTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt5() { //check for integer with float
		TextField intText = new TextField("23.4");
		boolean checkIntText = multTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testInt6() { //check for integer with float
		TextField intText = new TextField("23.0");
		boolean checkIntText = multTest.checkPosIntValues(intText);
		assertFalse(checkIntText);
	}
	
	@Test
	public void testFloat() { //check for float
		TextField floatText = new TextField("23.0");
		boolean checkFloatText = multTest.checkPosFloatValues(floatText);
		assertTrue(checkFloatText);
	}
	
	@Test
	public void testFloat2() { //check for float
		TextField floatText = new TextField("23");
		boolean checkFloatText = multTest.checkPosFloatValues(floatText);
		assertTrue(checkFloatText);
	}

	@Test
	public void testFloat3() { //check for float
		TextField floatText = new TextField("-23");
		boolean checkFloatText = multTest.checkPosFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat4() { //check for float
		TextField floatText = new TextField("abc");
		boolean checkFloatText = multTest.checkPosFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testFloat5() { //check for float
		TextField floatText = new TextField("-23.4");
		boolean checkFloatText = multTest.checkPosFloatValues(floatText);
		assertFalse(checkFloatText);
	}
	
	@Test
	public void testDirection() { //check direction int
		TextField directionText = new TextField("40");
		boolean checkDirectionText = multTest.checkDirectionValues(directionText);
		assertTrue(checkDirectionText);
	}

	@Test
	public void testDirection2() { //check direction float
		TextField directionText = new TextField("40.5");
		boolean checkDirectionText = multTest.checkDirectionValues(directionText);
		assertTrue(checkDirectionText);
	}
	
	@Test
	public void testDirection3() { //check direction negative int
		TextField directionText = new TextField("-40");
		boolean checkDirectionText = multTest.checkDirectionValues(directionText);
		assertFalse(checkDirectionText);
	}
	
	@Test
	public void testDirection4() { //check direction beyond range
		TextField directionText = new TextField("361");
		boolean checkDirectionText = multTest.checkDirectionValues(directionText);
		assertFalse(checkDirectionText);
	}
	
	@Test
	public void testDirection5() { //check direction float with alphanumeric
		TextField directionText = new TextField("abc");
		boolean checkDirectionText = multTest.checkDirectionValues(directionText);
		assertFalse(checkDirectionText);
	}
	
	@Test
	public void testL() { //check L float positive
		TextField LText = new TextField("40");
		boolean checkLText = multTest.checkLValues(LText);
		assertTrue(checkLText);
	}
	
	@Test
	public void testL2() { //check L float negative
		TextField LText = new TextField("-400");
		boolean checkLText = multTest.checkLValues(LText);
		assertTrue(checkLText);
	}
	
	@Test
	public void testL3() { //check L float beyond range
		TextField LText = new TextField("-550");
		boolean checkLText = multTest.checkLValues(LText);
		assertFalse(checkLText);
	}
	
	@Test
	public void testL4() { //check L float
		TextField LText = new TextField("40.5");
		boolean checkLText = multTest.checkLValues(LText);
		assertTrue(checkLText);
	}
	
	@Test
	public void testL5() { //check L float
		TextField LText = new TextField("abc");
		boolean checkLText = multTest.checkLValues(LText);
		assertFalse(checkLText);
	}
}
