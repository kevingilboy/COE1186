public class PLC{
	//PLC Variables
	private String lightLogic;
	private String switchLogic;
	private String crossingLogic;
	
	public PLC(){
		
	}
	
	public boolean parsePLC(String plcPath){
		System.out.println("parsing file: " + plcPath);
		/*BufferedReader 	br 			= null;
		String 			currline 	= "";
		String 			delimeter 	= ":";

		try {
			br = new BufferedReader(new FileReader(path));
			String [] plcString = new String[8];
			int location = 0;
			
			// Read from plc file and create logic for each line, then
			// add each statement to an arraylist of logic statements

			while ((currline = br.readLine()) != null){
				String [] logicStatement = currline.split(delimeter);
				location++;
				
				String 	logicFor 		= logicStatement[0].replaceAll("\\s+","");
				String 	logic 			= logicStatement[1].replaceAll("\\s+","");
				
				plcString[location-1] = logicFor;
				plcString[location] = logic;
					
				System.out.println(logicFor);
				System.out.println(logic);
			}
			newPlc = new Plc(plcString[0], plcString[1], plcString[2], plcString[3], plcString[4], plcString[5], plcString[6], plcString[7]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
		return true;
	}
	
	public String getLightLogic(){
		return lightLogic;
	}
	
	public String getSwitchLogic(){
		return switchLogic;
	}
	
	public String getCrossingLogic(){
		return crossingLogic;
	}
}