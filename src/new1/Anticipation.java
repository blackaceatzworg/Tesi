package new1;



import java.util.ArrayList;

import bsh.This;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Anticipation {
	Parameters params=RunEnvironment.getInstance().getParameters();
	int VehicleanticipationModule=(Integer)params.getValue("anticipationModule");
//	int PedanticipationModule=(Integer)params.getValue("PedanticipationModule");
	
	private String owner;//occhio, è getID!
	Grid<Object> grid;
	Context context;
	private ArrayList<AnticipationCell> anticipationCells;
	private String ownerType;
	
	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Anticipation(String ownerType){
		this.setOwnerType(ownerType);
	}
	
	public void initAnticipation(String id, Grid<Object> agentGrid, Context context){
		this.setId(id);
		this.grid=agentGrid;
		this.setAnticipationCells(new ArrayList<AnticipationCell>());
		this.context=context;
	}
	
	public void initVehicleAnticipation(String id,Grid<Object> agentGrid,Context context){
		this.setId(id);
		this.grid=agentGrid;
		this.setAnticipationCells(new ArrayList<AnticipationCell>());
		this.context=context;
	}
	
	public void setVehicleAnticipation(int direction, int x, int y, int anticipationLenght, int speed){
		AnticipationCell ac;
		int antIndex=VehicleanticipationModule;
		System.out.println(anticipationLenght);
		switch(direction){
		case Constants.E:
				for(int i=x;i<x+anticipationLenght;i++){
					for(int j=y;j<y+5;j++){
						try{
						ac= new AnticipationCell(i%Constants.GRID_LENGHT,j,this.getId(),ownerType, speed);
						ac.setIndex(this.calcAntIndex(antIndex));
						ac.setX(i%Constants.GRID_LENGHT);
						ac.setY(j);
						System.out.println(ac.getX()+" "+ac.getY());
						context.add(ac);
						grid.moveTo(ac,ac.getX(),ac.getY());
						this.getAnticipationCells().add(ac);
						}catch(Exception e){
							e.printStackTrace();
							}
					}System.out.println("");
					antIndex++;
				}
			break;
		case Constants.O:
				for(int i=x;i>x-anticipationLenght;i--){
					for(int j=y;j<y+5;j++){
						try{
						ac= new AnticipationCell((i+Constants.GRID_LENGHT)%Constants.GRID_LENGHT,j,this.getId(),ownerType, speed);
						ac.setIndex(this.calcAntIndex(antIndex));
						ac.setX((i+Constants.GRID_LENGHT)%Constants.GRID_LENGHT);
						ac.setY(j);
						System.out.print(ac.getX()+" "+ac.getY());
						context.add(ac);
						grid.moveTo(ac,ac.getX(),ac.getY());
						this.getAnticipationCells().add(ac);
						}catch(Exception e){
							e.printStackTrace();
							}
					}System.out.println("");
					antIndex++;
				}
			break;
		}
	}
		public int calcAntIndex(int k){
			int ant=VehicleanticipationModule;
			int antIndex=0;
			if(k>=ant&&k<ant*2){
				antIndex=1;
			}
			if(k>=ant*2&&k<ant*3){
				antIndex=2;
			}
			if(k>=ant*3&&k<ant*4){
				antIndex=3;
			}
			if(k>=ant*4&&k<ant*5){
				antIndex=4;
			}
			if(k>=ant*5&&k<ant*6){
				antIndex=5;
			}
			return antIndex;
		}
	
	//TODO
	public void updateVehicleAnticipation(int displacement, int heading, int speedZone){
		for(AnticipationCell ac:this.getAnticipationCells()){
			if(heading==Constants.E){
//				System.out.println(ac.getX()+" "+mod);
				ac.setX((ac.getX()+displacement)%Constants.GRID_LENGHT);
				ac.setSpeed(speedZone);
				context.add(ac);
				grid.moveTo(ac,ac.getX(),ac.getY());
			}else{
				ac.setX((ac.getX()-displacement+Constants.GRID_LENGHT)%Constants.GRID_LENGHT);
				ac.setSpeed(speedZone);
				context.add(ac);
				grid.moveTo(ac,ac.getX(),ac.getY());
			}
		}
	}
	
	public void updateDynamicVehicleAnticipation(int displacement, int heading, int anticipationLenght){
		System.out.println("anticipationLenght"+anticipationLenght);
		int antIndex=0;
		for(AnticipationCell ac:this.getAnticipationCells()){
			if(antIndex<=anticipationLenght){
				if(heading==Constants.E){
					ac.setX((ac.getX()+displacement)%Constants.GRID_LENGHT);
	//				System.out.println(ac.getX());
					context.add(ac);
					grid.moveTo(ac,ac.getX(),ac.getY());
				}else{
					System.out.println(ac.getX()+" "+antIndex);
					ac.setX((ac.getX()-displacement+Constants.GRID_LENGHT)%Constants.GRID_LENGHT);
					context.add(ac);
					grid.moveTo(ac,ac.getX(),ac.getY());
				}
				}
			}
	}
	
	public void updatePedestrianAnticipation(int direction, int x, int y){
		AnticipationCell ac;
		int k=5;
		switch(direction){
		case Constants.O:
			this.setOAnticip(x, y, k, ownerType);
			break;
		case Constants.S:
			this.setSAnticip(x, y, k, ownerType);
			break;
		case Constants.N:
			this.setNAnticip(x, y, k, ownerType);
			break;
		case Constants.E:
			this.setEAnticip(x, y, k, ownerType);
			break;
		case Constants.NO:
//			System.out.println(Constants.NO+" "+direction);
			this.setNOAnticip(x, y, k, ownerType);
			break;
		case Constants.SO:
//			System.out.println(Constants.NO+" "+direction);
			this.setSOAnticip(x, y, k, ownerType);
			break;
		case Constants.NE:
//			System.out.println(Constants.NO+" "+direction);
			this.setNEAnticip(x, y, k, ownerType);
			break;
		case Constants.SE:
//			System.out.println(Constants.NO+" "+direction);
			this.setSEAnticip(x, y, k, ownerType);
			break;
		}
	}
	public void flushAnticipation(){
		for(AnticipationCell ac:this.getAnticipationCells()){
			try{
				context.remove(ac);
			}catch(Exception e){
				System.out.println("array celle di anticipazione vuoto");
			}
		}
//		this.getAnticipationCells().removeAll(anticipationCells);
	}
	public void debug_checkAnticipation(ArrayList<AnticipationCell> aclist){
		int pedAnticipationCount=0;
		int vehAnticipationCount=0;
		int pedCount=0;
		int vehCount=0;
		//TODO try catch
		try{
		for(AnticipationCell ac:aclist){
			for(Object obj:grid.getObjectsAt(ac.getGp().getX(),ac.getGp().getY())){
				//System.out.println(((AnticipationCell) obj).getOwner());
				if(obj instanceof Pedestrian){
					pedCount++;
				}
				if(obj instanceof AnticipationCell){
					if(!((AnticipationCell) obj).getOwner().equals(this.getId())){
						//System.out.println("Conflitc in"+((AnticipationCell) obj).getGp().getX()+" "+
					//((AnticipationCell) obj).getGp().getY()+" for "+((AnticipationCell)obj).getOwner());
					}
				}
				if(obj instanceof Vehicle){
					vehCount++;
				}
			}
		}
		}catch(Exception e){
			
//				e.printStackTrace();	
		}
		//System.out.println(pedAnticipationCount+" "+vehAnticipationCount+" "+pedCount+" "+vehCount);
	}
	
	public void setNAnticip(int x, int y, int k, String ownerType){
		//System.out.println("dir:n");
		AnticipationCell ac;
			for(int i=y;i<y+k;i++){
				try{
//					System.out.println("dir:n");
				ac= new AnticipationCell(new GridPoint(x,i),this.getId(),ownerType);
				this.getAnticipationCells().add(ac);
				context.add(ac);
				grid.moveTo(ac,x,i);
				}catch(Exception e){
//					e.printStackTrace();
					}
			}
	}
	public void setSAnticip(int x, int y, int k, String ownerType){
//		System.out.println("dir:s");
		AnticipationCell ac;
			for(int i=y;i>y-k;i--){
				try{
//					System.out.println("dir:s");
				ac= new AnticipationCell(new GridPoint(x,i),this.getId(),ownerType);
				this.getAnticipationCells().add(ac);
				context.add(ac);
				grid.moveTo(ac,x,i);}catch(Exception e){
//					e.printStackTrace();
					}
			}
	}
	public void setEAnticip(int x, int y, int k, String ownerType){
		
		AnticipationCell ac;
			for(int i=x;i<x+k;i++){
				try{
//					System.out.println("dir:e");
				ac= new AnticipationCell(new GridPoint(i,y),this.getId(),ownerType);
				this.getAnticipationCells().add(ac);
				context.add(ac);
				grid.moveTo(ac,i,y);}catch(Exception e){
//					e.printStackTrace();
					}
			}
	}
	public void setOAnticip(int x, int y, int k, String ownerType){
		AnticipationCell ac;
			for(int i=x;i>x-k;i--){
				try{
//					System.out.println("dir:o");
				ac= new AnticipationCell(new GridPoint(i,y),this.getId(),ownerType);
				this.getAnticipationCells().add(ac);
				context.add(ac);
				grid.moveTo(ac,i,y);}catch(Exception e){
//					e.printStackTrace();
					}
			}
	}
	
	public void setNOAnticip(int x, int y, int k, String ownerType){
		AnticipationCell ac;
		int j=y;
			for(int i=x;i>x-k;i--){
				try{
//					System.out.println("dir:No");
					ac= new AnticipationCell(new GridPoint(i,j),this.getId(),ownerType);
					this.getAnticipationCells().add(ac);
					context.add(ac);
					grid.moveTo(ac,i,j);
					j++;
//					System.out.println(i+" "+j);
				}catch(Exception e){
//					e.printStackTrace();
					}	
			}
	}
	public void setSOAnticip(int x, int y, int k, String ownerType){
		AnticipationCell ac;
		int j=y;
			for(int i=x;i>x-k+1;i--){
				try{
//					System.out.println("dir:No");
					ac= new AnticipationCell(new GridPoint(i,j),this.getId(),ownerType);
					this.getAnticipationCells().add(ac);
					context.add(ac);
					grid.moveTo(ac,i,j);
					j--;
//					System.out.println(i+" "+j);
				}catch(Exception e){
//					e.printStackTrace();
					}	
			}
	}
	public void setNEAnticip(int x, int y, int k, String ownerType){
		int j=y;
		AnticipationCell ac;
			for(int i=x;i<x+k;i++){
				try{
//					System.out.println("dir:No");
					ac= new AnticipationCell(new GridPoint(i,j),this.getId(),ownerType);
					this.getAnticipationCells().add(ac);
					context.add(ac);
					grid.moveTo(ac,i,j);
					j++;
//					System.out.println(i+" "+j);
				}catch(Exception e){
//					e.printStackTrace();
					}	
			}
	}
	public void setSEAnticip(int x, int y, int k, String ownerType){
		int j=y;
		AnticipationCell ac;
			for(int i=x;i<x+k;i++){
				try{
//					System.out.println("dir:No");
					ac= new AnticipationCell(new GridPoint(i,j),this.getId(),ownerType);
					this.getAnticipationCells().add(ac);
					context.add(ac);
					grid.moveTo(ac,i,j);
					j--;
//					System.out.println(i+" "+j);
				}catch(Exception e){
//					e.printStackTrace();
					}	
			}
	}
	
/////////////////////////////////////////////////////////////////////////
	
	
/////////////////////////////////////////////////////////////////////////
	public ArrayList<AnticipationCell> getAnticipationCells() {
		return anticipationCells;
	}

	public void setAnticipationCells(ArrayList<AnticipationCell> anticipationCells) {
		this.anticipationCells = anticipationCells;
	}
//	public double getHazardIndex() {
//		return hazardIndex;
//	}
//
//	public void setHazardIndex(double hazardIndex) {
//		this.hazardIndex = hazardIndex;
//	}

	public String getId() {
		return owner;
	}

	public void setId(String id) {
		this.owner = id;
	}

}
