package new1;

import java.util.ArrayList;

import repast.simphony.engine.environment.RunState;

public class VehicleTicker {
	
	
	public ArrayList<Vehicle> getVehList(){
		@SuppressWarnings("unchecked")
		final Iterable<Vehicle> vehicles=RunState.getInstance().getMasterContext().getObjects(Vehicle.class);
		final ArrayList<Vehicle> vehList=new ArrayList<Vehicle>();
		for(final Vehicle veh:vehicles){
			vehList.add(veh);
		}
		return vehList;
	}
	
	public void projectVehiclesAnticipation(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.project();
		}
	}
	public void evaluateVehiclesAnticipation(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.evaluate();
		}
	}
	public void moveVehicle(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.move(0, 0);
		}
	}
	
	
	
//	public void activatePedestrians(){
//		final ArrayList<Pedestrian> pedList=getPedList();
//		for(final Pedestrian ped:pedList){
//			//ped.chooseDestination2();
//			ped.setMotionstate(true);
//			destinationChoices.add(ped.chooseDestination2());
////			this.checkDestinationConflicts(destinationChoices);
//		}
//		this.checkDestinationConflicts(destinationChoices);
//	}

}
