/**
 * 
 */
package CrossingSiteModel;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;

/**
 * @author Lor3nz
 *
 */
public class Pedestrian extends MobileAgent {
	
	int step=0;
	int startStep=0;
	boolean firstPath=false;
	private int currentDirection;//heading
	private AnticipationCell[] anticipationArray;
	private boolean crossing=false;
	private boolean crossed=false;
	private boolean curbstop=false;
	private boolean arrived=false;
	private boolean closeVehicle;
	private boolean occupiedCell=false;
	private int currentSurface;
	private int lastSurface;
	private int roadType;
	private int crossingType;
	private int lastDirection;
	private int passedPedestrian=0;
	Parameters params=RunEnvironment.getInstance().getParameters();
	//int numberOfPedestrian=(Integer)params.getValue("nPed");
	double fieldWeight=(Double)params.getValue("fieldWeight");
	double roadW=(Double)params.getValue("roadW");
	double directionW=(Double)params.getValue("directionW");
	private boolean stoppedVehicle;
	private String filename;
	
	public Pedestrian(String ID,Destination d,String filename){
		this.setID(ID);
		this.setDestination(d);
		this.setAnticipation(4);
		this.setCurrentDirection(Values.initialX);
		anticipationArray=new AnticipationCell[this.getAnticipation()];
		this.setFilename(filename);
	}
	
	
	public void stepTest(){
		setDynamicAnticipationPathTest();
	}
	
	@ScheduledMethod(start=0,interval=Values.timeDivision*5)
	public void step(){
		
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		String dvlString=this.getDestination().getDistanceFieldName();
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer(dvlString);
		
		//Get pedestrian current position
		GridPoint currentPosition=this.getCurrentPosition(env);
		
		//Obtain distance from current position
		this.setCurrentDistance(gvl.get(currentPosition.getCoord(0),currentPosition.getCoord(1)));

		//Get destination
		GridPoint inDest=ChooseDestination();
		
		//double inDestDistance=gvl.get(inDest.getCoord(0),inDest.getCoord(1));
		//System.out.println("da:"+currentPosition.getX()+" "+currentPosition.getY()+" a "+inDest.getCoord(0)+" "+inDest.getCoord(1));
		//if(inDestDistance<this.getCurrentDistance()){
			//if not yet crossed
//			if(this.getCurrentPosition().getY()<this.getDestination().getyCor()){
//				this.setCrossing(true);
//			}else if(this.getCurrentPosition().getY()>=this.getDestination().getyCor()){
//				this.setCrossing(false);
//				this.setCrossed(true);
//			}
			setDynamicAnticipationPath();
			//checkAnticipation();
			//System.out.println("direction "+this.getCurrentDirection()+" "+this.getID());
			//printPath(inDest.getCoord(0),inDest.getCoord(1));
			if(!arrived){
				//if(step%5==0){
				if(!isOccupiedCell()){	
				//env.moveTo(this,inDest.getCoord(0),inDest.getCoord(1));
					move(inDest);
				}
				//}
			}
			step++;
			this.setLastDirection(this.getCurrentDirection());
			checkArrival();
		//}
	}//end step();
	
	//move to intermediate destination
	public void move(GridPoint inDest){
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		if(!arrived){
			setStartSurface();
			env.moveTo(this,inDest.getX(),inDest.getY());
			setSurface();
			controlSurface();
		}
		
	}
	
