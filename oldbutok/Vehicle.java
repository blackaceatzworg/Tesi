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
import CrossingSiteModel.Values;

/**
 * @author Lor3nz
 *
 */
public class Vehicle extends MobileAgent {
	
	private int currentDirection;
	private int roadType;
	private AnticipationCell[][] anticipationArray;
	private boolean passed;
	public int vehicleId=1;
	//check if vehicle is braking
	private boolean braking=false;
	private VehicleShape[][] vehicleShape;
	//check side of position of pedestrian
	private boolean PedOnLeftSide=false;
	private boolean PedOnRightSide=false;
	private boolean PedOnCenter=false;
	private boolean firstPedInAnticipation=false;
	private int conflits;
	private int waitingTime;
	private String filename;
	//state last action
	private int lastAction=0;
	
	//timing variables
	private int tick=0;

	private int lastChangeTick=0;
	

	Parameters params=RunEnvironment.getInstance().getParameters();
	int numberOfVehicle=(Integer)params.getValue("maxVperLane");
	
	public Vehicle(String ID,Destination d, String filename){
		this.setID(ID);
		this.setDestination(d);
		this.setDesiredSpees(5);
		this.setCurrentSpeed(3);
		this.setAnticipation(60);
		this.setCurrentDirection(1);
		this.setLastAction(1);
		this.setWaitingTime(0);
		this.anticipationArray=new AnticipationCell[this.getAnticipation()][5];
		this.setConflits(0);
		this.setFilename(filename);
		//this.createVShape(this,5,10);
		
	}
	
	@ScheduledMethod(start=0,interval=Values.timeDivision)
	public void step(){
		//DataCollector.setGeneralTick();
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer("CarDestinationFF");
		
		GridPoint currentPosition=this.getCurrentPosition(env);
		this.setCurrentDistance(gvl.get(currentPosition.getCoord(0),currentPosition.getCoord(1)));
		setDynamicAnticipationPath();
		speedStepper();
		setDynamicVShape(5,10);
		if(checkPassed()){
			removeVehicle();
		}
		
		tick++;
		//System.out.println(tick+" ---------------------------------------------------------- conflitc"+this.getConflits());
	}
	//@ScheduledMethod(start=1,interval=timeDivision)
	public void move(int val){
		//System.out.println("muovo");
		if(val!=0){
			this.setWaitingTime(0);
		}
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		GridValueLayer gvl=(GridValueLayer)context.getValueLayer("CarDestinationFF");
		GridPoint inDest=new GridPoint(this.getCurrentPosition().getCoord(0)-val,this.getCurrentPosition().getCoord(1)); //intermediate destination
		double inDestDistance=gvl.get(inDest.getCoord(0),inDest.getCoord(1));
		if(inDestDistance<=this.getCurrentDistance()){
			if(val!=0){
			env.moveTo(this,inDest.getX(),inDest.getY());
			this.setCurrentPosition(inDest);
			//this.clearVehicleShape();
			//this.createVShape(this,5,10);
			}
		}
		//checkPassed(this.getCurrentPosition());
	}
	
	//Speed management for different timestep
	public void speedStepper(){
		int cs=this.getCurrentSpeed();
		switch(cs){
		case 5:
			//System.out.println("speed 5");
			//if(tick==lastChangeTick+1){
				this.setLastChangeTick(tick);
				//System.out.println("case5 "+lastChangeTick);
				//clearAnticipationPath();
				//clearVehicleShape();
				//setDynamicAnticipationPath();
				move(2);
			//}
			
			break;
		case 4:
			//System.out.println("speed 4");
			if(tick%2==0){
				this.setLastChangeTick(tick);
				//System.out.println("case4 muovo2");
				//clearAnticipationPath();
				//clearVehicleShape();
				//setDynamicAnticipationPath();
				move(2);
				
			}else{
				this.setLastChangeTick(tick);
				//System.out.println("case4 muovo1");
				//clearAnticipationPath();
				//clearVehicleShape();
				//setDynamicAnticipationPath();
				move(1);
			}
			
			break;
		case 3:
			//System.out.println("speed 3");
			//if(tick==lastChangeTick){
				this.setLastChangeTick(tick);
				//System.out.println("case3 "+lastChangeTick);
				//clearAnticipationPath();
				//clearVehicleShape();
				//setDynamicAnticipationPath();
				move(1);
			//}
			
			break;
		case 2:
			////System.out.println("speed 2");
			if(tick==lastChangeTick+2){
				this.setLastChangeTick(tick);
				////System.out.println("case2 "+lastChangeTick);
				//clearAnticipationPath();
				//clearVehicleShape();
				//setDynamicAnticipationPath();
				move(1);
			}
			
			break;
		case 1:
			////System.out.println("speed 1");
			if(tick==lastChangeTick+4){
				this.setLastChangeTick(tick);
				////System.out.println("case1 "+lastChangeTick);
				//clearAnticipationPath();
				//clearVehicleShape();
				//setDynamicAnticipationPath();
				move(1);
			}
			
			break;
		case 0:
			
			////System.out.println("speed 0");
			this.setLastChangeTick(tick);
			this.setWaitingTime(this.getWaitingTime()+1);
			////System.out.println("lastchange"+lastChangeTick);
			//clearAnticipationPath();
			//setDynamicAnticipationPath();
			move(0);
			break;
		}
	}
	
