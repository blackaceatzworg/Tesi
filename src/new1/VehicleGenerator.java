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
	private String counterFileName;
	Parameters params=RunEnvironment.getInstance().getParameters();
	int numberOfLaneParam=(Integer)params.getValue("numberOfLaneParam");
	int numberOfVehicle=(Integer)params.getValue("numberOfVehicle");
	int anticipationModule=(Integer)params.getValue("anticipationModule");
	
	/**
	 * Vehicle generator constructor
	 * 
	 *@param id Vehicle generator id
	 *@param cont scenario context
	 *@param xgen coordinate on x-axis of vehicle single cell
	 *@param ygen coordinate on y-axis of vehicle single cell
	 *@param heading heading of vehicles, may be Constants.E or Constants.O 
	 * */
	public VehicleGenerator(String id, Context cont, int xgen, int ygen, int heading){
		this.vehindex=0;
		this.setId(id);
		this.context=cont;
		this.setXgen(xgen);
		this.setYgen(ygen);
		this.setHeading(heading);
//		this.counterFileName=counterFileName;
		
	}	
	//TODO vehicleShape
//	@ScheduledMethod(start=0, interval=1, priority=1)
	
	/**
	 * add vehicle to scenario
	 * */
	public void addVehicle(){
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		String id=this.getId()+"-"+this.getVehindex();
		Random r=new Random();
		int curSpeed=1;
		if(this.getHeading()==Constants.E){
			int antXGen=this.getXgen()+1;
			int yGen=14;
		}
		if(this.getVehindex()<numberOfVehicle){
			//TODO control position
			if(this.checkSpace()){
				this.setVehindex(this.getVehindex()+1);
				Vehicle veh=new Vehicle(id,curSpeed,this.getHeading(),grid,12,5);
				veh.getAnticipation().initVehicleAnticipation(veh.getId(), grid,context);
				veh.getAnticipation().setVehicleAnticipation(this.getHeading(),this.getXgen()+1,this.getYgen(),anticipationModule*5, veh.getSpeedZone());
				veh.getVehicleShape().initVehicleShape(12,5, veh.getId(), grid, context);
				veh.getVehicleShape().setVehicleShape(this.getXgen(),this.getYgen(), this.getHeading());
				context.add(veh);
				grid.moveTo(veh,this.getXgen(),this.getYgen());	
			}
		}
		
	}
	
	public void addVehicle(int numOfVeh){
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		String id=this.getId()+"-"+this.getVehindex();
		Random r=new Random();
		int curSpeed=1;
		if(this.getHeading()==Constants.E){
			int antXGen=this.getXgen()+1;
			int yGen=14;
		}
		if(this.getVehindex()<numOfVeh){
			//TODO control position
			if(this.checkSpace()){
				this.setVehindex(this.getVehindex()+1);
				Vehicle veh=new Vehicle(id,curSpeed,this.getHeading(),grid,12,5);
				veh.getAnticipation().initVehicleAnticipation(veh.getId(), grid,context);
				veh.getAnticipation().setVehicleAnticipation(this.getHeading(),this.getXgen()+1,this.getYgen(),anticipationModule*5, veh.getSpeedZone());
				veh.getVehicleShape().initVehicleShape(12,5, veh.getId(), grid, context);
				veh.getVehicleShape().setVehicleShape(this.getXgen(),this.getYgen(), this.getHeading());
				context.add(veh);
				grid.moveTo(veh,this.getXgen(),this.getYgen());	
			}
		}
		
	}
	
	/**
	 * 
	 * check if there is available space in starting area to add a new vehicle
	 * */
	public boolean checkSpace(){
		boolean freespace=true;
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		for(int i=14;i>=0;i--){
			for(Object obj:grid.getObjectsAt(i,6)){
				if(obj instanceof VehicleShapeCell){
//					System.out.println("vshapefound in "+i);
					freespace=false;
					break;
				}
			}
				
		}
		return freespace;
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
