package new1;

import java.util.ArrayList;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class PedGenerator {
	
	private int pedindex;
	private String id;
	private Context context;
	private ArrayList<String> route1;
	private ArrayList<String> route2;
	private int height;
	
	public PedGenerator(String id,Context context, ArrayList<String> route1,ArrayList<String> route2,int ry){
		this.setPedindex(0);
		this.setId(id);
		this.context=context;
		this.route1=route1;
		this.route2=route2;
		this.setHeight(ry);
	}
	
	@ScheduledMethod(start=1, interval=40)
	public void addPedestrian(){
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		String id=this.getId()+"-"+this.getPedindex();
		this.pedindex++;
		Random r=new Random();
		int direction=r.nextInt(9);
		int rx=r.nextInt(Constants.GRID_LENGHT);
		boolean ns=r.nextBoolean();
		int ry=this.getHeight();
		Pedestrian ped=new Pedestrian(id,direction,grid);
		if(ns){
			ped.setRoute(route1);
		}else{
			ped.setRoute(route2);
		}
		ped.setCurrentField(ped.getRoute().get(0));
		ped.setAnticipation(new Anticipation());
		ped.getAnticipation().initAnticipation(ped.getId(), grid, context);
		context.add(ped);
		grid.moveTo(ped, rx,ry);
	}

	public int getPedindex() {
		return pedindex;
	}

	public void setPedindex(int pedindex) {
		this.pedindex = pedindex;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
