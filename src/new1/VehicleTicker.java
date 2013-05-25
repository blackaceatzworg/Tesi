package new1;

import java.util.ArrayList;

import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;

public class VehicleTicker {
	
	@ScheduledMethod(start=0, interval=1, priority=0)
	public void vehicleTurn(){
			//this.projectVehiclesAnticipation();
			//this.moveVehicle();
			//this.flushVehiclesAnticipation();
		this.testVehicleShape();
		
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
	public void flushVehiclesAnticipation(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.getAnticipation().flushAnticipation();
		}
	}
	
	public void testVehicleShape(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			int x=veh.getGrid().getLocation(veh).getX();
			int y=veh.getGrid().getLocation(veh).getY();
			int heading=veh.getHeading();
			System.out.println(x+" "+y);
			veh.getVehicleCells().updateVehicleShape(x,y,heading);
		}
	}
	
	public void moveVehicle(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			int xl=veh.getGrid().getLocation(veh).getX();
			//anticipation safe
			if(xl<Constants.GRID_LENGHT-12){
				veh.project();
			}
			if(veh.getCurrentSpeed()<veh.getPreferredSpeed()){
				veh.speedUp();
				veh.move();
			}else{
				veh.speedDown();
				veh.move();
			}
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
