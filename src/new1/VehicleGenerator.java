package new1;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;

public class VehicleGenerator {
	
	private int vehindex;
	private String id;
	private Context context;
	
	public VehicleGenerator(){
		
	}
	
	
	public void addVehicle(){
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		String id=this.getId()+"-"+this.getVehindex();
		this.setVehindex(this.getVehindex()+1);
		Random r=new Random();
		int curSpeed=r.nextInt(3);
		Vehicle veh=new Vehicle(id,curSpeed,Constants.E,grid,12,5);
		veh.getAnticipation().initAnticipation(id, grid, context);
		
		
//		Vehicle veh=new Vehicle(id, vehindex, vehindex, grid, vehindex, vehindex);
	}
	public void addVehicleToGrid(Vehicle vehicle){
		context.add(vehicle);
	}
	
	public void addVehicle(int numberOfLane){
		
	}


	
	
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