	public void setStartSurface(){
		this.setLastSurface(this.getCurrentSurface());
	}
	//Control if pedestrian has crossed
	public void  controlSurface(){
		
		
		if(this.getCurrentSurface()!=Values.roadway&&this.getLastSurface()==Values.roadway){
			PrintStream p=null;
			try {
				p = new PrintStream(new FileOutputStream(this.getFilename(),true));
				p.println(this.getID()+","+RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setCrossed(true);
		}
	}
	
	//check actual position
	//Control if pedestrian is on:
	// 0 walkway
	// 1 curb
	// 2 roadway
	public int checkSurface(){
		//default, on walkway
		int surface=Values.walkable;
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		//return surface;
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		for(Object ags : env.getObjectsAt(x,y)){
			if(ags instanceof StaticElement){
				if(((StaticElement) ags).getType().equals("walkable")){
					surface=Values.walkable;
				}
				if(((StaticElement) ags).getType().equals("curb")||y==4||y==15){
					surface=Values.curb;
					//||y==4||y==15
				}
				if(((StaticElement) ags).getType().equals("roadway")){
					surface=Values.roadway;
				}
			}
		}
		//System.out.println("surface="+surface);
		return surface;
	}
	public void setSurface(){
		//default, on walkway
		int surface=Values.walkable;
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		//return surface;
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		for(Object ags : env.getObjectsAt(x,y)){
			if(ags instanceof StaticElement){
				if(((StaticElement) ags).getType().equals("walkable")){
					this.setCurrentSurface(Values.walkable);
				}
				if(((StaticElement) ags).getType().equals("curb")||y==4||y==15){
					this.setCurrentSurface(Values.curb);
					//||y==4||y==15
				}
				if(((StaticElement) ags).getType().equals("roadway")){
					this.setCurrentSurface(Values.roadway);
				}
			}
		}
		//System.out.println("surface="+surface);
		//return surface;
	}
	
	//determine if approaching vehicle is fast or slow
	public void checkAnticipation(){
		//System.out.println(this.getID());
		reset();
		
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		//int vehAntIndex=10000; // vehicle's anticipation index
		for(int i=0;i<this.getAnticipationArray().length;i++){
			int xcell=this.getAnticipationArray()[i].getX();
			int ycell=this.getAnticipationArray()[i].getY();
			//System.out.println(xcell+" "+ycell);
			for(Object ac:env.getObjectsAt(xcell,ycell)){
				if(ac instanceof AnticipationCell){
					if(((AnticipationCell)ac).getOwner().equals("vehicle")){
						//setCloseVehicle(true);
						int vehicleCurrentSpeed=((AnticipationCell)ac).getVehicle().getCurrentSpeed();
						//System.out.println(vehicleCurrentSpeed);
						//System.out.println("veh cel index"+((AnticipationCell) ac).getIndex()+" "+((AnticipationCell)ac).getOwnerId());						
						if(((AnticipationCell) ac).getIndex()<15){
							if(vehicleCurrentSpeed!=0){
							this.setStoppedVehicle(false);
							//this.setCloseVehicle(true);
							}else{
								//System.out.println("Veicolo fermo"+this.getID());
								this.setStoppedVehicle(true);
								//this.setCloseVehicle(false);
								}
							this.setCloseVehicle(true);
							}
					}
				}
				if(ac instanceof VehicleShape){
					//System.out.println(((VehicleShape)ac).getOwnerId());
					setOccupiedCell(true);
					this.setCloseVehicle(true);
				}
			}
		}
	}
	
	
	public double goalCalc(int i,int j, String s, Double dirW){
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer(s);
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		double val=gvl.get(x,y)-gvl.get(i,j);
		double goal=(fieldWeight*val);//+(dirW*directionW);
		return goal;
	}
	
	public void reset(){
		this.setCloseVehicle(false);
		this.setStoppedVehicle(false);
		this.setOccupiedCell(false);
	}
	public GridPoint ChooseDestination(){
		
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		GridPoint newDest = new GridPoint(x,y);
		int surface=checkSurface();
		//System.out.println("Getcell2 in traiettoria "+isOccupiedCell()+" "+this.getID());
		if(isCloseVehicle()){
			//System.out.println("vicino"+isCloseVehicle()+" "+this.getID());
			//veicolo vicino
			if(isStoppedVehicle()){
				newDest=getCell(surface);
				
			}else{
				//System.out.println("vicino"+isCloseVehicle()+" "+this.getID());
				newDest= getCell(surface);
			}
		}else{
			newDest=getCell(surface);}
		return newDest;
	}
	
	//calculate next destination cell
	public GridPoint getCell(int surface){
		
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		String dvlString=this.getDestination().getDistanceFieldName();
		String cflString=this.getDestination().getCrossingFieldName();
		String cuflString=this.getDestination().getCurbFieldName();
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer(dvlString);
		double[] dw=new double[9];
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		GridPoint newDest = new GridPoint(x,y);

		//if(!isOccupiedCell()){
			
		if(surface!=Values.walkable){
			if(!isCloseVehicle()){
			gvl=(GridValueLayer)context.getValueLayer(cflString);
			fieldWeight=roadW;
			dw=directionWeightM(this.getDestination().getHpos());
			}
			else{
				//near vehicle
				gvl=(GridValueLayer)context.getValueLayer(cflString);
				fieldWeight=roadW;
				Random randDir=new Random();
				int randdDir=(Integer)randDir.nextInt(2);
				//System.out.println("randdDir"+randdDir+" "+this.getID());
				if(randdDir==0){
				dw=directionWeightM(Values.east);}else{dw=directionWeightM(Values.west);
//				for(int i=0;i<dw.length;i++){
//					//System.out.print(dw[i]+"|");
//				}
				
				}
			}
		}else{
			gvl=(GridValueLayer)context.getValueLayer(cuflString);
			for(int i=0;i<dw.length;i++){
				dw[i]=0.1111;
			}
		}
		//current position
		
		
		//istantiate next destination cell
		
		
		//print current field value;
//		for(int j=19;j>=0;j--){
//			for(int i=0;i<249;i++){
//				System.out.print("|"+gvl.get(i,j)+"|");
//			}
//			System.out.println("---");
//		}
		
		//Neighborhood cell matrix
		
		int index0=0; 
		NextCell[] neighborhood=new NextCell[9];
		
		for(int i=x-1;i<x+2;i++){
			for(int j=y-1;j<y+2;j++){
				if(!checkOccupation(i,j)){
				neighborhood[index0]=new NextCell(new GridPoint(i,j),0.0,index0,false);}
				else{
					neighborhood[index0]=new NextCell(new GridPoint(i,j),0.0,index0,true);
				}
				index0++;
			}
		}
		
		///LOTTERY COMPONENT
		double val=0;
		int index1=0;
		
		//goal values array
		double[] goalVal=new double[9];
		int index=0;
		double diagonalP=1;
		for(int i=x-1;i<x+2;i++){
			for(int j=y-1;j<y+2;j++){
				if(index%2==0){
					diagonalP=1.4;
				}else{diagonalP=1;}
				index++;
				val=(gvl.get(x,y)-gvl.get(i,j))/diagonalP;
				//val=(gvl.get(x,y)-gvl.get(i,j));
				//val=val/1.4241;
				goalVal[index1]=(val*fieldWeight)+(dw[index1]*directionW);
				//System.out.print("|goal cella"+index1+" = "+goalVal[index1]);
				index1++;
			}
		}
		
		
		//normalization factor
		double tot=0;
		for(int i=0;i<goalVal.length;i++){
			tot=tot+Math.exp(goalVal[i]);
		}
		double N=1/tot;
		//normalized lottery values
		double[] lotteryVal=new double [9];
		for(int i=0;i<lotteryVal.length;i++){
			double P=N*Math.exp(goalVal[i]);
			
			if(!neighborhood[i].isOccupied()){
			lotteryVal[i]=P;
			neighborhood[i].setPtransition(P);}else{
				neighborhood[i].setPtransition(0.0);
			}
			//System.out.println("Cell "+i+", P:"+lotteryVal[i]);
		}
		
		//lottery
		Random rand=new Random();
		double randd=rand.nextDouble();
		//System.out.println(randd);
		double lb=0;//lower bound
		double ub=0;//upper bound
		int CellIndex=0;
		for(int i=0;i<lotteryVal.length;i++){
			lb=ub;
			ub=ub+lotteryVal[i];
			if(randd>=lb&&randd<ub){
				//System.out.println("lb "+lb+" ub "+ub+" direction: "+i);
				newDest=neighborhood[i].getGridPoint();
				CellIndex=i;
			}
		}
		
		this.setCurrentDirection(CellIndex);
		//}else{System.out.println("is occupied cell "+isOccupiedCell()+" "+this.getID());}
		return newDest;
	}
	
	public void updateCell(int surface){
		getCell(surface);
	}
	//Control if pedestrian has reached his destination
	public void checkArrival(){
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		Destination destination=this.getDestination();
		
		GridPoint currentPosition=this.getCurrentPosition();
		if(destination.getxCor()==currentPosition.getX()&&destination.getyCor()==currentPosition.getY()){
			//addPedestrian(destination);
			removePed();
			//arrived=true;
			//System.out.println("Total step:"+step);
			
		}
	}

	
	public void removePed(){
		DataCollector.setPedCount();
		DataCollector.setCurrentPedestrianNumber(DataCollector.getCurrentPedestrianNumber()-1);
		//System.out.println("Passed pedestrian at step"+DataCollector.getGeneralTick()+" = "+DataCollector.getPedCount());
		ContextUtils.getContext(this).remove(this);
	}
	//@param Crossing direction:
	//5: north
	//3: south
	public double[] directionWeightM(int CrossingDirection){
		double[] dw=new double[9];
			for(int i=0;i<dw.length;i++){
				if(i!=CrossingDirection){
					dw[i]=0.025;
					}
					else{
						dw[i]=0.8;
						//this.setCurrentDirection(i);
					}
				}
		
		return dw;
	}
	
	
	
	public void setDynamicAnticipationPath(){
		if(step!=0){
			clearAnticipationPath();
		}
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		int cd=this.getCurrentDirection();
		//System.out.println("direzione correte:"+this.getCurrentDirection());
		//if pedestrian is on curb or roadway
		if(checkSurface()!=Values.walkable){
			this.anticipationArray=new AnticipationCell[this.getAnticipation()];
			AnticipationCell ac;
			int anticip=this.getAnticipation();
			if(cd==Values.south_west){
				int index=anticip-1;
				int j=y-anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x-anticip;i<x;i++){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j++;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					
					//ac.setAnticipationWeight();
				}
			
			}
			if(cd==Values.west){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x-anticip;i<x;i++){
					//System.out.println(i+" "+y);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(i);
					ac.setY(y);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,y);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.north_west){
				int j=y+anticip+1;
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x-anticip;i<x;i++){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j--;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.south){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=y-anticip;i<y;i++){
					//System.out.println(i+" "+y);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(x);
					ac.setY(i);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,x,i);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.initialX){
				int index=0;
				//System.out.println("fermo");
				this.anticipationArray=new AnticipationCell[9];
				for(int i=x-1;i<=x+1;i++){
					for(int j=y-1;j<=y+1;j++){
						ac=new AnticipationCell(index,"pedestrian",null,this);
						ac.setOwnerId(this.getID());
						ac.setX(i);
						ac.setY(j);
						this.anticipationArray[index]=ac;
						index++;
						context.add(ac);
						env.moveTo(ac,i,j);
					}
				}
				
//				this.anticipationArray[0]=new AnticipationCell(0,"Pedestrian");
//				context.add(this.anticipationArray[0]);
//				env.moveTo(this.anticipationArray[0], x,y);
				
			}
			if(cd==Values.north){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=y+anticip;i>y;i--){
					//System.out.println(x+" "+i);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(x);
					ac.setY(i);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,x,i);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.south_east){
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				int index=anticip-1;
				int j=y-anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x+anticip;i>x;i--){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j++;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.east){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x+anticip;i>x;i--){
					//System.out.println(i+" "+y);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(i);
					ac.setY(y);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,y);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.north_east){
				int index=anticip-1;
				int j=y+anticip+1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x+anticip;i>x;i--){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j--;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					
					//ac.setAnticipationWeight();
				}
			}
		}//end if
		else{
			//System.out.println("su marciapiede");
			int index=0;
//			int space= Math.abs(y-this.getDestination().getyCor());
//			if(space<this.getAnticipation()){
//				this.setAnticipation(space);
//			}
//			int anticip=this.getAnticipation();
			AnticipationCell ac;
//			//System.out.println("fermo");
			this.anticipationArray=new AnticipationCell[9];
			for(int i=x-1;i<=x+1;i++){
				for(int j=y-1;j<=y+1;j++){
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index++;
					context.add(ac);
					env.moveTo(ac,i,j);
				}
			}
		}
		checkAnticipation();
