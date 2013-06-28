package new1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;

public class AgentManager {
	
	//model parameters
	Parameters params=RunEnvironment.getInstance().getParameters();
	int numOfped=(Integer)params.getValue("numOfPed");
	int numberOfLaneParam=(Integer)params.getValue("numberOfLaneParam");
	int numberOfVehicle=(Integer)params.getValue("numberOfVehicle");
	int anticipationModule=(Integer)params.getValue("anticipationModule");
	int numberOfPedestrian=(Integer)params.getValue("numOfPed");
	int pedOffsetGeneration=(Integer)params.getValue("pedx");
	
	private int interval;
	
	public int getOffSet(){
		return pedOffsetGeneration;
	}
	public int getNumberOfPed(){
		return numberOfPedestrian;
	}
	public int getNumberOfVehicle(){
		return numberOfVehicle;
	}
	
	//PedTicker attributes
	ArrayList<DestinationCell> destinationChoices;  
	ArrayList<DestinationCell> destinationConflicts;
	private String filename;
	
	//VehTicker attributes
	private int vehicleCount;
	private int pedCount;

	public AgentManager(String filename){
		this.destinationChoices=new ArrayList<DestinationCell>();
		this.destinationConflicts=new ArrayList<DestinationCell>();
		this.setVehicleCount(0);
		this.setFilename(filename);
		this.setInterval(-1);
	}
	
	/**
	 * Constructor for time dynamic scenario
	 * */
	public AgentManager(String filename, int interval){
		this.destinationChoices=new ArrayList<DestinationCell>();
		this.destinationConflicts=new ArrayList<DestinationCell>();
		this.setVehicleCount(0);
		this.setFilename(filename);
		this.setInterval(interval);
	}
	
	//simulation step
	int pedOffSet=0;
	int numVeh=0;
	@ScheduledMethod(start=0, interval=1, priority=0)
	public void simulationStep(){
		
		int tick=(int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if(tick==13333){
			this.logCross();
		}
//		if(tick%this.getInterval()==0&&tick<13333){
////			this.setInterval(this.getInterval()+tick);
//			this.logCross();
//			pedOffSet=pedOffSet+5;
//			numVeh=numVeh+3;
////			System.out.println(tick+" "+numVeh+" "+pedOffSet);
//		}else if(tick%this.getInterval()==0){
//			if(pedOffSet>0){
//				pedOffSet=pedOffSet-5;
//			}
//		}
		
//		System.out.println("tick--- "+tick);
		//pedGen
		this.addPedestrian();
		
		//vehGen
		this.addVehicle();
		
		this.evaluateVehiclesAnticipation();
		this.updateVehiclesAnticipation();
		
		this.solvePedConflict();
		this.updatePedAnticipation();
		this.evalPeds();
//		this.test_updatePedAnticipation();
	
		this.moveVehicle();
		this.updateVehicleShape();
		
		
		
		// was 5
		if(tick%1==0){
		this.movePedestrians();}
	}
	
	
	//Agent retrieving methods
	public ArrayList<Vehicle> getVehList(){
		@SuppressWarnings("unchecked")
		final Iterable<Vehicle> vehicles=RunState.getInstance().getMasterContext().getObjects(Vehicle.class);
		final ArrayList<Vehicle> vehList=new ArrayList<Vehicle>();
		for(final Vehicle veh:vehicles){
			vehList.add(veh);
		}
		return vehList;
	}
	
	public ArrayList<PedGenerator> getPedGenList(){
		@SuppressWarnings("unchecked")
		final Iterable<PedGenerator> gens=RunState.getInstance().getMasterContext().getObjects(PedGenerator.class);
		final ArrayList<PedGenerator> List=new ArrayList<PedGenerator>();
		for(final PedGenerator gen:gens){
			List.add(gen);
		}
		return List;
	}
	
	public ArrayList<VehicleGenerator> getVehGenList(){
		@SuppressWarnings("unchecked")
		final Iterable<VehicleGenerator> gens=RunState.getInstance().getMasterContext().getObjects(VehicleGenerator.class);
		final ArrayList<VehicleGenerator> List=new ArrayList<VehicleGenerator>();
		for(final VehicleGenerator gen:gens){
			List.add(gen);
		}
		return List;
	}
	
	public ArrayList<VehicleShapeCell> getVehShapeCelList(){
		@SuppressWarnings("unchecked")
		final Iterable<VehicleShapeCell> vehicles=RunState.getInstance().getMasterContext().getObjects(VehicleShapeCell.class);
		final ArrayList<VehicleShapeCell> vehList=new ArrayList<VehicleShapeCell>();
		for(final VehicleShapeCell veh:vehicles){
			vehList.add(veh);
		}
		return vehList;
	}
	public ArrayList<Pedestrian> getPedList(){
		@SuppressWarnings("unchecked")
		final Iterable<Pedestrian> peds=RunState.getInstance().getMasterContext().getObjects(Pedestrian.class);
		final ArrayList<Pedestrian> pedList=new ArrayList<Pedestrian>();
		for(final Pedestrian ped:peds){
			pedList.add(ped);
		}
		return pedList;
	}
	
	//agent generators turn
	public void addPedestrian(){
		final ArrayList<PedGenerator> list=this.getPedGenList();
		final ArrayList<Pedestrian> plist=this.getPedList();
		if(numOfped>0){
		if(plist.size()==0||plist.size()<numOfped){
			for(final PedGenerator pg:list){
				pg.addPedestrian();
			}
		}}
	}
	
	public void addVehicle(){
		final ArrayList<VehicleGenerator> list=this.getVehGenList();
			for(final VehicleGenerator pg:list){
				pg.addVehicle();
			}
	}
	
	
	
	//Vehicle turn method
	public void updateVehiclesAnticipation(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.updateAnticipation();
		}
	}
	public void evaluateVehiclesAnticipation(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.evaluate();
		}
	}
	
