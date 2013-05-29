package new1;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;

public class VehicleGenerator {
	
	private int vehindex;
	private String id;
	private Context context;
	private int xgen;
	private int ygen;
	private int heading;
	Parameters params=RunEnvironment.getInstance().getParameters();
	int numberOfLaneParam=(Integer)params.getValue("numberOfLaneParam");
	int numberOfVehicle=(Integer)params.getValue("numberOfVehicle");
	
	public VehicleGenerator(String id, Context cont, int xgen, int ygen, int heading){
		this.setId(id);
		this.context=cont;
		this.setXgen(xgen);
		this.setYgen(ygen);
		this.setHeading(heading);
	}
	
//	Vehicle veh1=new Vehicle("v1",0,Constants.O,grid,12,5);
//	veh1.getAnticipation().initVehicleAnticipation(veh1.getId(), grid, context);
//	veh1.getAnticipation().setVehicleAnticipation(veh1.getHeading(),159,14,80, veh1.getSpeedZone());
//	veh1.getVehicleShape().initVehicleShape(12,5, veh1.getId(), grid, context);
//	veh1.getVehicleShape().setVehicleShape(160,14,veh1.getHeading());
//	context.add(veh1);
//	grid.moveTo(veh1,160,14);
//////
//	
//	Vehicle veh2=new Vehicle("v2",2,Constants.E,grid,12,5);
//	veh2.getAnticipation().initVehicleAnticipation(veh2.getId(), grid,context);
//	veh2.getAnticipation().setVehicleAnticipation(veh2.getHeading(),44,6,80, veh2.getSpeedZone());
//	veh2.getVehicleShape().initVehicleShape(12,5, veh2.getId(), grid, context);
//	veh2.getVehicleShape().setVehicleShape(43,6, Constants.E);
//	context.add(veh2);
//	grid.moveTo(veh2,43,6);
//	
	
	
	//TODO vehicleShape
	@ScheduledMethod(start=0, interval=1)
	public void addVehicle(){
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		String id=this.getId()+"-"+this.getVehindex();
		Random r=new Random();
		int curSpeed=r.nextInt(3);
		while(this.getVehindex()<numberOfVehicle){
			//TODO control position
			this.setVehindex(this.getVehindex()+1);
			Vehicle veh=new Vehicle(id,curSpeed,this.getHeading(),grid,12,5);
			veh.getAnticipation().initVehicleAnticipation(veh.getId(), grid,context);
			veh.getAnticipation().setVehicleAnticipation(this.getHeading(),this.getXgen()+1,this.getYgen(),80, veh.getSpeedZone());
			veh.getVehicleShape().initVehicleShape(12,5, veh.getId(), grid, context);
			veh.getVehicleShape().setVehicleShape(this.getXgen(),this.getYgen(), this.getHeading());
			context.add(veh);
			grid.moveTo(veh,this.getXgen(),this.getYgen());
		}
		
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

	public int getXgen() {
		return xgen;
	}

	public void setXgen(int xgen) {
		this.xgen = xgen;
	}

	public int getYgen() {
		return ygen;
	}

	public void setYgen(int ygen) {
		this.ygen = ygen;
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}

}
