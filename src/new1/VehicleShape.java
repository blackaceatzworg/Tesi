package new1;

import java.util.ArrayList;

import com.sun.tools.doclets.internal.toolkit.builders.ConstantsSummaryBuilder;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;

public class VehicleShape {
	private int lenght;
	private int width;
	private String ownerId;
	private ArrayList<VehicleShapeCell> shape;
	private Grid grid;
	private Context context;
	
	public void initVehicleShape(int l,int w, String id,Grid<Object> agentGrid, Context context){
		this.setLenght(l);
		this.setWidth(w);
		this.setOwnerId(id);
		this.setGrid(agentGrid);
		this.setContext(context);
		this.setShape(new ArrayList<VehicleShapeCell>());
	}
	
	public void updateVehicleShape(int x, int y, int heading){
		if(heading==Constants.E){
			for(int i=x-this.getLenght();i<=x;i++){
				for(int j=y;j<=y+this.getWidth();j++){
					VehicleShapeCell vsc=new VehicleShapeCell(this.getOwnerId(),i,j);
					this.getShape().add(vsc);
					context.add(vsc);
					System.out.println(i+" "+j);
					grid.moveTo(vsc,i,j);
				}
			}
		}else{
			for(int i=x;i<=x+this.getLenght();i++){
				for(int j=y;j<=y+this.getWidth();j++){
					VehicleShapeCell vsc=new VehicleShapeCell(this.getOwnerId(),i,j);
					this.getShape().add(vsc);
					context.add(vsc);
					System.out.println(i+" "+j);
					grid.moveTo(vsc,i,j);
				}
			}
		}
		
	}
	
	/**
	 * Service method to clear from context vehicle shape at the end of vehicle cycle
	 * */
	public void clearShape(){
		for(VehicleShapeCell vsc:this.getShape()){
			try{
				context.remove(vsc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		this.getShape().removeAll(this.getShape());
	}
	
	
	public int getLenght() {
		return lenght;
	}
	public void setLenght(int lenght) {
		this.lenght = lenght;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public ArrayList<VehicleShapeCell> getShape() {
		return shape;
	}

	public void setShape(ArrayList<VehicleShapeCell> shape) {
		this.shape = shape;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	

}
