package new1;

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
	
	//PedTicker attributes
	ArrayList<DestinationCell> destinationChoices;  
	ArrayList<DestinationCell> destinationConflicts;
	
	//VehTicker attributes
	private int vehicleCount;
	private int pedCount;

	public AgentManager(){
		this.destinationChoices=new ArrayList<DestinationCell>();
		this.destinationConflicts=new ArrayList<DestinationCell>();
		this.setVehicleCount(0);
	}
	
	//simulation step
	@ScheduledMethod(start=0, interval=1, priority=0)
	public void simulationStep(){
		
		int tick=(int)RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
		//pedGen
		this.addPedestrian();
		
		//vehGen
		this.addVehicle();
		
		//vehicle
		this.evaluateVehiclesAnticipation();
		this.updateVehiclesAnticipation();
		this.moveVehicle();
		this.updateVehicleShape();
		
		//pedestrian
		this.solvePedConflict();
		this.updateAnticipationPeds();
//		this.evalPeds();
		if(tick%5==0){
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
		if(plist.size()==0||plist.size()<numOfped){
			for(final PedGenerator pg:list){
				pg.addPedestrian();
			}
		}
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
	
	public void flushVehiclesAnticipation(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.getAnticipation().flushAnticipation();
		}
	}
	
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
//			this.flushVehiclesShape();
		}
	}
	
	//Pedestrian turn method
	public void solvePedConflict(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			//ped.chooseDestination2();
			ped.setMotionstate(true);
			destinationChoices.add(ped.chooseDestination());
		}
		this.checkDestinationConflicts(destinationChoices);
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
						this.destinationConflicts.add(dc2);
						
						for(final Pedestrian ped:pedList){
							if(ped.getId().equals(dc2Id)){
								ped.setMotionstate(false);
							}
						}
					}else{
						this.destinationConflicts.add(dc);
						for(final Pedestrian ped:pedList){
							if(ped.getId().equals(dcId)){
								ped.setMotionstate(false);
							}
						}
					}
				}
			}
		}
	}
	
	public void updateAnticipationPeds(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			ped.project();
//			ped.getAnticipation().updatePedAnticipation(, y, ped.getHeading(), ped.getAnticipation().getAnticipationCells());
		}
	}
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
			}else{
				System.out.println(ped.getId()+" in conflict, stopped");
			}
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
	
	

}
