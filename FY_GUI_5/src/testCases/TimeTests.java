package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.TimeController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

public class TimeTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	TimeController timeTest = new TimeController();
	
	@Test
	public void testTimeEnd() { //check if time end is empty
		String timeEnd = "";
		boolean endTimeChecking = timeTest.checkTimeEnd(timeEnd);
		assertFalse(endTimeChecking);
	}
	
	@Test(expected=NullPointerException.class)
	public void testTimeEnd2() { //check if time end is written
		String timeEnd = "23";
		boolean endTimeChecking = timeTest.checkTimeEnd(timeEnd);
		assertTrue(endTimeChecking);
	}

	@Test
	public void testTimeFloat() { //check if time is negative
		TextField timeFloat = new TextField("-23");
		boolean checkTimeFloat = timeTest.checkTimeFloat(timeFloat);
		assertFalse(checkTimeFloat);
	}
	
	@Test
	public void testTimeFloat2() { //check if time is numerical
		TextField timeFloat = new TextField("abc");
		boolean checkTimeFloat = timeTest.checkTimeFloat(timeFloat);
		assertFalse(checkTimeFloat);
	}
	
	@Test
	public void testTimeFloat3() { //check if time is an integer
		TextField timeFloat = new TextField("23");
		boolean checkTimeFloat = timeTest.checkTimeFloat(timeFloat);
		assertTrue(checkTimeFloat);
	}
	
	@Test
	public void testTimeFloat4() { //check if time is a float
		TextField timeFloat = new TextField("23.4");
		boolean checkTimeFloat = timeTest.checkTimeFloat(timeFloat);
		assertTrue(checkTimeFloat);
	}
	
	@Test
	public void testFileFormat() { //check if files has extension
		String filesText = "couch.tpl" + "\n" + "newCouch";
		boolean checkFiles = timeTest.formatText(filesText);
		assertFalse(checkFiles);
	}
	
	@Test(expected=NullPointerException.class)
	public void testFileFormat2() { //check file for 1 file
		String filesText = "couch.tpl";
		boolean checkFiles = timeTest.formatText(filesText);
		assertTrue(checkFiles);
	}
	
	@Test
	public void testFileFormat3() { //check file for incorrect delimitation
		String filesText = "couch.tpl,newcouch.tpl";
		boolean checkFiles = timeTest.formatText(filesText);
		assertFalse(checkFiles);
	}
	
	@Test
	public void testFileFormat4() { //check file for incorrect delimitation
		String filesText = "couch.tpl;newcouch.tpl";
		boolean checkFiles = timeTest.formatText(filesText);
		assertFalse(checkFiles);
	}
	
	@Test
	public void testFileFormat5() { //check file for incorrect delimitation
		String filesText = "couch.tpl newcouch.tpl";
		boolean checkFiles = timeTest.formatText(filesText);
		assertFalse(checkFiles);
	}
	
	@Test
	public void testFileFormat6() { //check file for incorrect delimitation
		String filesText = "couch.tplnewcouch.tpl";
		boolean checkFiles = timeTest.formatText(filesText);
		assertFalse(checkFiles);
	}
}
