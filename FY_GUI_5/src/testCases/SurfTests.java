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
	public void testXb() { //test with 6 positive values
		TextField xbText = new TextField("1,2,3,4,5,6");
		boolean checkXb = surfTest.checkXbFormat(xbText);
		assertTrue(checkXb);
	}
	
	@Test
	public void testXb2() { //test with 7 positive values
		TextField xbText = new TextField("1,2,3,4,5,6,7");
		boolean checkXb = surfTest.checkXbFormat(xbText);
		assertFalse(checkXb);
	}
	
	@Test
	public void testXb3() { //test with 5 positive values & 1 negative
		TextField xbText = new TextField("1,2,3,-4,5,6");
		boolean checkXb = surfTest.checkXbFormat(xbText);
		assertFalse(checkXb);
	}
	
	@Test
	public void testXb4() { //test with 6 positive values & white spaces
		TextField xbText = new TextField("1, 2, 3, 4, 5, 6");
		boolean checkXb = surfTest.checkXbFormat(xbText);
		assertFalse(checkXb);
	}
	
	@Test
	public void testXb5() { //test with 5 positive values & alphanumeric
		TextField xbText = new TextField("1,2,3,ed,5,6");
		boolean checkXb = surfTest.checkXbFormat(xbText);
		assertFalse(checkXb);
	}
	
	@Test
	public void testXb6() { //test with 6 positive float values
		TextField xbText = new TextField("1.1,2.1,3.1,4,5,6");
		boolean checkXb = surfTest.checkXbFormat(xbText);
		assertTrue(checkXb);
	}

}
