package new1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import bsh.This;

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
	private boolean passed;

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
	
	public Vehicle(String filename,String id,double currentSpeed,int heading, Grid<Object> grid, int l, int w){
		this.setId(id);
		this.setCurrentSpeed(currentSpeed);
		this.setHeading(heading);
		this.setGrid(grid);
		this.setPreferredSpeed(5);
		this.setAnticipation(new Anticipation(Constants.ownerTypeVeh));
		this.setVehicleCells(new VehicleShape(l,w));
		this.setTicker(0);
		this.logFileName=filename;
	}
	
	////LOG
	public void logArrival(){
		PrintStream p=null;
		int passedTime=(int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if(this.isPassed()){
		try {
			p = new PrintStream(new FileOutputStream(this.logFileName,true));
			p.println(this.getId()+","+passedTime);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}}
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
	
//	@ScheduledMethod(start=0,interval=2)
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
	@ScheduledMethod(start=0,interval=2)
	public void test_eval_anticipation(){
		this.project();
		this.evaluate();
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
	
	public void checkArrival(int x){
		
	}
	
////////////////PERCEPTION
	
	/**
	 * Update vehicle anticipation given the vehicle's heading and speed.
	 * Anticipation length is determined by speed value, calling calcAnticipation(speed)
	 * 
	 * */
	public void project(){
		int speed=(int)this.getCurrentSpeed();
//		System.out.println(speed);
		int x=grid.getLocation(this).getX()+1;
		int y=grid.getLocation(this).getY();
		int anticipationLenght=this.calcAnticipation(speed);
//		System.out.println(this.getCurrentSpeed());
		this.getAnticipation().updateVehicleAnticipation(this.getHeading(), x, y, anticipationLenght,speed);
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
		return antValue;
	}
	
	public void evaluate(){
		for(AnticipationCell ac:this.getAnticipation().getAnticipationCells()){
			int x=ac.getGp().getX();
			int y=ac.getGp().getY();
//			System.out.print(x+" "+ac.getIndex());
//			System.out.println("");
			for(Object ags : grid.getObjectsAt(x,y)){
				if(ags instanceof Pedestrian){
					System.out.println("pedone rilevato in "+x+" "+y+" indice fascia di velocitˆ:"+ac.getIndex());
				}
				if(ags instanceof Vehicle){
					System.out.println("veicolo rilevato in "+x+" "+y+ac.getOwnerType());
				}
				if(ags instanceof AnticipationCell){
					if(!((AnticipationCell) ags).getOwner().equals(this.getId())){
						System.out.println("anticipazione rilevata in "+x+" "+y+" indice fascia di velocitˆ:"+ac.getOwner());
					}
				}
				if(ags instanceof StoppedPed){
					System.out.println("pedone fermo in "+x+" "+y+ac.getIndex()+" "+((StoppedPed) ags).getId());
				}
			}
		}
		
		
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

	public int getTicker() {
		return ticker;
	}

	public void setTicker(int ticker) {
		this.ticker = ticker;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	

}
