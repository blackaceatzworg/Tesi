package new1;

import java.util.ArrayList;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class AnticipationCell {
	private GridPoint gp;
	private int index;
	private String owner;
	private String ownerType;

	public AnticipationCell(GridPoint gp, String owner, String ownerType){
		this.setOwner(owner);
		this.setGp(gp);
		this.setOwnerType(ownerType);
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
}
