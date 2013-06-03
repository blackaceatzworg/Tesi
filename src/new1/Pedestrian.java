package new1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;
import new1.AgentUtils;

public class Pedestrian extends Agent {
	private Grid<Object> grid;
	//private int desiderdSpeed;
	private String id;
	private int heading;
	private int previourHeading;
	private boolean motionstate;
	private String logDirectionFileName;
	private ArrayList<String> route;
	private int routeIndex;
	private boolean arrived;
	private boolean crossed;
	

	private Anticipation anticipation;
	private DestinationCell dest;
	private String currentField;
	
	//weights
	Parameters params=RunEnvironment.getInstance().getParameters();
	double fieldWeight=(Double)params.getValue("fieldWeight");
	double directionWeight=(Double)params.getValue("directionWeight");
	
	//CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////
	public Pedestrian(String id,Grid<Object> grid,String logDirectionFileName){
		this.setId(id);
		//this.setDesiderdSpeed(desiredSpeed);
		this.grid=grid;
		this.setHeading(Constants.E);//TODO orientamento casuale
		this.setPreviourHeading(this.getHeading());
		this.setLogDirectionFileName(logDirectionFileName);
		this.routeIndex=0;
		this.setArrived(false);
		this.setAnticipation(new Anticipation(Constants.ownerTypePed));
	}
	