//		System.out.print(this.getID()+ " anticipations index:");
//		for(int i=0;i<this.getAnticipationArray().length;i++){
//			System.out.print(this.getAnticipationArray()[i].getX()+" "+this.getAnticipationArray()[i].getY()+"|");
//		}
		
		
	}
	
	public void setDynamicAnticipationPathTest(){
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		//int cd=this.getCurrentDirection();
		int cd=0;
		while(cd<9){
		//System.out.println("direzione correte:"+this.getCurrentDirection());
		//if pedestrian is on curb or roadway
			this.anticipationArray=new AnticipationCell[this.getAnticipation()];
			AnticipationCell ac;
			int anticip=this.getAnticipation();
			if(cd==Values.south_west){
				int index=anticip-1;
				int j=y-anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x-anticip;i<x;i++){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j++;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					
					//ac.setAnticipationWeight();
				}
			
			}
			if(cd==Values.west){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x-anticip;i<x;i++){
					//System.out.println(i+" "+y);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(i);
					ac.setY(y);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,y);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.north_west){
				int j=y+anticip+1;
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x-anticip;i<x;i++){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j--;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.south){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=y-anticip;i<y;i++){
					//System.out.println(i+" "+y);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(x);
					ac.setY(i);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,x,i);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.initialX){
				int index=0;
				//System.out.println("fermo");
				this.anticipationArray=new AnticipationCell[9];
				for(int i=x-1;i<=x+1;i++){
					for(int j=y-1;j<=y+1;j++){
						ac=new AnticipationCell(index,"pedestrian",null,this);
						ac.setOwnerId(this.getID());
						ac.setX(i);
						ac.setY(j);
						this.anticipationArray[index]=ac;
						index++;
						context.add(ac);
						env.moveTo(ac,i,j);
					}
				}
				
//				this.anticipationArray[0]=new AnticipationCell(0,"Pedestrian");
//				context.add(this.anticipationArray[0]);
//				env.moveTo(this.anticipationArray[0], x,y);
				
			}
			if(cd==Values.north){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=y+anticip;i>y;i--){
					//System.out.println(x+" "+i);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(x);
					ac.setY(i);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,x,i);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.south_east){
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				int index=anticip-1;
				int j=y-anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x+anticip;i>x;i--){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j++;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.east){
				int index=anticip-1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x+anticip;i>x;i--){
					//System.out.println(i+" "+y);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					ac.setX(i);
					ac.setY(y);
					//j++;
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,y);
					//ac.setAnticipationWeight();
				}
			}
			if(cd==Values.north_east){
				int index=anticip-1;
				int j=y+anticip+1;
				//System.out.println("direzione correte:"+this.getCurrentDirection());
				for(int i=x+anticip;i>x;i--){
					//System.out.println(i+" "+j);
					ac=new AnticipationCell(index,"pedestrian",null,this);
					ac.setOwnerId(this.getID());
					j--;
					ac.setX(i);
					ac.setY(j);
					this.anticipationArray[index]=ac;
					index--;
					context.add(ac);
					env.moveTo(ac,i,j);
					
					//ac.setAnticipationWeight();
				}
			}
		cd++;}
	}
	
	//clear anticipation path
	//@ScheduledMethod(start=2,interval=2)
	public void clearAnticipationPath(){
		//step++;
		//if(this.getLastDirection()!=Values.initialX)
		for(int i=0;i<this.anticipationArray.length;i++){
			ContextUtils.getContext(this).remove(this.anticipationArray[i]);
		}	
	}
	
	public void printPath(int x,int y){
		PathCell pc=new PathCell(x,y);
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		context.add(pc);
		env.moveTo(pc,x,y);	
	}
	
	public boolean checkOccupation(int x,int y){
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		boolean value=false;
		for(Object ags : env.getObjectsAt(x,y)){
			if(ags instanceof MobileAgent){
				//System.out.println("Mobile agent in"+x+" "+y);
				if(ags instanceof Pedestrian){
					if(((Pedestrian)ags).getID()!=this.getID()){
						value=true;
					}
				}
				if(ags instanceof Vehicle){
					value=true;
				}
			}
		}
		return value;
	}
	
	
	//////////////////////////Getters and Setters
	public boolean isPedestrian(){
		return true;
	}
	
	public int getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(int currentDirection) {
		/*
		 * south-west:0
		 * west:1
		 * north-west:2
		 * south:3
		 * initial(NO DIRECTION):4
		 * north:5
		 * south-east:6
		 * east:7
		 * north-east:8
		 * 
		 * */
		this.currentDirection = currentDirection;
	}

	public AnticipationCell[] getAnticipationArray() {
		return anticipationArray;
	}

	public void setAnticipationArray(AnticipationCell[] anticipationArray,AnticipationCell ac, int i) {
		anticipationArray[i]=ac;
	}

	public boolean isCrossing() {
		return crossing;
	}

	public void setCrossing(boolean crossing) {
		this.crossing = crossing;
	}

	public boolean isCrossed() {
		return false;
	}

	public void setCrossed(boolean crossed) {
		this.crossed = crossed;
	}

	public boolean isCurbstop() {
		return curbstop;
	}

	public void setCurbstop(boolean curbstop) {
		this.curbstop = curbstop;
	}


	public int getLastDirection() {
		return lastDirection;
	}


	public void setLastDirection(int lastDirection) {
		this.lastDirection = lastDirection;
	}


	public boolean isCloseVehicle() {
		return closeVehicle;
	}


	public void setCloseVehicle(boolean closeVehicle) {
		this.closeVehicle = closeVehicle;
	}


	public boolean isOccupiedCell() {
		return occupiedCell;
	}


	public void setOccupiedCell(boolean occupiedCell) {
		this.occupiedCell = occupiedCell;
	}


	public boolean isStoppedVehicle() {
		return stoppedVehicle;
	}


	public void setStoppedVehicle(boolean stoppedVehicle) {
		this.stoppedVehicle = stoppedVehicle;
	}


	public int getPassedPedestrian() {
		return passedPedestrian;
	}


	public void setPassedPedestrian(int passedPedestrian) {
		this.passedPedestrian = passedPedestrian;
	}


	public int getRoadType() {
		return roadType;
	}


	public void setRoadType(int roadType) {
		this.roadType = roadType;
	}


	public int getCrossingType() {
		return crossingType;
	}


	public void setCrossingType(int crossingType) {
		this.crossingType = crossingType;
	}


	public int getCurrentSurface() {
		return currentSurface;
	}


	public void setCurrentSurface(int currentSurface) {
		this.currentSurface = currentSurface;
	}


	public int getLastSurface() {
		return lastSurface;
	}


	public void setLastSurface(int lastSurface) {
		this.lastSurface = lastSurface;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}
}
