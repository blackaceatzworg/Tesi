package new1;

import repast.simphony.space.grid.GridPoint;

public class DestinationCell {
	private int x;
	private int y;
	private double p;
	private int relativeDirection;
	private String pedId;
	public DestinationCell(int x2, int y2, double i, int directionIndex, String pedId) {
		// TODO Auto-generated constructor stub
		this.setX(x2);
		this.setY(y2);
		this.setP(i);
		this.setRelativeDirection(directionIndex);
		this.setPedId(pedId);
	}
	public DestinationCell(GridPoint pt, double i, int directionIndex) {
		// TODO Auto-generated constructor stub
		this.setX(pt.getX());
		this.setY(pt.getY());
		this.setP(i);
		this.setRelativeDirection(directionIndex);
	}
	
	public boolean equals(Object ob){
		if(ob==null){
			return false;
		}
		if(!(ob instanceof DestinationCell)){
			return false;
		}
		DestinationCell dc=(DestinationCell) ob;
		return dc.getX()==this.getX()&&dc.getY()==this.getY();
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getRelativeDirection() {
		return relativeDirection;
	}
	public void setRelativeDirection(int relativeDirection) {
		this.relativeDirection = relativeDirection;
	}
	public String getPedId() {
		return pedId;
	}
	public void setPedId(String pedId) {
		this.pedId = pedId;
	}

}