	public Pedestrian(String id,Grid<Object> grid){
		this.setId(id);
//		this.setDesiderdSpeed(desiredSpeed);
		this.grid=grid;
		this.setHeading(Constants.E);//TODO orientamento casuale
		this.setPreviourHeading(this.getHeading());
		this.setAnticipation(new Anticipation(Constants.ownerTypePed));
		this.routeIndex=0;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Check
			
			public void checkAndSwitchRouteNode(GridPoint position, GridValueLayer gvl){
				double val=gvl.get(position.getX(),position.getY());
				if(val==0){
					if(this.getRouteIndex()==this.getRoute().size()-1){
						System.out.println("stop");
						this.setArrived(true);
					}else{
						this.setRouteIndex(this.getRouteIndex()+1);
						this.setCurrentField(this.getRoute().get(this.getRouteIndex()));//necessario per schedule esterno
						System.out.println("walk on "+this.getRoute().get(this.getRouteIndex()));
					}
				}
			}
			
			public void checkArrival(){
				if(this.isArrived()){
					this.getAnticipation().flushAnticipation();
					ContextUtils.getContext(this).remove(this);
				}
			}
			
			
			/**
			 * Control when one pedestrian cross the road successfully
			 * 
			 * */
			public void checkSurface(){
				if(!this.isCrossed()){
					int x=grid.getLocation(this).getX();
					int y=grid.getLocation(this).getY();
					for(Object ags : grid.getObjectsAt(x,y)){
						if(ags instanceof SurfaceCell){
							if(((SurfaceCell) ags).getSurfaceType()==Constants.Curb&&this.getRouteIndex()==2){
								this.setCrossed(true);
								Constants.crossedPedCounter++;
								System.out.println("ha attraversato "+ this.getId());
							}
						}
//						else{
//							//System.out.println("Cella libera");
//						}
					}
				}
			}
			
			public boolean checkOccupation(DestinationCell dc){
				boolean occupied=false;
				int x=dc.getX();
				int y=dc.getY();
				Context context=ContextUtils.getContext(this);
				Grid<Object> env=(Grid<Object>)context.getProjection(Constants.GridID);
				for(Object ags : env.getObjectsAt(x,y)){
					if(ags instanceof Pedestrian){
							occupied=true;
//							System.out.println("Cella occupata"+dc.getX()+" "+dc.getY());
					}
//					else{
//						//System.out.println("Cella libera");
//					}
				}
				return occupied;
			}
			
			/**translate pedestrian at the given gridpoint.
			 * */
			public void move(GridPoint gp){
				grid.moveTo(this,gp.getX(),gp.getY());
				this.checkSurface();
			}
			public void move(int x, int y){
				grid.moveTo(this,x,y);
				this.checkSurface();
			}
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	//STEP ////////////////////////////////////////////////////////////////////////////////////////////////////////////////debug
	public DestinationCell chooseDestination(){
		String fieldName=this.getCurrentField();
		GridPoint position=grid.getLocation(this);
		Context context=ContextUtils.getContext(this);
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer(fieldName);
		/////////////////////////
		this.checkAndSwitchRouteNode(position, gvl);
		
		ArrayList<DestinationCell> nlist=this.getNeigh(position,fieldName);
		
		this.calcP(nlist,fieldName);
		
		this.setDest(this.lottery(nlist));
		
		this.setHeading(dest.getRelativeDirection());

		return dest;
	}
	public void project(){
//		Context context=ContextUtils.getContext(this);
		GridPoint position=grid.getLocation(this);
		this.getAnticipation().updatePedAnticipation(position.getX(), position.getY(), this.getHeading(), this.getAnticipation().getAnticipationCells());
	}
	
	int testIndex=0;
	public void testProjection(){
		testIndex++;
		if(testIndex>8){
			testIndex=0;
		}
		System.out.println("."+testIndex);
		GridPoint position=grid.getLocation(this);
		this.getAnticipation().updatePedAnticipation(position.getX(), position.getY(), testIndex, this.getAnticipation().getAnticipationCells());
	}
	
	private boolean frontVehicle;
	private boolean frontVehicleAnt;
	public void evaluate(){
		frontVehicle=false;
		frontVehicleAnt=true;
		boolean stop=false;
		for(AnticipationCell ac:this.getAnticipation().getAnticipationCells()){
			if (stop){
				break;}
			int x=ac.getX();
			int y=ac.getY();
			try{
			for(Object ags : grid.getObjectsAt(x,y)){
				
				if(ags instanceof VehicleShapeCell){
					frontVehicle=true;
					stop=true;
					break;
				}
				if(ags instanceof AnticipationCell){
					if(((AnticipationCell) ags).getOwnerType().equals("Vehicle")){
						if(((AnticipationCell) ags).getSpeed()>=3){
							frontVehicle=true;
							System.out.println("v veicolo rilvato:"+((AnticipationCell) ags).getSpeed()+" "+frontVehicleAnt);
						}
						stop=true;
						break;
					}	
				}

			}}catch(Exception e){
				System.out.println("ant corta-ped");
			}
		}
	}
	public void movement(){
		System.out.println(frontVehicle);
		if(!frontVehicle){
			this.move(dest.getX(),dest.getY());
		}
		this.checkArrival();
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////debug
	
	
	/**Log the direction of pedestrian
	 * @param direction current direction of pedestrian
	 * 
	 * 
	 * */
	public void logDirection(int direction){
		PrintStream p=null;
		String moveDirection=AgentUtils.translateHeading(direction);
		try {
			p = new PrintStream(new FileOutputStream(this.getLogDirectionFileName(),true));
			p.println(this.getId()+","+moveDirection);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//LOTTERY
	/**Procedure to determine next cell.
	 * */
	public DestinationCell chooseDestination(GridPoint position, String fieldName){
		ArrayList<DestinationCell> nlist=this.getNeigh(position,fieldName);
		this.calcP(nlist,fieldName);
		DestinationCell ret=this.lottery(nlist);
		return ret;
		}
	
	
	/**
	 * @param position the current position of agent. (GridPoint)
	 * @param fieldName the name of current path field followed
	 * @return nList the list of near DestinationCells
	 * 
	 * Each DestinationCell, if available or freely walkable, is initialized with the current value of current path field
	 * 
	 * */
	public ArrayList<DestinationCell> getNeigh(GridPoint position, String fieldname){
		Context context=ContextUtils.getContext(this);
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer(fieldname);
		ArrayList<DestinationCell> nList=new ArrayList<DestinationCell>();
		int x=position.getX();
		int y=position.getY();
		int headIndex=0;
		for(int ix=x-1;ix<x+2;ix++){
			for(int jy=y-1;jy<y+2;jy++){
				try{
					double cellValue=gvl.get(ix,jy);
					DestinationCell dc=new DestinationCell(ix,jy,cellValue,headIndex,this.getId());
					//occupation control____________________________________________________________________
					if(!this.checkOccupation(dc)){
						nList.add(dc);
					}
					headIndex++;
				}catch(Exception e){
					//e.printStackTrace();
					headIndex++;
				}
			}
		}
		return nList;
	}
	
	/**
	 * @param nlist list of near cells
	 * @param gvlName name of current path field
	 * 
	 * Calculate the transition probabilities. It considers parametric weights
	 * */
	public void calcP(ArrayList<DestinationCell> nlist, String gvlName){
		Context context=ContextUtils.getContext(this);
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer(gvlName);
		double sum=0;
		//double dirW=1;
		double currentVal=gvl.get(grid.getLocation(this).getX(),grid.getLocation(this).getY());
		ArrayList<Double> gpvalArray=new ArrayList<Double>();
		for(DestinationCell dc:nlist){
			double dirW=1;
			if(dc.getRelativeDirection()%2==0&&dc.getRelativeDirection()!=4){
				dirW=directionWeight;
				//System.out.println("dir"+dc.getRelativeDirection());
			}
			double gpval=Math.exp((currentVal-dc.getP())*fieldWeight*(1/dirW));
			gpvalArray.add(gpval);
//			System.out.println(gpval);
//			gpval*=(1/dirW);
//			System.out.println(gpval+"*");
			sum+=gpval;
		}
		double N=1/sum;
		int index=0;
		for(DestinationCell dc:nlist){
			//weight direction. Linear motion preferred
//			dirW=1;
//			if(dc.getRelativeDirection()%2==0&&dc.getRelativeDirection()!=4){
//				dirW=directionWeight;
//			}
//			double goal=N*Math.exp((currentVal-dc.getP())*fieldWeight*(1/dirW));
			dc.setP(N*gpvalArray.get(index));
			index++;
		}
//		for(DestinationCell dc:nlist){
//			System.out.println(dc.getX()+" "+dc.getY()+" p: "+dc.getP()+", heading: "+AgentUtils.translateHeading(dc.getRelativeDirection()));
//			
//		}
	}
	
	/**
	 * @param dclist
	 * @return ret The final destination cell where the pedestrian is going to move.
	 * Randomly choose the next destination cell, following the transition probabilities
	 * */
	public DestinationCell lottery(ArrayList<DestinationCell> dclist){
		Random r=new Random();
		double rand=r.nextDouble();
		double lb=0;
		double ub=0;
		DestinationCell ret=new DestinationCell(0,0,0,0,this.getId());
		for(DestinationCell dc:dclist){
			lb=ub;
			ub=ub+dc.getP();
			if(rand>=lb&&rand<ub){
				ret=dc;
			}
		}
		//heading setting________________________________________________________________________
		//this.setHeading(ret.getRelativeDirection());
		return ret;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//GETTERS AND SETTERS
	/**
	 * @return desiredSpeed 
	 * */
//	public int getDesiderdSpeed() {
//		return desiderdSpeed;
//	}
//	public void setDesiderdSpeed(int desiderdSpeed) {
//		this.desiderdSpeed = desiderdSpeed;
//	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getHeading() {
		return heading;
	}
	public void setHeading(int heading) {
		this.heading = heading;
	}
	public boolean isMotionstate() {
		return motionstate;
	}
	public void setMotionstate(boolean motionstate) {
		this.motionstate = motionstate;
	}
	public int getPreviourHeading() {
		return previourHeading;
	}
	public void setPreviourHeading(int previourHeading) {
		this.previourHeading = previourHeading;
	}

	public String getLogDirectionFileName() {
		return logDirectionFileName;
	}

	public void setLogDirectionFileName(String logDirectionFileName) {
		this.logDirectionFileName = logDirectionFileName;
	}
	public ArrayList<String> getRoute() {
		return route;
	}

	public void setRoute(ArrayList<String> route) {
		this.route = route;
	}

	public int getRouteIndex() {
		return routeIndex;
	}

	public void setRouteIndex(int routeIndex) {
		this.routeIndex = routeIndex;
	}

	public boolean isArrived() {
		return arrived;
	}

	public void setArrived(boolean arrived) {
		this.arrived = arrived;
	}

	public Anticipation getAnticipation() {
		return anticipation;
	}

	public void setAnticipation(Anticipation anticipation) {
		this.anticipation = anticipation;
	}

	public String getCurrentField() {
		return currentField;
	}

	public void setCurrentField(String currentField) {
		this.currentField = currentField;
	}
	public Grid<Object> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}

	public DestinationCell getDest() {
		return dest;
	}

	public void setDest(DestinationCell dest) {
		this.dest = dest;
	}
	public boolean isCrossed() {
		return crossed;
	}

	public void setCrossed(boolean crossed) {
		this.crossed = crossed;
	}
}
