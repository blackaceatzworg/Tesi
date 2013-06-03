package new1;

public final class Constants {
	
	public static int maxint=100;
	
	//grid values
	public static final int GRID_LENGHT=1000;//500
	public static final int DOUBLE_GRID_HEIGHT=26;
	public static final int SINGLE_GRID_HEIGHT=18;//18
	public static final int CURB_HEIGHT=1;
	public static final int WALKWAY_HEIGHT=4;
	public static final int ROAD_HEIGHT=8;
	public static final int PED_ZONE_HEIGHT=WALKWAY_HEIGHT+CURB_HEIGHT;
	
	public static String ContextID="ScenarioBuilder";
	public static String GridID="ScenarioGrid";
	static int vehicleCounter;
	static int crossedPedCounter;
	
	public static final int speedUpDecision=1;
	public static final int speedDownDecision=2;
	
	public int getVehicleCounter(){
		return this.vehicleCounter;
	}
	
	//floor fields ids
	public static final  String northCurbFF="nordCurbFF";
	public static final  String southCurbFF="southCurbFF";
	public static final  String destinationFF="destinationFF";
	
	//destination area ids
	public static final String NorthDestFF="northDestination";
	public static final String southDestFF="southDestination";
	public static final String southEastDest="southEastDestination";
	public static final String southWestDest="southWestDestination";
	public static final String northEastDest="northEastDestination";
	public static final String northWestDest="northWestDestination";
	public static final String nordCurbDest="nordCurbDestination";
	public static final String southCurbDest="southCurbDestination";
	public static final String EastDest="EastDestination";
	public static final String WestDest="WestDestination";
	
	//Surface Type
	public static final int Walkway=1;
	public static final int Roadway=2;
	public static final int Curb=3;
	public static final int ZebraCrossing=4;
	
	//HEADINGS VALUE
	public static final int SO=0;
	public static  final int O=1;
	public static  final int NO=2;
	public static  final int S=3;
	public static  final int N=5;
	public static  final int SE=6;
	public static  final int E=7;
	public static  final int NE=8;
	
	public static final String ownerTypePed="ped";
	public static final String ownerTypeVeh="vehicle";
	
	public static final double hardBrakeModule=0.1;
	public static final double softBrakeModule=0.05;
	public static final double accelerationModule=0.05;
	
	

}
