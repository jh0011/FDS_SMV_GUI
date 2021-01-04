package testCases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BasicTests.class, DevcTests.class, DumpTests.class, HvacTests.class, 
	InitTests.class, MoveTests.class, MultTests.class, ObstTests.class, PartTests.class, PresTests.class, 
	PropTests.class, RampTests.class, SurfTests.class, TimeTests.class, TrnxTests.class})
public class AllTests {

}
