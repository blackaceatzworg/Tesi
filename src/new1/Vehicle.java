package new1;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
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
	private int ticker;

	////////////////CONSTRUCTOR
	public Vehicle(String id,double currentSpeed,int heading, Grid<Object> grid, int l, int w){
		this.setId(id);
		this.setCurrentSpeed(currentSpeed);
		this.setHeading(heading);
		this.setGrid(grid);
		this.setPreferredSpeed(5);
		this.setAnticipation(new Anticipation(Constants.ownerTypeVeh));
		this.setVehicleCells(new VehicleShape(l,w));
		this.setTicker(0);
	}
	
	///test
//	public void test_accelerate(){
//	this.gearUp();
//	int x=grid.getLocation(this).getX()+this.calcDisplacement();
//	int y=grid.getLocation(this).getY();
//	grid.moveTo(this,x,y);
//}
////@ScheduledMethod(start=0,interval=1)
//public void test_brake(){
////	System.out.println(this.getCurrentSpeed()+" "+this.calcDisplacement()+" ,freno");
//	this.gearDown();
//	System.out.println(this.getCurrentSpeed()+" "+this.calcDisplacement());
//	int x=grid.getLocation(this).getX()+this.calcDisplacement();
//	int y=grid.getLocation(this).getY();
//	System.out.println(x);
//	grid.moveTo(this,x,y);
//}
	
////	@ScheduledMethod(start=0,interval=1)
//	public void test(){
//		Context context=ContextUtils.getContext(this);
//		GridPoint position=grid.getLocation(this);
//		int x=position.getX();
//		int y=position.getY();
////		if(this.getCurrentSpeed()<2){
////		this.accelerate();}
////		this.brake(Constants.softBrakeModule);
////		int k=(int) Math.round(this.getCurrentSpeed());
////		this.getAnticipation().updateVehicleAnticipation(this.getHeading(), x-1, y, 16);
//		//grid.moveTo(this,x+1,y);
//	}
	
	@ScheduledMethod(start=0,interval=2)
	public void test_1a(){
		this.project();
		if(this.getCurrentSpeed()<this.getPreferredSpeed()){
			this.gearUp();
			int delta=this.calcDisplacement();
			int x=grid.getLocation(this).getX()+delta;
			int y=grid.getLocation(this).getY();
			this.move(x, y);
		}else{
			this.gearDown();
			int delta=this.calcDisplacement();
			int x=grid.getLocation(this).getX()+delta;
			int y=grid.getLocation(this).getY();
			this.move(x, y);
		}
	}
	
	@ScheduledMethod(start=1,interval=2)
	public void test_1b(){
		this.getAnticipation().flushAnticipation();
	}
	
	
	////////////////MOTION
	public void accelerate(){
		this.gearUp();
	}
	
	public void brake(){
		this.gearDown();
	}
	
	/**
	 * Models the acceleration process. Each value is the speed increment for each speed zone
	 * */
	public void gearUp(){
		double delta=0;
		if(this.getCurrentSpeed()>=0&&this.getCurrentSpeed()<1){
			delta=0.3645;
		}if(this.getCurrentSpeed()>=1&&this.getCurrentSpeed()<2){
			delta=0.486;
		}if(this.getCurrentSpeed()>=2&&this.getCurrentSpeed()<3){
			delta=0.2916;
		}if(this.getCurrentSpeed()>=3&&this.getCurrentSpeed()<4){
			delta=0.2082;
		}if(this.getCurrentSpeed()>=4&&this.getCurrentSpeed()<5){
			delta=0.243;
		}
		this.setCurrentSpeed(this.getCurrentSpeed()+delta);
//		return delta;
	}
	
	/**
	 * Models the deceleration process. Each value is the speed decrement for each speed zone
	 * */
	public void gearDown(){
		double delta=0;
		if(this.getCurrentSpeed()>=0&&this.getCurrentSpeed()<1){
			delta=0.486;
		}if(this.getCurrentSpeed()>=1&&this.getCurrentSpeed()<2){
			delta=0.648;
		}if(this.getCurrentSpeed()>=2&&this.getCurrentSpeed()<3){
			delta=0.3888;
		}if(this.getCurrentSpeed()>=3&&this.getCurrentSpeed()<4){
			delta=0.2777;
		}if(this.getCurrentSpeed()>=4&&this.getCurrentSpeed()<=5){
			delta=0.324;
		}
		this.setCurrentSpeed(this.getCurrentSpeed()-delta);
//		return delta;
	}
	
	
	/**
	 * Calculate the displacement related to speed.
	 * 
	 * */
	 public int calcDisplacement(){
		 int currentTick=(int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		 int displacement=0;
		 int val=(int) (Math.round(this.getCurrentSpeed()*100)/100);
		 switch(val){
		 case 1:
			 if(currentTick%2==0){
				 displacement=1;
			 }else{
				 displacement=2;
			 }
			 return displacement;
		 case 2:
			 if(currentTick%2==0){
				 displacement=2;
			 }else{
				 displacement=3;
			 }
			 return displacement;
		 case 3:
			 if(currentTick%2==0){
			 displacement=3;
		 	}else{
		 		displacement=4;
		 	}
			 return displacement;
		 case 4:
			 displacement=7;
			 return displacement;
		 case 5: 
			 displacement=9;
			 return displacement;
		 }
		 return displacement;
	 }
	 
	public DestinationCell chooseDestination(){
		return this.getDest();//TODO
	}
	
	public void move(int x, int y){
		grid.moveTo(this,x,y);
		//TODO flush anticipation
		//TODO update shape
	}
	
////////////////PERCEPTION
	public void project(){
		int speed=(int)this.getCurrentSpeed();
		int x=grid.getLocation(this).getX()+1;
		int y=grid.getLocation(this).getY();
		int anticipationLenght=this.calcAnticipation(speed);
		System.out.println(this.getCurrentSpeed());
		this.getAnticipation().updateVehicleAnticipation(this.getHeading(), x, y, anticipationLenght);
	}
	public int calcAnticipation(int speed){
		int antValue=16;
		switch(speed){
		case 0:
			antValue=16;
			break;
		case 1:
			antValue=32;
			break;
		case 2:
			antValue=48;
			break;
		case 3:
			antValue=64;
			break;
		case 4:
			antValue=80;
			break;
		case 5:
			antValue=96;
			break;
		}
		if(speed>5){
			this.setCurrentSpeed(0);
		}
		return antValue;
	}
	
	public void evaluate(){}
	
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

	public int getTicker() {
		return ticker;
	}

	public void setTicker(int ticker) {
		this.ticker = ticker;
	}
	

}