	//random accelation for vehicle
	public void RandomAcceleration(){
		Random rand=new Random();
		double randd=rand.nextDouble();
		////System.out.println("PrevoiusSpeed "+this.getCurrentSpeed());
		if(randd<0.1){
			if(this.getCurrentSpeed()>=2){
			this.setCurrentSpeed(this.getCurrentSpeed()-1);
			}
			}
//		else{
//			if(this.getCurrentSpeed()<5){
//				this.setCurrentSpeed(this.getCurrentSpeed()+1);
//			}
//		}
		////System.out.println("CurrentSpeed "+this.getCurrentSpeed());
	}
	
	//@ScheduledMethod(start=2,interval=timeDivision)
	public void clearAnticipationPath(){
		//step++;
		////System.out.println("cleat vehicle anticpaition");
		if(tick!=0){
		for(int i=0;i<this.getAnticipationArray().length;i++){
			for(int j=0;j<this.getAnticipationArray()[i].length;j++)
			ContextUtils.getContext(this).remove(this.anticipationArray[i][j]);
		}}	
	}
	
	public void clearVehicleShape(){
		if(tick!=0){
			for(int i=0;i<this.getVehicleShape().length;i++){
				for(int j=0;j<this.getVehicleShape()[i].length;j++)
				ContextUtils.getContext(this).remove(this.getVehicleShape()[i][j]);
			}
		}	
	}
	
	public void setDynamicVShape(int width, int lenght){
		if(tick!=0){
			this.clearVehicleShape();
		}
		Context context =ContextUtils.getContext(this);
		Grid env=(Grid)context.getProjection("grid");
		VehicleShape vs;
		VehicleShape[][] vehicleShape=new VehicleShape[lenght][width];
		int xindex=0;
		int xv=this.getCurrentPosition().getX();
		int yv=this.getCurrentPosition().getY();
		for(int i=xv;i<xv+lenght;i++){
			int yindex=0;
			for(int j=yv-2;j<=yv+2;j++){
				vs=new VehicleShape(this,i,j);
				////System.out.println(xindex+":xindex i:"+i+" j "+j+" yindex"+yindex);
				vehicleShape[xindex][yindex]=vs;
				context.add(vs);
				env.moveTo(vs,i,j);
				yindex++;
			}
			xindex++;
		}
		this.setVehicleShape(vehicleShape);
	}
	
	//anticipation, direction East-west
	public void setDynamicAnticipationPath(){
		if(tick!=0){
			clearAnticipationPath();
			//clearVehicleShape();
		}
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		int x=this.getCurrentPosition().getX();
		int y=this.getCurrentPosition().getY();
		int space= Math.abs(x-this.getDestination().getxCor());
		if(space<this.getAnticipation()){
			this.setAnticipation(space);
		}
		int anticip=this.getAnticipation();
		////System.out.println("anticipation:"+this.getAnticipation()+"x-anticip:"+(x-anticip));
		this.anticipationArray=new AnticipationCell[this.getAnticipation()][5];
		AnticipationCell ac;
		int direction=this.getCurrentDirection();
		if(direction==1){
			int xindex=this.getAnticipation()-1;
			for(int i=x-anticip;i<x;i++){
				int yindex=4;
				for(int j=y-2;j<=y+2;j++){
				ac=new AnticipationCell(xindex,"vehicle",this,null);
				ac.setOwner("vehicle");
				ac.setOwnerId(this.getID());
				ac.setX(i);
				ac.setY(j);
				this.anticipationArray[xindex][yindex]=ac;
				////System.out.println(xindex+" "+this.anticipationArray[xindex][yindex].getX()+" "+yindex+" "+this.anticipationArray[xindex][yindex].getY());
				yindex--;
				context.add(ac);
				env.moveTo(ac,i,j);
				}
				xindex--;
			}
			
		}
		
		if(!checkVehicle()){
		int[] detection=checkAnticipation();
		chooseBehavior(detection);
		}
		
	}
	
