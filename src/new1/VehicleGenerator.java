package new1;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.space.grid.Grid;

public class VehicleGenerator {
	
	private int vehindex;
	private String id;
	private Context context;
	
	public VehicleGenerator(String id, Context cont){
		this.setId(id);
		this.context=cont;
	}
	
	//TODO vehicleShape
	@ScheduledMethod(start=0, interval=10)
	public void addVehicle(){
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		String id=this.getId()+"-"+this.getVehindex();
		this.setVehindex(this.getVehindex()+1);
		Random r=new Random();
		int curSpeed=r.nextInt(3);
		Vehicle veh=new Vehicle(id,curSpeed,Constants.E,grid,12,5);
		veh.getAnticipation().initAnticipation(id, grid, context);
		veh.getVehicleShape().initVehicleShape(12, 5, veh.getId(), grid, context);
		context.add(veh);
		grid.moveTo(veh,13,6);
	}
	
	public void addVehicle(int numberOfLane){
		
	}
	
//	@Watch(watcheeClassName="new1.Vehicle",watcheeFieldNames="removed",whenToTrigger=WatcherTriggerSchedule.LATER,scheduleTriggerDelta = 1)
//	public void addProcess(){
////		System.out.println("Veicolo uscito");
//		this.addVehicle();
//	}


	
	
	///getters and setters
	public int getVehindex() {
		return vehindex;
	}


	public void setVehindex(int vehindex) {
		this.vehindex = vehindex;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

}
