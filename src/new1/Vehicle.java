package new1;

import java.util.ArrayList;

import repast.simphony.space.grid.Grid;

public class Vehicle {
	
	private Grid<Object> grid;
	private String id;
	private double currentSpeed;
	private double preferredSpeed;
	private int heading;
	private Anticipation anticipation;
	private VehicleShape vehicleCells;
	private boolean motionState;
	private ArrayList<String> route;
	private String currentField;
	private DestinationCell dest;
	
	////////////////CONSTRUCTOR
	public Vehicle(String id,double currentSpeed,int heading, Grid<Object> grid, int l, int w){
		this.setId(id);
		this.setCurrentSpeed(currentSpeed);
		this.setHeading(heading);
		this.setGrid(grid);
		this.setAnticipation(new Anticipation(Constants.ownerTypeVeh));
		this.setVehicleCells(new VehicleShape(l,w));
	}

	
	
	
	//GETTERS AND SETTERS
	////////////////////////////////////////////////////////////////
	public Grid<Object> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public double getPreferredSpeed() {
		return preferredSpeed;
	}

	public void setPreferredSpeed(double preferredSpeed) {
		this.preferredSpeed = preferredSpeed;
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}

	public Anticipation getAnticipation() {
		return anticipation;
	}

	public void setAnticipation(Anticipation anticipation) {
		this.anticipation = anticipation;
	}

	public VehicleShape getVehicleCells() {
		return vehicleCells;
	}

	public void setVehicleCells(VehicleShape vehicleCells) {
		this.vehicleCells = vehicleCells;
	}

	public boolean isMotionState() {
		return motionState;
	}

	public void setMotionState(boolean motionState) {
		this.motionState = motionState;
	}

	public ArrayList<String> getRoute() {
		return route;
	}

	public void setRoute(ArrayList<String> route) {
		this.route = route;
	}

	public String getCurrentField() {
		return currentField;
	}

	public void setCurrentField(String currentField) {
		this.currentField = currentField;
	}

	public DestinationCell getDest() {
		return dest;
	}

	public void setDest(DestinationCell dest) {
		this.dest = dest;
	}
	

}