	public boolean checkVehicle(){
		boolean vehicleAhead=false;
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		for(int i=0;i<this.getAnticipationArray().length;i++){
			for(int j=0;j<this.getAnticipationArray()[i].length;j++){
				int xcell=this.getAnticipationArray()[i][j].getX();
				int ycell=this.getAnticipationArray()[i][j].getY();
				for(Object ac:env.getObjectsAt(xcell,ycell)){
					if(ac instanceof VehicleShape){
						if(i<5){ // Distanza di sicurezza 
							Vehicle v=((VehicleShape)ac).getVehicle();
							//this.setCurrentSpeed(v.getCurrentSpeed());
							while(this.getCurrentSpeed()!=0){
								brake(1);
							}
							vehicleAhead=true;
						}
						
					}
					//break;
					}
				}
		}
		return vehicleAhead;
	}
	
	public int[] checkAnticipation(){
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		int y=this.getCurrentPosition().getY();
		int pedDirection=-1;
		int firstPedx=10000;
		int firstPedy=10000;
		int firstAntx=10000;
		int firstAnty=10000;
		int firstAntIndex=-1;
		int totAnt=0;
		for(int i=0;i<this.getAnticipationArray().length;i++){
			for(int j=0;j<this.getAnticipationArray()[i].length;j++){
				int xcell=this.getAnticipationArray()[i][j].getX();
				int ycell=this.getAnticipationArray()[i][j].getY();
				for(Object ac:env.getObjectsAt(xcell,ycell)){
					if(ac instanceof AnticipationCell){
						if(((AnticipationCell) ac).getOwner().equals("pedestrian"))
						{
							totAnt++;
							if(i<firstAntx&&j<firstAnty)
							{
								firstAntx=i;
								firstAnty=j;
								firstAntIndex=((AnticipationCell)ac).getIndex();
							}
						}
					}
					if(ac instanceof Pedestrian){
						resetCheckPosition();
						if(i<firstPedx&&j<firstPedy)
						{
							firstPedx=i;
							firstPedy=j;
							this.setFirstPedInAnticipation(true);
							
						}
						
						if(ycell==y-2||ycell==y-1){
							setPedOnLeftSide(true);
						}
						else if(ycell==y+2||ycell==y+1){
							setPedOnRightSide(true);
						}
						else if(ycell==y){
							setPedOnCenter(true);
						}
						pedDirection=StateTPosition(((Pedestrian)ac).getCurrentDirection());
						
					}
				}
				
			}
		}
		if(tick!=0){
		checkConflict(this.getVehicleShape()[0].length);}
		int[] result=new int[7];
		result[0]=firstPedx;
		result[1]=firstPedy;
		result[2]=firstAntx;
		result[3]=firstAnty;
		result[4]=totAnt;
		result[5]=firstAntIndex;
		result[6]=pedDirection;
		////System.out.println("first ped in "+firstPedx+" firstAnt in "+firstAntx+" totAnt="+totAnt );
		return result;
	}
	
	public void checkConflict(int length){
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		
			int currentY=this.getCurrentPosition().getY();
			int currentX=this.getCurrentPosition().getX();
			for(int j=currentY-2;j<=currentY+2;j++){
				for(int i=0;i<length;i++){
					////System.out.println(i+" "+j);
					for(Object ac:env.getObjectsAt(currentX+i,j)){
						if(ac instanceof Pedestrian){
							//int [] conflictArray= getConflictCoord(((AnticipationCell)ac));
							this.setConflits(this.getConflits()+1);
							//System.out.println(currentX+i+" "+j);
						}
					}
				}
			}
		
	}
	
