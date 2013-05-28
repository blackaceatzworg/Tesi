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
//		this.setVehicleShape(x,y,heading);
	}
	
	public void setVehicleShape(int x, int y, int heading){
		VehicleShapeCell vsc;
		if(heading==Constants.E){
			for(int i=0;i<this.getLenght();i++){
				for(int j=0;j<this.getWidth();j++){
					try {
//						System.out.println((x-i)%Constants.GRID_LENGHT+" "+j+"-ciclo");
						vsc=new VehicleShapeCell(this.getOwnerId(),(x-i)%Constants.GRID_LENGHT,y+j);
						context.add(vsc);
//						System.out.println(vsc.getX()+" "+vsc.getY()+"-cella");
						grid.moveTo(vsc,vsc.getX(),vsc.getY());
						this.getShape().add(vsc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			for(int i=x;i<=x+this.getLenght();i++){
				for(int j=y;j<y+this.getWidth();j++){
					try {
						vsc=new VehicleShapeCell(this.getOwnerId(),i,j);
						context.add(vsc);
//						System.out.println(i+" "+j);
						grid.moveTo(vsc,i,j);
						this.getShape().add(vsc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public void alternateUpdate(int mod){
//		System.out.println(this.getShape().size()+"");
		for(VehicleShapeCell vsc: this.getShape()){
			vsc.setX(vsc.getX()+mod);
			context.add(vsc);
			grid.moveTo(vsc,vsc.getX()%Constants.GRID_LENGHT,vsc.getY());
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
//		this.getShape().removeAll(this.getShape());
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
