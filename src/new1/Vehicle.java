package new1;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;

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
	private String logFileName;//
	
	////////////////CONSTRUCTOR
	public Vehicle(String id,double currentSpeed,int heading, Grid<Object> grid, int l, int w){
		this.setId(id);
		this.setCurrentSpeed(currentSpeed);
		this.setHeading(heading);
		this.setGrid(grid);
		this.setPreferredSpeed(2);
		this.setAnticipation(new Anticipation(Constants.ownerTypeVeh));
		this.setVehicleCells(new VehicleShape(l,w));
	}
	
	public void accelerate(){
		if(this.getCurrentSpeed()>=0&&this.getCurrentSpeed()<this.getPreferredSpeed()){
			System.out.println("in accereleration: "+Math.round(this.getCurrentSpeed()));
			this.setCurrentSpeed(this.getCurrentSpeed()+Constants.accelerationModule);
		}
		
	}
	public void brake(double softbrakemodule){
		System.out.println("in decereleration: "+Math.round(this.getCurrentSpeed()));
		this.setCurrentSpeed(this.getCurrentSpeed()-softbrakemodule);
	}
	
	
	@ScheduledMethod(start=0,interval=1)
	public void movement(){
		Context context=ContextUtils.getContext(this);
		GridPoint position=grid.getLocation(this);
		int x=position.getX();
		int y=position.getY();
//		if(this.getCurrentSpeed()<2){
//		this.accelerate();}
		this.brake(Constants.softBrakeModule);
		int k=(int) Math.round(this.getCurrentSpeed());
		grid.moveTo(this,x+k,y);
	}
	
	///Derived from pedestrian
	public boolean checkOccupation(DestinationCell dc){
		boolean occupied=false;
		int x=dc.getX();
		int y=dc.getY();
		Context context=ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection(Constants.GridID);
		for(Object ags : env.getObjectsAt(x,y)){
			if(ags instanceof Vehicle){
					occupied=true;
//					System.out.println("Cella occupata"+dc.getX()+" "+dc.getY());
			}
//			else{
//				//System.out.println("Cella libera");
//			}
		}
		return occupied;
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
