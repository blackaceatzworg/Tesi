package new1;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class SurfaceClassStyle extends DefaultStyleOGL2D {
	
	@Override
	public void init(ShapeFactory2D factory) {
	    this.shapeFactory = factory;
	  }
	
	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
	    if (spatial == null) {
	      spatial = shapeFactory.createRectangle(13, 13);
	    }
	    return spatial;
	  }
	
	@Override
	public Color getColor(final Object agent){
		
		if(agent instanceof SurfaceCell){
			
			if(((SurfaceCell) agent).isDestination()){
				return new Color(0x82FF66);
			}
			if(((SurfaceCell) agent).getSurfaceType()==Constants.Curb){
				return new Color(0x808080);
			}
			if(((SurfaceCell) agent).getSurfaceType()==Constants.Roadway){
				return new Color(0x4A4344);
			}
			if(((SurfaceCell) agent).getSurfaceType()==Constants.Walkway){
				return new Color(0xC0C0C0);
			}
			
		}
		
		return new Color(0xFFFFFF);}

}
