package Modules.Mbo;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Arrays;

public class MboTest {

	private Mbo mbo;

	@Before
	public void setUp() throws Exception {
		mbo = new Mbo();
		String sig1 = "RED 1;0;0:" + Integer.toUnsignedString(0xE9533119);
		String sig2 = "RED 2;0;0:" + Integer.toUnsignedString(0xAEF34BC9);
		String sig3 = "GREEN 1;0;0:" + Integer.toUnsignedString(0x4A9EBEB4);
		String sig4 = "GREEN 2;0;0:" + Integer.toUnsignedString(0x0D3EC464);
		mbo.receiveTrainPosition(sig1);
		;mbo.receiveTrainPosition(sig2);
		mbo.receiveTrainPosition(sig3);
		mbo.receiveTrainPosition(sig4);
	}

	@Test 
	public void testTrainDataNotNull() {
		Object[][] trainData = mbo.getTrainData("");
		assertThat(trainData,not(new String()));
	}

	@Test
	public void testGetTrainDataByName() {
		Object[][] trainData = mbo.getTrainData("RED 2");
		assertEquals(trainData[0][0], "RED 2");
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
	public void testAddTrainBySignal() {
		String signal = "RED 3;1;2:" + Integer.toUnsignedString(0x7C5F6962);
		mbo.receiveTrainPosition(signal);
		Object[][] trainData = mbo.getTrainData("RED 3");
		assertEquals(trainData[0][0], "RED 3");
	}

	@Test
	public void testUpdateSignal() {
		String signal = "RED 3;1;2:" + Integer.toUnsignedString(0x7C5F6962);
		mbo.receiveTrainPosition(signal);
		//Object[][] trainData = mbo.getTrainData("RED 3");
		double[] pos = {1.0, 2.0};
		assertEquals(Arrays.toString(mbo.getTrainPosition("RED 3")), 
			         Arrays.toString(pos));
		//double[] gotten = trainData[0][2];
		//System.out.println("Name " + trainData[0][0]);
		//System.out.printf("Coord: %f, %f\n", coord[0], coord[1]);
		//boolean result = (trainData[0][0] == "RED 3" && trainData[0][2] == coord);
		//assertTrue(result);
	}

	@Test
	public void testGetAllTrainData() {
		Object[][] trainData = mbo.getTrainData("");
		String[] allTrains = {"GREEN 1", "GREEN 2",  
		                      "RED 1", "RED 2"};
		String[] received = new String[4];
		received[0] = trainData[0][0].toString();
		received[1] = trainData[1][0].toString();
		received[2] = trainData[2][0].toString();
		received[3] = trainData[3][0].toString();
		//received[4] = trainData[4][0].toString();
		assertEquals(allTrains, received);
	}

	@Test
	public void testChecksummedSignalAccepted() {
		String signal = "RED 3;1;2:" + Integer.toUnsignedString(0x7C5F6962);
		assertTrue(mbo.receiveTrainPosition(signal));
	}

	@Test
	public void testChecksummedSignalRejectedBadChecksum() {
		String signal = "RED 3;1;2" + ":" + Integer.toUnsignedString(0xB784A5A1);
		assertFalse(mbo.receiveTrainPosition(signal));
	}

	@Test
	public void testCalculateAuthority() {
		String signal = "RED 3;3;4" + ":" + Integer.toUnsignedString(0x96B81839);
		mbo.receiveTrainPosition(signal);
		double receivedAuthority = mbo.debug_getAuthority("RED 3");
		assertEquals(5.0, receivedAuthority, 0.001);
	}

	@Test 
	public void testAlwaysPasses() {
		assertEquals(1,1);
	}
}
