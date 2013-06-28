package new1;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;

public class StoppedPed extends Pedestrian {

	public StoppedPed(String id, Grid<Object> grid) {
		super(id, grid);
		// TODO Auto-generated constructor stub
	}
	
	public void project(Context context, int x, int y){
		Anticipation ac=new Anticipation("stopped");
		ac.initAnticipation("1", this.getGrid(), context);
		for(int i=0;i<9;i++){
			if(i!=4){
			ac.setPedestrianAnticipation(i, x, y);}
		}
	}

}
