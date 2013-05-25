package new1;

public class VehicleShapeCell {
	
	private int x;
	private int y;
	private String owner;
	
	public VehicleShapeCell(String owner, int x, int y){
		this.setOwner(owner);
		this.setX(x);
		this.setY(y);
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

}
