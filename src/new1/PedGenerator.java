package new1;

import java.util.ArrayList;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class PedGenerator {
	
	private int pedindex;
	private String id;
	private Context context;
	private ArrayList<String> northSouthRoute;
	private ArrayList<String> southNorthRoute;
	Parameters params=RunEnvironment.getInstance().getParameters();
//	pedx
	int pedx=(Integer)params.getValue("pedx");
	
	public PedGenerator(String id,Context context, ArrayList<String> NSroute,ArrayList<String> SNroute){
		this.setPedindex(0);
		this.setId(id);
		this.context=context;
		this.northSouthRoute=NSroute;
		this.southNorthRoute=SNroute;
	}
	
//	@ScheduledMethod(start=0, interval=500)
	public void addPedestrian(){
		Parameters params=RunEnvironment.getInstance().getParameters();
		int numberOfLaneParam=(Integer)params.getValue("numberOfLaneParam");
		int scenarioHeight=Constants.SINGLE_GRID_HEIGHT;
		int secondLaneOffset=0;
		if(numberOfLaneParam!=1){
			scenarioHeight=Constants.DOUBLE_GRID_HEIGHT;
			secondLaneOffset=8;
		}
		Grid<Object> grid=(Grid<Object>)context.getProjection(Constants.GridID);
		String id=this.getId()+"-"+this.getPedindex();
		this.pedindex++;
		Random r=new Random();
//		int rx=r.nextInt(Constants.GRID_LENGHT);
		int rx=0;
		int ry=3;
		boolean eo=r.nextBoolean();
		boolean ns=r.nextBoolean();
		int head=4;
		int off=r.nextInt(2);
		Pedestrian ped=new Pedestrian(id,grid);
		if(ns){
			ped.setRoute(this.getNorthSouthRoute());
			ry=scenarioHeight-3;
			head=1;
		}else{
			ped.setRoute(this.getSouthNorthRoute());
			ry=3;
			head=7;
		}
		if(eo){
			rx=Constants.GRID_LENGHT/2+pedx+off;
		}
		else{
			rx=Constants.GRID_LENGHT/2-1-pedx-off;
		}
		ped.setCurrentField(ped.getRoute().get(0));
		ped.getAnticipation().initAnticipation(ped.getId(), grid, context);
		ped.getAnticipation().setPedestrianAnticipation(head, rx, ry);
		context.add(ped);
		grid.moveTo(ped,rx,ry);
	}

	public int getPedindex() {
		return pedindex;
	}

	public void setPedindex(int pedindex) {
		this.pedindex = pedindex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<String> getNorthSouthRoute() {
		return northSouthRoute;
	}

	public void setNorthSouthRoute(ArrayList<String> northSouthRoute) {
		this.northSouthRoute = northSouthRoute;
	}

	public ArrayList<String> getSouthNorthRoute() {
		return southNorthRoute;
	}

	public void setSouthNorthRoute(ArrayList<String> southNorthRoute) {
		this.southNorthRoute = southNorthRoute;
	}

}