	public int[] getConflictCoord(AnticipationCell ac){
		int[] conflictArray=new int[2];
		conflictArray[0]=ac.getX();
		conflictArray[1]=ac.getY();
		return conflictArray;
	}
	public void chooseBehavior(int index[]){
		int pedDirection=index[6];
		int currentSpeed=this.getCurrentSpeed();
		int perceptionZone=getPerceptionZone(index[0], index[2]);
		if(lastAction==4&&this.getCurrentSpeed()>0){
			brake(1);
		}else{
		switch(perceptionZone){
		case 1:
			manageFirstZone(currentSpeed,pedDirection);
			break;
		case 2:
			
			manageSecondZone(currentSpeed,pedDirection);
			break;
		case 3:
			
			manageThirdZone(currentSpeed,pedDirection);
			break;
		case 0:
			if(currentSpeed<5){
				accelerate();
				RandomAcceleration();
			}
			break;
		}}
			
			
			//controllo anticipazione
		
		
	}
	
	
	//Count number of intersection beetween car and pedestrian anticipation
	
	//anticipation zone detection
	
	public int getPerceptionZone(int index1, int index2){
			//pedestrian detected in one of anticipazion zones
			int index=60;
			if(index1<index2){
			index=index1;
			}
			else{index=index2;}
			////System.out.println("perception index="+index);
			if(index>=0&&index<=19){
				return 1;
			}else if(index>=20&&index<=39){
				
				return 2;
			}else if(index>=40&&index<=59){
				
				return 3;
			}else{
				return 0;
			}
		}
	
	//Utility function for behavior
	public void accelerate(){
		this.setCurrentSpeed(this.getCurrentSpeed()+1);
		this.setLastAction(2);
		//System.out.println("T"+tick+" : Accelero,v ="+this.getCurrentSpeed());
	}
	
	//@param val
	//1 soft brake
	//2 hard brake
	public void brake(int val){
		this.setCurrentSpeed(this.getCurrentSpeed()-val);
		this.setLastAction(val+2);
		//System.out.println("T"+tick+" : freno di "+val+" v ="+this.getCurrentSpeed());
	}
	
	public void mantain(){
		this.setCurrentSpeed(this.getCurrentSpeed());
		this.setLastAction(1);
		//System.out.println("T"+tick+" : mantengo,v ="+this.getCurrentSpeed());
	}
	
	//management for uncertain choice
	public void randBehav(int choice){
		double choiceN=choice/6;
		double angryDriver=0.4;//aggressiveness parameter
		switch(choice){
		case 1:
			//1 hard brake/soft brake
			if(choiceN<=0.5-angryDriver){brake(2);}
			else{brake(1);}
			break;
		case 2:
			//2 hard brake/accelerate
			if(choiceN<=0.5-angryDriver){brake(2);}
			else{accelerate();}
			break;
		case 3:
			//3 soft brake/mantain
			if(choiceN<=0.5-angryDriver){brake(1);}
			else{mantain();}
			break;
		case 4:
			//4 mantain/accelerate
			if(choiceN<=0.5-angryDriver){mantain();}
			else{accelerate();}
			break;
		case 5:
			//5 soft brake/accelerate
			if(choiceN<=0.5-angryDriver){brake(1);}
			else{accelerate();}
			break;
		case 6:
			//6 hard brake/mantain
			if(choiceN<=0.5-angryDriver){brake(2);}
			else{mantain();}
			break;
		}
		
	}
	
	//Determine if Pedestrian is on road counting intersecanting anticipations
	public boolean isOnRoad(int index, int index4, int index5){
		if(index>=0&&index<=2){
			//System.out.println("Low conflict, "+index);
		return false;}
		else{
			//System.out.println("High conflict "+index);
			return true;}
	}
	
	//express cases based on pedestrian direction and trasversal position
	public int StateTPosition(int PedDirection){
		int value=-1;
		//System.out.println("L: "+isPedOnLeftSide()+" R: "+isPedOnRightSide());
		if(isPedOnLeftSide()){
			if(PedDirection==Values.north||PedDirection==Values.north_east||PedDirection==Values.north_west){
				//Ped on left, entering crossing site
				value=Values.Entering;
				//System.out.println("ls-Entering");
			}
			if(PedDirection==Values.south||PedDirection==Values.south_east||PedDirection==Values.south_west){
				//Ped on left, exiting crossing site
				value=Values.Exiting;
				//System.out.println("ls-Exiting");
			}
		}
		if(isPedOnRightSide()){
			if(PedDirection==Values.north||PedDirection==Values.north_east||PedDirection==Values.north_west){
				//Ped on right, entering crossing site
				value=Values.Exiting;
				//System.out.println("rs-Exiting");
			}
			if(PedDirection==Values.south||PedDirection==Values.south_east||PedDirection==Values.south_west){
				//Ped on right, exiting crossing site
				value=Values.Entering;
				//System.out.println("rs-Entering");
			}
		}
		if(isPedOnCenter()){
			value=Values.crossing;
			//System.out.println("crossing");
		}
		return value;
		//May be left.
		
	}
	
