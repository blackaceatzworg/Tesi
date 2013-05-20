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
		this.setPreferredSpeed(2);
		this.setAnticipation(new Anticipation(Constants.ownerTypeVeh));
		this.setVehicleCells(new VehicleShape(l,w));
		this.setTicker(0);
	}
	
	public void accelerate(){
		if(this.getCurrentSpeed()>=0&&this.getCurrentSpeed()<this.getPreferredSpeed()){
			System.out.println("in accereleration: "+Math.round(this.getCurrentSpeed()));
			this.setCurrentSpeed(this.getCurrentSpeed()+Constants.accelerationModule);
		}
	}
	@ScheduledMethod(start=0,interval=1)
	public void test_accelerate(){
		this.setTicker(this.getTicker()+1);
		if(this.getCurrentSpeed()<5){
		this.setCurrentSpeed(this.getCurrentSpeed()+0.5);}
		int delta=(int) Math.round(this.getCurrentSpeed());
		int delta1=this.speedSelector(4);
		System.out.println(delta+" "+this.getCurrentSpeed()+" "+delta1);
		int x=grid.getLocation(this).getX()+delta1;
		int y=grid.getLocation(this).getY();
		//DestinationCell dest=new DestinationCell(new GridPoint(x,y),0,Constants.E);
		this.move(x,y);
	}
	
	public int speedSelector(int val){
		int tick=(int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		switch(val){
		case 1:
			if(this.getTicker()==4){
				this.setTicker(0);
				return 1;
			}else{
				return 0;
			}
			//break;
		case 2:
			
			if(tick%2==0){
				return 1;
			}else{
				return 0;
			}
//			break;
		case 3:
			return 1;
		case 4:
			if(tick%2==0){
				return 2;
			}else{
				return 1;
			}
		case 5:
			return 2;
		}
		
		
		return 0;
	}
	public void brake(double softbrakemodule){
		System.out.println("in decereleration: "+Math.round(this.getCurrentSpeed()));
		this.setCurrentSpeed(this.getCurrentSpeed()-softbrakemodule);
	}
	
	
//	@ScheduledMethod(start=0,interval=1)
	public void test(){
		Context context=ContextUtils.getContext(this);
		GridPoint position=grid.getLocation(this);
		int x=position.getX();
		int y=position.getY();
//		if(this.getCurrentSpeed()<2){
//		this.accelerate();}
//		this.brake(Constants.softBrakeModule);
//		int k=(int) Math.round(this.getCurrentSpeed());
//		this.getAnticipation().updateVehicleAnticipation(this.getHeading(), x-1, y, 16);
		//grid.moveTo(this,x+1,y);
	}
	
	public DestinationCell chooseDestination(){
		return this.getDest();//TODO
	}
	
	public void project(){
		int speed=(int)this.getCurrentSpeed();
		int x=grid.getLocation(this).getX()+1;
		int y=grid.getLocation(this).getY();
		int anticipationLenght=this.calcAnticipation(speed);
		System.out.println(this.getCurrentSpeed());
		this.getAnticipation().updateVehicleAnticipation(this.getHeading(), x, y, anticipationLenght);
		this.setCurrentSpeed(this.getCurrentSpeed()+1);
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
	
	@ScheduledMethod(start=1,interval=2)
	public void movevement(){
//		int x=this.getDest().getX();
//		int y=this.getDest().getY();
//		this.move(x, y);
		this.getAnticipation().flushAnticipation();
	}
	public void move(int x, int y){
		grid.moveTo(this,x,y);
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
	

}
