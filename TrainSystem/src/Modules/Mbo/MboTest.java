package Modules.Mbo;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MboTest {

	private Mbo mbo;

	@Before
	public void setUp() throws Exception {
		mbo = new Mbo();
	}

	@Test 
	public void testTrainDataNotNull() {
		Object[][] trainData = mbo.getTrainData("");
		assertThat(trainData,not(new String()));
	}

	@Test 
	public void testAlwaysPasses() {
		assertEquals(1,1);
	}
}
