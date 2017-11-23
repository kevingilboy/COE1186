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
	public void testGetTrainDataByName() {
		Object[][] trainData = mbo.getTrainData("RED 3");
		assertEquals(trainData[0][0], "RED 3");
	}

	@Test 
	public void testGetTrainsByRegex() {
		Object[][] trainData = mbo.getTrainData("GREEN");
		String[] greenTrains = {"GREEN 1", "GREEN 2"};
		String[] received = new String[2];
		received[0] = trainData[0][0].toString();
		received[1] = trainData[1][0].toString();
		assertEquals(greenTrains, received);
	}

	@Test
	public void testGetAllTrainData() {
		Object[][] trainData = mbo.getTrainData("");
		String[] allTrains = {"GREEN 1", "GREEN 2",  
		                      "RED 1", "RED 2", "RED 3"};
		String[] received = new String[5];
		received[0] = trainData[0][0].toString();
		received[1] = trainData[1][0].toString();
		received[2] = trainData[2][0].toString();
		received[3] = trainData[3][0].toString();
		received[4] = trainData[4][0].toString();
		assertEquals(allTrains, received);
	}

	@Test 
	public void testAlwaysPasses() {
		assertEquals(1,1);
	}
}
