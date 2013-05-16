package new1;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class SurfaceCell {
	private final int xcor;
	private final int ycor;
	private int surfaceType;
	private boolean isDestination;
	private GridPoint surfaceCellPoint;
	private boolean free;
	
	
	public SurfaceCell(int x, int y,int st, boolean dest){
		this.xcor=x;
		this.ycor=y;
		this.setSurfaceType(st);
		this.setDestination(dest);
		this.setSurfaceCellPoint(new GridPoint(this.getXcor(),this.getYcor()));
		this.setFree(true);
	}
	
	//@ScheduledMethod(start=1,interval=1)
	public void step(){
		Context context=ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection(Constants.GridID);
		int x=env.getLocation(this).getX();
		int y=env.getLocation(this).getY();
		boolean check=true;
		for(Object ags : env.getObjectsAt(x,y)){
			if(ags instanceof Pedestrian){
				check=false;
			}
		}
		if(!check)
		this.setFree(check);
	}
	
	
	
	
	public int getSurfaceType() {
		return surfaceType;
	}
	public void setSurfaceType(int surfaceType) {
		this.surfaceType = surfaceType;
	}
	public int getXcor() {
		return xcor;
	}
	public int getYcor() {
		return ycor;
	}
	public boolean isDestination() {
		return isDestination;
	}

	public void setDestination(boolean isDestination) {
		this.isDestination = isDestination;
	}
	public GridPoint getSurfaceCellPoint() {
		return surfaceCellPoint;
	}
	public void setSurfaceCellPoint(GridPoint surfaceCellPoint) {
		this.surfaceCellPoint = surfaceCellPoint;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}

}
