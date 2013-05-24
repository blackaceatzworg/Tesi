package new1;

import java.util.ArrayList;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

public class VehicleTicker {
	
	@ScheduledMethod(start=0, interval=1, priority=0)
	public void vehicleTurn(){
			this.projectVehiclesAnticipation();
			this.evaluateVehiclesAnticipation();
			this.moveVehicles();
			this.flushVehiclesAnticipation();
			this.controlVehiclesExit();
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
	public void moveVehicles(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			int displacement=veh.calcDisplacement();
			veh.move(displacement);
		}
	}
	public void flushVehiclesAnticipation(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			veh.getAnticipation().flushAnticipation();
		}
	}
	public void controlVehiclesExit(){
		final ArrayList<Vehicle> vehList=getVehList();
		for(final Vehicle veh:vehList){
			int x=veh.getGrid().getLocation(veh).getX();
			if((veh.getHeading()==Constants.E&&x>=Constants.GRID_LENGHT-1)||(veh.getHeading()==Constants.O&&x<=0)){
				ContextUtils.getContext(this).remove(veh);
			}
		}
	}

}
