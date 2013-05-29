package new1;

import java.util.ArrayList;

import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

public class VehicleTicker {
	
	@ScheduledMethod(start=0, interval=2, priority=0)
	public void vehicleTurn(){

			this.updateVehiclesAnticipation();
//			this.evaluateVehiclesAnticipation();
//			this.flushVehiclesAnticipation();
			
			this.flushVehiclesShape();
			this.moveVehicle();
////			this.logVehicles();
			this.updateVehicleShape();
			

	}
	

	
	
	public ArrayList<Vehicle> getVehList(){
		@SuppressWarnings("unchecked")
		final Iterable<Vehicle> vehicles=RunState.getInstance().getMasterContext().getObjects(Vehicle.class);
		final ArrayList<Vehicle> vehList=new ArrayList<Vehicle>();
		for(final Vehicle veh:vehicles){
			vehList.add(veh);
		}
		return vehList;
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
	public void logVehicles(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.logArrival();
//			this.flushVehiclesShape();
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
