package new1;

import repast.simphony.context.Context;
import repast.simphony.util.ContextUtils;

public class AgentUtils {
	public static String translateHeading(int i){
		String s="";
		switch(i){
		case Constants.SO:
			s="so";break;
		case Constants.O:
			s="o";break;
		case Constants.NO:
			s="no";break;
		case Constants.S:
			s="s";break;
		case Constants.N:
			s="n";break;
		case Constants.SE:
			s="se";break;
		case Constants.E:
			s="e";break;
		case Constants.NE:
			s="ne";break;
		case 4:
			s="x";break;
		}
		return s;
	}
	
	public static void checkDestination(SurfaceCell sc, Pedestrian ped){
		if(sc.isDestination()){
			ContextUtils.getContext(ped).remove(ped);
		}
	}
}