	//anticipation zone management
	public void manageFirstZone(int currentSpeed, int TPosition){
		int waitingTimeFZ=this.getWaitingTime();
		if(this.firstPedInAnticipation){
			//System.out.print("con pedone, tpos:"+TPosition+", ");
			switch(currentSpeed){
			case 0:
				//System.out.println("fascia1 v0");
				if(TPosition==Values.Entering){mantain();}
				if(TPosition==Values.Exiting){randBehav(Values.mantainAccelerate);}
				if(TPosition==Values.crossing){mantain();}
				if(waitingTimeFZ>174){
					randBehav(Values.mantainAccelerate);
				}
				break;
			case 1:
				//System.out.println("fascia1 v1");
				if(TPosition==Values.Entering){brake(1);}
				if(TPosition==Values.Exiting){brake(1);}
				if(TPosition==Values.crossing){brake(1);}
				break;
			case 2:
				//System.out.println("fascia1 v2");
				if(TPosition==Values.Entering){brake(1);}
				if(TPosition==Values.Exiting){brake(1);}
				if(TPosition==Values.crossing){brake(2);}
				break;
			case 3:
				//randBehav(2);
				//System.out.println("fascia1 v3");
				if(TPosition==Values.Entering){brake(2);}
				if(TPosition==Values.Exiting){brake(2);}
				if(TPosition==Values.crossing){brake(2);}
				break;
			case 4:
				//randBehav(2);
				//System.out.println("fascia1 v4");
				if(TPosition==Values.Entering){brake(2);}
				if(TPosition==Values.Exiting){brake(2);}
				if(TPosition==Values.crossing){brake(2);}
				break;
			case 5:
				//System.out.println("fascia1 v5");
				if(TPosition==Values.Entering){brake(2);}
				if(TPosition==Values.Exiting){brake(2);}
				if(TPosition==Values.crossing){brake(2);}
				break;
			}
		}else{
			//System.out.println("senza pedone, ");
			switch(currentSpeed){
			case 0:
				//System.out.println("fascia1 v0");
				randBehav(Values.mantainAccelerate);
				break;
			case 1:
				//System.out.println("fascia1 v1");
				randBehav(Values.sBrakeMantain);
				break;
			case 2:
				//System.out.println("fascia1 v2");
				randBehav(Values.hardsoftBrake);
				break;
			case 3:
				//System.out.println("fascia1 v3");
				randBehav(Values.hardsoftBrake);
				break;
			case 4:
				//System.out.println("fascia1 v4");
				brake(2);
				//randBehav(Values.mantainAccelerate);
				break;
			case 5:
				//System.out.println("fascia1 v5");
				brake(2);
				break;
			}
			
		}
	}
	public void manageSecondZone(int currentSpeed, int TPosition){
		if(this.firstPedInAnticipation){
			//System.out.print("con pedone, "+TPosition);
			switch(currentSpeed){
			case 0:
				//System.out.println("fascia2 v0");
				if(TPosition==Values.Entering){accelerate();}
				if(TPosition==Values.Exiting){accelerate();}
				if(TPosition==Values.crossing){accelerate();}
				break;
			case 1:
				//System.out.println("fascia2 v1");
				if(TPosition==Values.Entering){randBehav(Values.mantainAccelerate);}
				if(TPosition==Values.Exiting){accelerate();}
				if(TPosition==Values.crossing){randBehav(Values.mantainAccelerate);}
				break;
			case 2:
				//System.out.println("fascia2 v2");
				if(TPosition==Values.Entering){brake(1);}
				if(TPosition==Values.Exiting){randBehav(Values.sBrakeMantain);}
				if(TPosition==Values.crossing){randBehav(Values.hardsoftBrake);}
				break;
			case 3:
				//System.out.println("fascia2 v3");
				if(TPosition==Values.Entering){randBehav(Values.hardsoftBrake);}
				if(TPosition==Values.Exiting){randBehav(Values.hardsoftBrake);}
				if(TPosition==Values.crossing){randBehav(Values.hardsoftBrake);}
				break;
			case 4:
				//System.out.println("fascia2 v4");
				if(TPosition==Values.Entering){brake(1);}
				if(TPosition==Values.Exiting){randBehav(Values.mantainAccelerate);}
				if(TPosition==Values.crossing){randBehav(Values.hardsoftBrake);}
				break;
			case 5:
				//System.out.println("fascia2 v5");
				if(TPosition==Values.Entering){brake(1);}
				if(TPosition==Values.Exiting){mantain();}
				if(TPosition==Values.crossing){brake(2);}
				break;
			}
		}else{
			//System.out.println("senza pedone, ");
			switch(currentSpeed){
			case 0:
				//System.out.println("fascia2 v0");
				accelerate();
				break;
			case 1:
				//System.out.println("fascia2 v1");
				randBehav(Values.mantainAccelerate);
				break;
			case 2:
				//System.out.println("fascia2 v2");
				mantain();
				break;
			case 3:
				//System.out.println("fascia2 v3");
				randBehav(Values.sBrakeAccelerate);
				break;
			case 4:
				//System.out.println("fascia2 v4");
				randBehav(Values.sBrakeAccelerate);
				break;
			case 5:
				//System.out.println("fascia2 v5");
				mantain();
				break;
			}
			
		}
	}
	public void manageThirdZone(int currentSpeed, int TPosition){
		if(this.firstPedInAnticipation){
			//System.out.println("con pedone, "+TPosition);
			switch(currentSpeed){
			case 0:
				//System.out.println("fascia3 v0");
				if(TPosition==Values.Entering){accelerate();}
				if(TPosition==Values.Exiting){accelerate();}
				if(TPosition==Values.crossing){accelerate();}
				break;
			case 1:
				//System.out.println("fascia3 v1");
				if(TPosition==Values.Entering){accelerate();}
				if(TPosition==Values.Exiting){accelerate();}
				if(TPosition==Values.crossing){accelerate();}
				break;
			case 2:
				//System.out.println("fascia3 v2");
				if(TPosition==Values.Entering){accelerate();}
				if(TPosition==Values.Exiting){accelerate();}
				if(TPosition==Values.crossing){accelerate();}
				break;
			case 3:
				//System.out.println("fascia3 v3");
				if(TPosition==Values.Entering){mantain();}
				if(TPosition==Values.Exiting){randBehav(Values.mantainAccelerate);}
				if(TPosition==Values.crossing){randBehav(Values.sBrakeMantain);}
				break;
			case 4:
				//System.out.println("fascia3 v4");
				if(TPosition==Values.Entering){randBehav(Values.sBrakeMantain);}
				if(TPosition==Values.Exiting){mantain();}
				if(TPosition==Values.crossing){brake(1);}
				break;
			case 5:
				//System.out.println("fascia3 v5");
				if(TPosition==Values.Entering){randBehav(Values.hardsoftBrake);}
				if(TPosition==Values.Exiting){randBehav(Values.sBrakeMantain);}
				if(TPosition==Values.crossing){randBehav(Values.hardsoftBrake);}
				break;
			}
		}else{
			//System.out.println("senza pedone, ");
			switch(currentSpeed){
			case 0:accelerate();
			//System.out.println("fascia3 v0");
				break;
			case 1:accelerate();
			//System.out.println("fascia3 v1");
				break;
			case 2:accelerate();
			//System.out.println("fascia3 v2");
				break;
			case 3://System.out.println("fascia3 v3");
				randBehav(Values.mantainAccelerate);
				break;
			case 4://System.out.println("fascia3 v4");
				randBehav(Values.sBrakeMantain);
				break;
			case 5://System.out.println("fascia3 v5");
				randBehav(Values.sBrakeMantain);
				break;
			}
			
		}
	}
	
	
	//vehicle can understand if it has reached his destination.
	public boolean checkPassed(){
		int x=this.getCurrentPosition().getX();
		boolean ret=false;
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		
		if(x<=1){
			try {
				PrintStream p=new PrintStream(new FileOutputStream(this.getFilename(),true));
				p.println(this.getID()+","+RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setPassed(true);
			ret=true;
		}
		return ret;
	}
	
	public boolean getPassed(){
		return checkPassed();
	}
	public void removeVehicle(){
		//DataCollector.setVehCount();
		this.clearVehicleShape();
		this.clearAnticipationPath();
		DataCollector.setCurrentVehicleNumber(DataCollector.getCurrentVehicleNumber()-1);
//		for(int i=0;i<this.getVehicleShape().length;i++){
//			for(int j=0;j<this.getVehicleShape()[i].length;j++){
//				ContextUtils.getContext(this).remove(this.getVehicleShape()[i][j]);
//			}
//		}
		ContextUtils.getContext(this).remove(this);
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Getters and setters///////////////////////////////////////////////////////
	public void getPedestrianChecks(){
		////System.out.println("OnLeftSide:"+this.isPedOnLeftSide()+" OnRightSide:"+this.isPedOnRightSide()+" OnCenter:"+this.isPedOnCenter());
	}
	public boolean isVehicle(){
		return true;
	}
	public AnticipationCell[][] getAnticipationArray() {
		return anticipationArray;
	}


	public void setAnticipationArray(AnticipationCell[] anticipationArray) {
		
	}
	
	public void getX(){
		this.getCurrentPosition().getX();
	}

	public int isPassed() {
		return 1;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public int getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(int currentDirection) {
		this.currentDirection = currentDirection;
	}

	public boolean isBraking() {
		return braking;
	}

	public void setBraking(boolean braking) {
		this.braking = braking;
	}

	public int getLastAction() {
		return lastAction;
	}

	//@param
	//1 mantain
	//2 accelerate
	//3 soft brake
	//4 hard brake
	public void setLastAction(int lastAction) {
		this.lastAction = lastAction;
	}
	public boolean isPedOnLeftSide() {
		return PedOnLeftSide;
	}

	public void setPedOnLeftSide(boolean pedOnLeftSide) {
		PedOnLeftSide = pedOnLeftSide;
	}

	public boolean isPedOnRightSide() {
		return PedOnRightSide;
	}

	public void setPedOnRightSide(boolean pedOnRightSide) {
		PedOnRightSide = pedOnRightSide;
	}

	public boolean isPedOnCenter() {
		return PedOnCenter;
	}

	public void setPedOnCenter(boolean pedOnCenter) {
		PedOnCenter = pedOnCenter;
	}
	
	public void resetCheckPosition(){
		setPedOnRightSide(false);
		setPedOnLeftSide(false);
		setPedOnCenter(false);
	}
	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}
	public int getLastChangeTick() {
		return lastChangeTick;
	}

	public void setLastChangeTick(int lastChangeTick) {
		this.lastChangeTick = lastChangeTick;
	}

	public boolean isFirstPedInAnticipation() {
		return firstPedInAnticipation;
	}

	public void setFirstPedInAnticipation(boolean firstPedInAnticipation) {
		this.firstPedInAnticipation = firstPedInAnticipation;
	}

	public int getConflits() {
		return conflits;
	}

	public void setConflits(int conflits) {
		this.conflits = conflits;
	}

	public VehicleShape[][] getVehicleShape() {
		return vehicleShape;
	}

	public void setVehicleShape(VehicleShape vehicleShape[][]) {
		this.vehicleShape = vehicleShape;
	}
	public void createVShape(Vehicle v, int width, int lenght){
		Context context =ContextUtils.getContext(this);
		Grid env=(Grid)context.getProjection("grid");
		VehicleShape vs;
		VehicleShape[][] vehicleShape=new VehicleShape[lenght][width];
		int xindex=0;
		int xv=v.getCurrentPosition().getX();
		int yv=v.getCurrentPosition().getY();
		for(int i=xv;i<xv+lenght;i++){
			int yindex=0;
			for(int j=yv-2;j<=yv+2;j++){
				vs=new VehicleShape(v,i,j);
				////System.out.println(xindex+":xindex i:"+i+" j "+j+" yindex"+yindex);
				vehicleShape[xindex][yindex]=vs;
				context.add(vs);
				env.moveTo(vs,i,j);
				yindex++;
			}
			xindex++;
		}
		this.setVehicleShape(vehicleShape);
		//return vehicleShape;
	}

	public int getRoadType() {
		return roadType;
	}

	public void setRoadType(int roadType) {
		this.roadType = roadType;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	

}
