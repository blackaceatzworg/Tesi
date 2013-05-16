package CrossingSiteModel;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;

public class VehicleShape {
	
	private int index;
	private String ownerId;
	private String owner;
	private int x;
	private int y;
	private Vehicle vehicle;
	
	
	public VehicleShape(Vehicle v, int i, int j){
		this.setOwner("vehicle");
		this.setOwnerId(v.getID());
		this.setIndex(index);
		this.setX(i);
		this.setY(j);
		this.setVehicle(v);
	}
	
	
	
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}



	public int getX() {
		return x;
	}



	public void setX(int x) {
		this.x = x;
	}



	public int getY() {
		return y;
	}



	public void setY(int y) {
		this.y = y;
	}





	public Vehicle getVehicle() {
		return vehicle;
	}





	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

}
