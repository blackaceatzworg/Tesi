package new1;



import java.util.ArrayList;

import bsh.This;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Anticipation {
	
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
	
	public void updateVehicleAnticipation(int direction, int x, int y, int anticipationLenght, int speed){
		switch(direction){
		case Constants.O:
			this.setVehOAnticip(x,y,anticipationLenght, this.getOwnerType(),speed);
			break;
		case Constants.E:
			this.setVehEAnticip(x, y, anticipationLenght, this.getOwnerType(),speed);
			break;
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
		this.getAnticipationCells().removeAll(anticipationCells);
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
	//vehicle anticipations
	public void setVehEAnticip(int x, int y, int k, String ownerType, int speed){
//		int safeValue=Constants.GRID_LENGHT-x;		
		AnticipationCell ac;
		int antIndex=k;
//		System.out.println("veh e ant");
			for(int i=x;i<=x+k;i++){
				for(int j=y;j<y+5;j++){
					try{
					ac= new AnticipationCell(new GridPoint((i%Constants.GRID_LENGHT),j),this.getId(),ownerType, speed);
					ac.setIndex(this.setAntIndex(antIndex));
					context.add(ac);
					grid.moveTo(ac,(i%Constants.GRID_LENGHT),j);
					this.getAnticipationCells().add(ac);
					}catch(Exception e){
						e.printStackTrace();
						}
				}
				antIndex++;
			}
//			System.out.println(this.getAnticipationCells().size());
	}
	
	
	public void setVehOAnticip(int x, int y, int k, String ownerType, int speed){
		AnticipationCell ac;
		int antIndex=k;
//		System.out.println("veh o ant");
			for(int i=x;i>x-k;i--){
				for(int j=y;j<y+5;j++){
					try{
//						System.out.println("dir:o");
					ac= new AnticipationCell(new GridPoint(i+Constants.GRID_LENGHT%Constants.GRID_LENGHT,j),this.getId(),ownerType, speed);
					ac.setIndex(this.setAntIndex(antIndex));
					context.add(ac);
					grid.moveTo(ac,i+Constants.GRID_LENGHT%Constants.GRID_LENGHT,j);
					this.getAnticipationCells().add(ac);
					System.out.print(ac.getIndex());
					}catch(Exception e){
						e.printStackTrace();
						}
				}
				antIndex++;
				System.out.println("");
			}
	}
	public int setAntIndex(int k){
		int antIndex=0;
		if(k>=16&&k<=32){
			antIndex=1;
		}
		if(k>=33&&k<=48){
			antIndex=2;
		}
		if(k>=49&&k<=64){
			antIndex=3;
		}
		if(k>=65&&k<=80){
			antIndex=4;
		}
		if(k>=81&&k<=96){
			antIndex=5;
		}
		return antIndex;
	}
	
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