//	public void flushVehiclesAnticipation(){
//		final ArrayList<Vehicle> vehList=getVehList();
//		for(final Vehicle veh:vehList){
//			veh.getAnticipation().flushAnticipation();
//		}
//	}
	
	public void updateVehicleShape(){
//		this.flushVehiclesAnticipation();
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.getVehicleShape().alternateUpdate(veh.calcDisplacement(),veh.getHeading());
		}
	}
	
	public void flushVehiclesShape(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.getVehicleShape().clearShape();
		}
	}
	
	public void moveVehicle(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.move();
		}
	}
	
	//Pedestrian turn method
	
	ArrayList<Pedestrian> stopped;
	public void solvePedConflict(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			ped.setMotionstate(false);
			destinationChoices.add(ped.chooseDestination());
		}
		this.checkDestinationConflicts(destinationChoices);
		destinationChoices.removeAll(destinationChoices);
	}
	
	
	/**
	 * Solve conflict among pedestrians.
	 * */
	public void checkDestinationConflicts(ArrayList<DestinationCell> dcList){
		final ArrayList<Pedestrian> pedList=getPedList();
		Random r=new Random();
		boolean move=r.nextBoolean();
		for(int i=0;i<dcList.size();i++){
			for(int j=i+1;j<dcList.size();j++){
				DestinationCell dc=dcList.get(i);
				DestinationCell dc2=dcList.get(j);
				int dcx= dc.getX();
				int dcy= dc.getY();
				String dcId=dc.getPedId();
				int dc2x= dc2.getX();
				int dc2y= dc2.getY();
				String dc2Id=dc2.getPedId();
				if(dcx==dc2x&&dcy==dc2y&&!dcId.equals(dc2Id)){
					if(move){
						dcList.remove(dc2);
					}else{
						dcList.remove(dc);
					}
					
				}
			}
		}
		for(DestinationCell dc:dcList){
			for(Pedestrian ped:this.getPedList()){
				if(dc.getPedId().equals(ped.getId())){
					ped.setMotionstate(true);
				}
			}
		}
	}
	
	public void updatePedAnticipation(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			ped.project();
		}
	}
	
//	public void test_updatePedAnticipation(){
//		final ArrayList<Pedestrian> pedList=getPedList();
//		for(final Pedestrian ped:pedList){
//			ped.testProjection();
//		}
//	}
	public void evalPeds(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			ped.evaluate();
		}
	}
	public void movePedestrians(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			if(ped.isMotionstate()){
			ped.movement();
			}
//			else{
//				System.out.println(ped.getId()+" in conflict, stopped");
//			}
		}
		this.destinationChoices.removeAll(destinationChoices);
		this.destinationConflicts.removeAll(destinationConflicts);
		
	}
	
	
	public int getVehCount(){
		return Constants.vehicleCounter;
	}
	public int getPedCount(){
		return Constants.crossedPedCounter;
	}
	
	public int getVehicleCount() {
		return vehicleCount;
	}

	public void setVehicleCount(int vehicleCount) {
		this.vehicleCount = vehicleCount;
	}
	public void logCross(){
		int tick=(int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		PrintStream p=null;
		try {
			p = new PrintStream(new FileOutputStream(this.getFilename(),true));
			p.println(tick+" "+Constants.vehicleCounter+" "+Constants.crossedPedCounter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	

}
