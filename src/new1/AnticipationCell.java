package new1;

import java.util.ArrayList;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class AnticipationCell {
	private GridPoint gp;
	private int x;
	private int y;
	private int index;
	private String owner;
	private String ownerType;
	private int speed;

	public AnticipationCell(GridPoint gp, String owner, String ownerType){
		this.setOwner(owner);
		this.setGp(gp);
		this.setOwnerType(ownerType);
	}
	
	/**
	 * Constuctor for anticipation cells used by vehicle. Added parameter speed
	 * 
	 * */
	public AnticipationCell(int x, int y, String owner, String ownerType, int speed){
		this.setOwner(owner);
		this.setOwnerType(ownerType);
		this.setSpeed(speed);
	}

	public GridPoint getGp() {
		return gp;
	}

	public void setGp(GridPoint gp) {
		this.gp = gp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
