package testCases;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import gui.BasicController;
import javafx.embed.swing.JFXPanel;

public class BasicTests {
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	JFXPanel fxPanel = new JFXPanel();
	BasicController basicTest = new BasicController();
	
	@Test
	public void testChid() { //check if chid is empty
		String chidText = "";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}

	@Test
	public void testChid2() { //check if chid has whitespace
		String chidText = "the couch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid3() { //check if chid has an extension
		String chidText = "couch.fds";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid4() { //check chid
		String chidText = "New_couch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertTrue(checkChid);
	}
	
	@Test
	public void testChid5() { //check if invalid chid symbol
		String chidText = "cou\\ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid6() { //check if invalid chid symbol
		String chidText = "cou|ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid7() { //check if invalid chid symbol
		String chidText = "cou?ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid8() { //check if invalid chid symbol
		String chidText = "cou/ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid9() { //check if invalid chid symbol
		String chidText = "cou<ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid10() { //check if invalid chid symbol
		String chidText = "cou>ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid11() { //check if invalid chid symbol
		String chidText = "cou*ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid12() { //check if invalid chid symbol
		String chidText = "cou:ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
	
	@Test
	public void testChid13() { //check if invalid chid symbol
		String chidText = "cou\"ch";
		boolean checkChid = basicTest.checkChid(chidText);
		assertFalse(checkChid);
	}
}
