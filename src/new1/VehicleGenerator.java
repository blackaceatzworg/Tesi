package new1;

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
//		Vehicle veh=new Vehicle(id, vehindex, vehindex, grid, vehindex, vehindex);
	}
	
	public void addVehicle(int numberOfLane){}


	
	
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
