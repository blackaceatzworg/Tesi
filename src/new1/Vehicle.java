package new1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import bsh.This;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
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
	private VehicleShape vehicleShape;
	private boolean motionState;
	private ArrayList<String> route;
	private String currentField;
	private GridPoint dest;
	private String logFileName;//
	private int ticker;
	private boolean passed;
	private boolean removed;
	
	Parameters params=RunEnvironment.getInstance().getParameters();
	int anticipationModule=(Integer)params.getValue("anticipationModule");

	////////////////CONSTRUCTOR
	/**
	 * @param id vehicle id
	 * @param currenSpeed initial speed
	 * @param heading Vehicle's heading: it may be east or west
	 * @param grid context grid
	 * @param l vehicle's lenght
	 * @param w vehicle's width
	 * 
	 * */
	public Vehicle(String id,double currentSpeed,int heading, Grid<Object> grid, int l, int w){
		this.setId(id);
		this.setCurrentSpeed(currentSpeed);
		this.setHeading(heading);
		this.setGrid(grid);
		this.setPreferredSpeed(5);
		this.setAnticipation(new Anticipation(Constants.ownerTypeVeh));
		this.setVehicleShape(new VehicleShape());
		this.setTicker(0);
		this.setRemoved(false);
	}
	
	public Vehicle(String filename,String id,double currentSpeed,int heading, Grid<Object> grid, int l, int w){
		this.setId(id);
		this.setCurrentSpeed(currentSpeed);
		this.setHeading(heading);
		this.setGrid(grid);
		this.setPreferredSpeed(5);
		this.setAnticipation(new Anticipation(Constants.ownerTypeVeh));
		this.setVehicleShape(new VehicleShape());
		this.setTicker(0);
		this.logFileName=filename;
		this.setRemoved(false);
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
	///TEST
	
	
	
	
	////////////////MOTION
	public void accelerate(){
		this.speedUp();
	}
	
	public void brake(){
		this.speedDown();
	}
	
	/**
	 * Models the acceleration process. Each value is the speed increment for each speed zone
	 * */
	public void speedUp(){
		
		if(this.getCurrentSpeed()<=5){
			System.out.println(this.getId()+" accelera");
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
		this.setCurrentSpeed(this.getCurrentSpeed()+delta);}
//		return delta;
	}
	
	/**
	 * Models the deceleration process. Each value is the speed decrement for each speed zone
	 * */
	public void speedDown(){
		
		if(this.getCurrentSpeed()>0){
			System.out.println(this.getId()+" frena");
		double delta=0;
		if(this.getCurrentSpeed()>=0&&this.getCurrentSpeed()<1){
			delta=0.486;
		}if(this.getCurrentSpeed()>=1&&this.getCurrentSpeed()<2){
			delta=0.648;
		}if(this.getCurrentSpeed()>=2&&this.getCurrentSpeed()<3){
			delta=0.3888;
		}if(this.getCurrentSpeed()>=3&&this.getCurrentSpeed()<4){
			delta=0.2777;
		}
		//TODO valore
		if(this.getCurrentSpeed()>=4&&this.getCurrentSpeed()<=6){
			delta=0.324;
		}
		System.out.println(this.getCurrentSpeed());
		this.setCurrentSpeed(this.getCurrentSpeed()-delta);
		System.out.println(this.getCurrentSpeed()+" dopo frenata");
		}
		
//		return delta;
	}
	
	
	/**
	 * Calculate the displacement related to speed.
	 * 
	 * */
	 public int calcDisplacement(){
		 int currentTick=(int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		 int displacement=0;
		 int x=grid.getLocation(this).getX();
		 int y=grid.getLocation(this).getY();
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
	
	//TODO direzione inversa
	 
	 /**
	  * Move by displacemente related to speed,
	  * 
	  * */
	public void move(){
		System.out.println(this.getId()+" move");
		int delta=this.calcDisplacement();
		int x=(grid.getLocation(this).getX()+delta)%(Constants.GRID_LENGHT);
		int y=grid.getLocation(this).getY();
		try{
			grid.moveTo(this,x,y);
			
		}catch(Exception e){
			System.out.print("da rimuovere");
			this.setRemoved(true);
//			grid.moveTo(this,13,y);
		}
	}
	
//	//TODO generalizzazione
//	public void checkPassed(){
//		if(this.isPassed()){
//			for(Object ags : grid.getObjectsAt(0,7)){
//				if(ags instanceof Vehicle){
//					System.out.println("veicolo presente");
//				}else{
//					this.setRemoved(true);
//					ContextUtils.getContext(this).remove(this);
//				}
//			}
//			
//		}
//	}
	
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
		int anticipationLenght=this.calcAnticipationLenght(speed);
//		System.out.println(this.getCurrentSpeed());
		this.getAnticipation().updateVehicleAnticipation(this.getHeading(), x, y, anticipationLenght,speed);
	}
	public int calcAnticipationLenght(int speed){
		int antValue=anticipationModule;
		switch(speed){
		case 0:
			//antValue=16;
			break;
		case 1:
			antValue*=1;
			break;
		case 2:
			antValue*=2;
			break;
		case 3:
			antValue*=3;
			break;
		case 4:
			antValue*=4;
			break;
		case 5:
			antValue*=5;
			break;
		}
		return antValue;
	}
	
	public void evaluate(){
		boolean freeride=true;
		for(AnticipationCell ac:this.getAnticipation().getAnticipationCells()){
			int x=ac.getGp().getX();
			int y=ac.getGp().getY();
			for(Object ags : grid.getObjectsAt(x,y)){
				if(ags instanceof Vehicle){
					freeride=false;
					System.out.println("veicolo "+((Vehicle)ags).getId()+" rilevato in fascia "+ac.getIndex()+" con velocitˆ"+((Vehicle)ags).getCurrentSpeed());
					this.manageVehiclePresence(ac.getIndex());
//					break;//TODO ???lasciare?
				}
//				if(ags instanceof Pedestrian){
//					freeride=false;
//					System.out.println("pedone rilevato in fascia di velocitˆ:"+ac.getIndex());
//				}
//				if(ags instanceof AnticipationCell){
//					if(!((AnticipationCell) ags).getOwner().equals(this.getId())){
//						freeride=false;
//						System.out.println("anticipazione rilevata in "+x+" "+y+" indice fascia di velocitˆ:"+ac.getOwner());
//					}
//					if(((AnticipationCell) ags).getOwnerType().equals("Pedestrian")){
//						System.out.println("anticipazione pedone rilevata in "+x+" "+y+" indice fascia di velocitˆ:"+ac.getIndex());
//					}	
//				}
				if(ags instanceof StoppedPed){
					System.out.println("fascia di v:"+this.getSpeedZone()+", pedone rilevato in fascia di velocitˆ:"+ac.getIndex());
					freeride=false;
					this.managePedestrianPresence(ac.getIndex());
				}
			}
		}
		if(freeride){
			System.out.println("free");
			this.speedUp();
		}
		
		
	}
	
	public void manageVehiclePresence(int cellIndex){
		 
		//valore intero di velocitˆ
		int speedZone=this.getSpeedZone();
		//cambio tra fasce di anticipazione
		 switch(speedZone){
		 case 0:
			 this.speedUp();
			 break;
		 case 1:
			 if(cellIndex==1){
				 this.speedDown();
			 }else{
				 this.speedUp();
			 }
			 break;
		 case 2:
			 if(cellIndex==1){
				 this.speedDown();
			 }else if(cellIndex>2){
				 this.speedUp();
			 }
			 break;
		 case 3:
			 if(cellIndex==1||cellIndex==2){
				 this.speedDown();
			 }else if(cellIndex>2){
				 this.speedUp();
			 }
			 break;
		 case 4:
			 if(cellIndex==1||cellIndex==2){
				 this.speedDown();
			 }else if(cellIndex>3){
				 this.speedUp();
			 }
			 break;
		 case 5:
			 if(cellIndex==1||cellIndex==2||cellIndex==3){
				 this.speedDown();
			 }else if(cellIndex>4){
				 this.speedUp();
			 }
		 }
	}
	
	public void managePedestrianPresence(int cellIndex){
		 int speedZone=this.getSpeedZone();
		 switch(speedZone){
		 case 0:
			 break;
		 case 1:
			 if(cellIndex!=1){
				 this.speedUp();
			 }
			 break;
		 case 2:
			 if(cellIndex==1){
				 this.speedDown();
				 }
			 break;
		 case 3:
			 if(cellIndex==1||cellIndex==2){
				 this.speedDown();
			 }
			 break;
		 case 4:
			 if(cellIndex==1||cellIndex==2||cellIndex==3){
				 this.speedDown();
			 }
//			 if(cellIndex==4){
//				 this.speedUp();
//			 }
			 break;
		 case 5:
			 this.speedDown();
			 break;
		 
		 }
			 
	}
	
	public void manageAnticipationPresence(int cellIndex){
		 
	}
	
	
	public int getSpeedZone(){
		int val=(int) (Math.round(this.getCurrentSpeed()*100)/100);
		return val;
	}
	///Derived from pedestrian
	public boolean checkOccupation(GridPoint dc){
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

	public VehicleShape getVehicleShape() {
		return vehicleShape;
	}

	public void setVehicleShape(VehicleShape vehicleCells) {
		this.vehicleShape = vehicleCells;
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

	public GridPoint getDest() {
		return dest;
	}

	public void setDest(GridPoint dest) {
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

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	

}
