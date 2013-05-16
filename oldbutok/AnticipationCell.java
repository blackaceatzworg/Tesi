package CrossingSiteModel;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;

public class AnticipationCell {
	
	private int index;
	private String ownerId;
	private String owner;
	private int x;
	private int y;
	private Vehicle vehicle;
	private Pedestrian pedestian;
	
	
	public AnticipationCell(int i, String s,Vehicle v, Pedestrian p){
		
		this.index=i;
		this.owner=s;
		this.vehicle=v;
		this.pedestian=p;
		
	}
	

	public void setAnticipationWeight(){
		Context<Object> context =ContextUtils.getContext(this);
		GridValueLayer anticipationField=(GridValueLayer)context.getValueLayer("AnticipationField");
		anticipationField.set(anticipationField.get(this.getX(),this.getY())+1,this.getX(),this.getY());
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}


	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}


	public Pedestrian getPedestian() {
		return pedestian;
	}


	public void setPedestian(Pedestrian pedestian) {
		this.pedestian = pedestian;
	}

}
