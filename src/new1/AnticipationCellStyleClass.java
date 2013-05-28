package new1;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class AnticipationCellStyleClass extends DefaultStyleOGL2D {
	
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
		
		if(agent instanceof AnticipationCell){
			
			if(((AnticipationCell) agent).getIndex()==1){
				return new Color(0xFF4A28);
			}
			if(((AnticipationCell) agent).getIndex()==2){
				return new Color(0xFF873A);
			}
			if(((AnticipationCell) agent).getIndex()==3){
				return new Color(0xFFDF44);
			}
			if(((AnticipationCell) agent).getIndex()==4){
				return new Color(0xebff41);
			}
			if(((AnticipationCell) agent).getIndex()==5){
				return new Color(0x47FF3C);
			}
		}
		
		return new Color(0xFFFFFF);}
}
