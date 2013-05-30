package new1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;
public class ScenarioBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {
	Grid<Object> grid;
	@Override
	public Context<Object> build(Context<Object> context) {
		// TODO Auto-generated method stub
		context.setId("new1");
		//TODO parametrized value
				Parameters params=RunEnvironment.getInstance().getParameters();
				int numberOfLaneParam=(Integer)params.getValue("numberOfLaneParam");
				int numberOfVehicle=(Integer)params.getValue("numberOfVehicle");
				int scenarioHeight=Constants.SINGLE_GRID_HEIGHT;
				int secondLaneOffset=0;
				if(numberOfLaneParam!=1){
					scenarioHeight=Constants.DOUBLE_GRID_HEIGHT;
					secondLaneOffset=8;
				}
				Date dateNow = new Date ();
				SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
		        StringBuilder nowMMDDYYYY = new StringBuilder( date.format(dateNow));

		//data recover
		FileOutputStream pedDirectionLog=null;
		FileOutputStream vehDirectionLog=null;
		FileOutputStream VehicleCounter=null;
		String VehicleCounterFilename="..\'VehicleCounterFolder\'VehicleCount_@VehPerLane"+numberOfVehicle+"@"+nowMMDDYYYY;
		FileOutputStream fieldLog=null;
		FileOutputStream nordcurblog=null;
		FileOutputStream southcurblog=null;
		FileOutputStream nelog=null;
		FileOutputStream nwlog=null;
		FileOutputStream selog=null;
		FileOutputStream swlog=null;
		PrintStream p=null;
		PrintStream ncprint=null;
		PrintStream scprint=null;
		PrintStream neprint=null;
		PrintStream nwprint=null;
		PrintStream seprint=null;
		PrintStream swprint=null;
		
		try{
			pedDirectionLog=new FileOutputStream("PedDirectionLog");
			vehDirectionLog=new FileOutputStream("vehDirectionLog");
			VehicleCounter=new FileOutputStream(VehicleCounterFilename);
			fieldLog=new FileOutputStream("fieldLog");
			nordcurblog=new FileOutputStream("nclog");
			southcurblog=new FileOutputStream("sclog");
			nelog=new FileOutputStream("nelog");
			nwlog=new FileOutputStream("nwlog");
			selog=new FileOutputStream("selog");
			swlog=new FileOutputStream("swlog");
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		
		
		//grid creation
		GridFactory gf=GridFactoryFinder.createGridFactory(null);
		grid=gf.createGrid("ScenarioGrid", context,
				new GridBuilderParameters<Object>(
						new StrictBorders(),
						new SimpleGridAdder<Object>(),
						true,
						Constants.GRID_LENGHT,
						scenarioHeight));
		
		context.add(grid);
		//field creation,
		
		
		final GridValueLayer nordCurbFF=new GridValueLayer(Constants.northCurbFF,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(nordCurbFF);
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<scenarioHeight;j++){
				nordCurbFF.set(Constants.maxint,i,j);
				
			}
		}
		
		final GridValueLayer southCurbFF=new GridValueLayer(Constants.southCurbFF,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(southCurbFF);
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<scenarioHeight;j++){
				southCurbFF.set(Constants.maxint,i,j);
			}
		}
		
		final GridValueLayer southEastFF=new GridValueLayer(Constants.southEastDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(southEastFF);
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<scenarioHeight;j++){
				southEastFF.set(Constants.maxint,i,j);
			}
		}
		final GridValueLayer southWestFF=new GridValueLayer(Constants.southWestDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(southWestFF);
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<scenarioHeight;j++){
				southWestFF.set(Constants.maxint,i,j);
			}
		}
		final GridValueLayer northEastFF=new GridValueLayer(Constants.northEastDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(northEastFF);
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<scenarioHeight;j++){
				northEastFF.set(Constants.maxint,i,j);
			}
		}
		final GridValueLayer northWestFF=new GridValueLayer(Constants.northWestDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(northWestFF);
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<scenarioHeight;j++){
				northWestFF.set(Constants.maxint,i,j);
			}
		}
		//destination areas
		ArrayList<GridPoint> northcurbdestarea=new ArrayList<GridPoint>();
		ArrayList<GridPoint> southcurbdestarea=new ArrayList<GridPoint>();
		for(int i=Constants.GRID_LENGHT/2;i<Constants.GRID_LENGHT/2+6;i++){
			northcurbdestarea.add(new GridPoint(i,13+secondLaneOffset));
			northcurbdestarea.add(new GridPoint(i,14+secondLaneOffset));
			northcurbdestarea.add(new GridPoint(i,15+secondLaneOffset));
			northcurbdestarea.add(new GridPoint(i,16+secondLaneOffset));
			southcurbdestarea.add(new GridPoint(i,4));
			southcurbdestarea.add(new GridPoint(i,3));
			southcurbdestarea.add(new GridPoint(i,2));
			southcurbdestarea.add(new GridPoint(i,1));
		}
		this.calcPathField(nordCurbFF, northcurbdestarea);
		this.calcPathField(southCurbFF, southcurbdestarea);
		
		ArrayList<GridPoint> swDest=new ArrayList<GridPoint>();
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				swDest.add(new GridPoint(i,j));
			}
		}
		this.calcPathField(southWestFF, swDest);
		
		ArrayList<GridPoint> seDest=new ArrayList<GridPoint>();
		for(int i=Constants.GRID_LENGHT-3;i<Constants.GRID_LENGHT;i++){
			for(int j=0;j<3;j++){
				seDest.add(new GridPoint(i,j));
			}
		}
		this.calcPathField(southEastFF, seDest);
		
		ArrayList<GridPoint> neDest=new ArrayList<GridPoint>();
		for(int i=Constants.GRID_LENGHT-3;i<Constants.GRID_LENGHT;i++){
			for(int j=scenarioHeight-3;j<scenarioHeight;j++){
				neDest.add(new GridPoint(i,j));
			}
		}
		this.calcPathField(northEastFF, neDest);
		
		ArrayList<GridPoint> nwDest=new ArrayList<GridPoint>();
		for(int i=0;i<3;i++){
			for(int j=scenarioHeight-3;j<scenarioHeight;j++){
				nwDest.add(new GridPoint(i,j));
			}
		}
		this.calcPathField(northWestFF, nwDest);
		

		//north walkway //nordCurbFF southCurbFF southEastFF southWestFF northEastFF northWestFF
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=14+secondLaneOffset; j<scenarioHeight;j++){
				final SurfaceCell cell=new SurfaceCell(i,j,Constants.Walkway,false);
				context.add(cell);
				grid.moveTo(cell,i,j);
				if(nordCurbFF.get(i,j)==0||southCurbFF.get(i,j)==0||
						southEastFF.get(i,j)==0||
						southWestFF.get(i,j)==0||northEastFF.get(i,j)==0
						||northWestFF.get(i,j)==0){
					cell.setDestination(true);
				}
			}
		}
		//south walkway
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<Constants.WALKWAY_HEIGHT;j++){
				final SurfaceCell cell=new SurfaceCell(i,j,Constants.Walkway,false);
				context.add(cell);
				grid.moveTo(cell,i,j);
				if(nordCurbFF.get(i,j)==0||southCurbFF.get(i,j)==0||
						southEastFF.get(i,j)==0||
						southWestFF.get(i,j)==0||northEastFF.get(i,j)==0
						||northWestFF.get(i,j)==0){
					cell.setDestination(true);
				}
			}
		}
		//curb
		
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			final SurfaceCell cell=new SurfaceCell(i,4,Constants.Curb,false);
			final SurfaceCell cell2=new SurfaceCell(i,13+secondLaneOffset,Constants.Curb,false);
			context.add(cell);
			context.add(cell2);
			grid.moveTo(cell,i,4);
			grid.moveTo(cell2, i,13+secondLaneOffset);
			if(nordCurbFF.get(i,4)==0||southCurbFF.get(i,4)==0||
					southEastFF.get(i,4)==0||
					southWestFF.get(i,4)==0||northEastFF.get(i,4)==0
					||northWestFF.get(i,4)==0){
				cell.setDestination(true);
			}
			if(nordCurbFF.get(i,13+secondLaneOffset)==0||southCurbFF.get(i,13+secondLaneOffset)==0||
					southEastFF.get(i,13+secondLaneOffset)==0||
					southWestFF.get(i,13+secondLaneOffset)==0||northEastFF.get(i,13+secondLaneOffset)==0
					||northWestFF.get(i,13+secondLaneOffset)==0){
				cell2.setDestination(true);
			}
		}
		//roadway
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=5;j<13+secondLaneOffset;j++){
				final SurfaceCell cell=new SurfaceCell(i,j,Constants.Roadway,false);
				context.add(cell);
				grid.moveTo(cell,i,j);
				if(nordCurbFF.get(i,j)==0||southCurbFF.get(i,j)==0||
						southEastFF.get(i,j)==0||
						southWestFF.get(i,j)==0||northEastFF.get(i,j)==0
						||northWestFF.get(i,j)==0){
					cell.setDestination(true);
				}
			}
		}
		
		
		//File log
		for(int j=scenarioHeight-1;j>=0;j--){
			for(int i=0;i<Constants.GRID_LENGHT;i++){
				try {
					//p = new PrintStream(new FileOutputStream("fieldLog",true));
					//p.print(Math.floor(nordCurbFF.get(i,j)*1000)/1000+" ");
					ncprint=new PrintStream(new FileOutputStream("nclog",true));
					ncprint.print(Math.floor(nordCurbFF.get(i,j)*1000)/1000+" ");
					
					scprint=new PrintStream(new FileOutputStream("sclog",true));
					scprint.print(Math.floor(southCurbFF.get(i,j)*1000)/1000+" ");
					
					neprint=new PrintStream(new FileOutputStream("nelog",true));
					neprint.print(Math.floor(northEastFF.get(i,j)*1000)/1000+" ");
					
					nwprint=new PrintStream(new FileOutputStream("nwlog",true));
					nwprint.print(Math.floor(northWestFF.get(i,j)*1000)/1000+" ");
					
					seprint=new PrintStream(new FileOutputStream("selog",true));
					seprint.print(Math.floor(southEastFF.get(i,j)*1000)/1000+" ");
					
					swprint=new PrintStream(new FileOutputStream("swlog",true));
					swprint.print(Math.floor(southWestFF.get(i,j)*1000)/1000+" ");
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println("");
			try {
				ncprint=new PrintStream(new FileOutputStream("nclog",true));
				ncprint.println("");
				scprint=new PrintStream(new FileOutputStream("sclog",true));
				scprint.println("");
				neprint=new PrintStream(new FileOutputStream("nelog",true));
				neprint.println("");
				nwprint=new PrintStream(new FileOutputStream("nwlog",true));
				nwprint.println("");
				seprint=new PrintStream(new FileOutputStream("selog",true));
				seprint.println("");
				swprint=new PrintStream(new FileOutputStream("swlog",true));
				swprint.println("");
				
			} catch (FileNotFoundException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//path fields
		
		ArrayList<String> northSouthRoute=new ArrayList<String>();
		northSouthRoute.add(nordCurbFF.getName());
		northSouthRoute.add(southCurbFF.getName());
		northSouthRoute.add(southEastFF.getName());
		//northSouthRoute.add(southWestFF.getName());
		
		ArrayList<String> northSouthRoute2=new ArrayList<String>();
		northSouthRoute2.add(nordCurbFF.getName());
		northSouthRoute2.add(southCurbFF.getName());
		northSouthRoute2.add(southWestFF.getName());
		
		ArrayList<String> southNorthRoute=new ArrayList<String>();
		southNorthRoute.add(southCurbFF.getName());
		southNorthRoute.add(nordCurbFF.getName());
		southNorthRoute.add(northWestFF.getName());
		
//		//Pedestrian with log
//		Pedestrian ped=new Pedestrian("ped1",grid,"PedDirectionLog");
//		ped.setRoute(northSouthRoute);
//		ped.setCurrentField(ped.getRoute().get(0));
////		ped.setAnticipation(new Anticipation());
//		ped.getAnticipation().initAnticipation(ped.getId(), grid, context);
//		context.add(ped);
//		grid.moveTo(ped,25,22);
////		
//		Pedestrian ped3=new Pedestrian("ped3",grid);
//		ped3.setRoute(southNorthRoute);
//		ped3.setCurrentField(ped3.getRoute().get(0));
//		//ped3.setAnticipation(new Anticipation("ped"));
//		ped3.getAnticipation().initAnticipation(ped3.getId(), grid, context);
//		context.add(ped3);
//		grid.moveTo(ped3,25,2);
//		PedGenerator pedg=new PedGenerator("gen1",context,northSouthRoute,southNorthRoute);
//		context.add(pedg);
		
		VehicleGenerator vg1=new VehicleGenerator("vg1",context,13,6,Constants.E,VehicleCounterFilename);
		context.add(vg1);
		
//		PedGenerator pedg2=new PedGenerator("gen2",context,southNorthRoute,southNorthRoute);
//		context.add(pedg2);
		
//		PedTicker pedt=new PedTicker();
//		context.add(pedt);
		
		VehicleTicker veht=new VehicleTicker();
		context.add(veht);
////		
//		//Pedestrian normal
//		Pedestrian ped2=new Pedestrian("ped2",1,grid);
//		ped2.setRoute(northSouthRoute2);
//		ped2.setCurrentField(ped2.getRoute().get(0));
//		ped2.setAnticipation(new Anticipation());
//		ped2.getAnticipation().initAnticipation(ped.getId(), grid, context);
//		context.add(ped2);
//		grid.moveTo(ped2,30,16);
		

		
//		Pedestrian ped4=new Pedestrian("ped4",1,grid);
//		ped4.setRoute(southNorthRoute);
//		ped4.setCurrentField(ped4.getRoute().get(0));
//		ped4.setAnticipation(new Anticipation());
//		ped4.getAnticipation().initAnticipation(ped.getId(), grid, context);
//		context.add(ped4);
//		grid.moveTo(ped4,30,2);
//		
		
		
		//vehicle
//		Vehicle veh=new Vehicle("v1",0,Constants.E,grid,12,5);
//		veh.getAnticipation().initAnticipation(veh.getId(), grid, context);
////		veh.getVehicleShape().initVehicleShape(12, 5, veh.getId(), grid, context);
//		context.add(veh);
//		grid.moveTo(veh,13,6);
		
		
//		Vehicle veh1=new Vehicle("v1",0,Constants.O,grid,12,5);
//		veh1.getAnticipation().initVehicleAnticipation(veh1.getId(), grid, context);
//		veh1.getAnticipation().setVehicleAnticipation(veh1.getHeading(),159,14,80, veh1.getSpeedZone());
//		veh1.getVehicleShape().initVehicleShape(12,5, veh1.getId(), grid, context);
//		veh1.getVehicleShape().setVehicleShape(160,14,veh1.getHeading());
//		context.add(veh1);
//		grid.moveTo(veh1,160,14);
//////	
//		
//		Vehicle veh2=new Vehicle("v2",2,Constants.E,grid,12,5);
//		veh2.getAnticipation().initVehicleAnticipation(veh2.getId(), grid,context);
//		veh2.getAnticipation().setVehicleAnticipation(veh2.getHeading(),44,6,80, veh2.getSpeedZone());
//		veh2.getVehicleShape().initVehicleShape(12,5, veh2.getId(), grid, context);
//		veh2.getVehicleShape().setVehicleShape(43,6, Constants.E);
//		context.add(veh2);
//		grid.moveTo(veh2,43,6);
//////		
//		Vehicle veh3=new Vehicle("v3",0,Constants.E,grid,12,5);
//		veh3.getAnticipation().initAnticipation(veh3.getId(), grid, context);
//		veh3.getVehicleShape().initVehicleShape(12,5, veh3.getId(), grid, context);
//		veh3.getVehicleShape().setVehicleShape(63,6, Constants.E);
//		context.add(veh3);
//		grid.moveTo(veh3,63,6);
		
		
//		VehicleShapeCell vsc1=new VehicleShapeCell("test", Constants.GRID_LENGHT-50, 6);
//		context.add(vsc1);
//		grid.moveTo(vsc1, Constants.GRID_LENGHT-50,6);
//		VehicleShapeCell vsc2=new VehicleShapeCell("test", Constants.GRID_LENGHT-50, 7);
//		context.add(vsc2);
//		grid.moveTo(vsc2, Constants.GRID_LENGHT-50,7);
//		VehicleShapeCell vsc3=new VehicleShapeCell("test", Constants.GRID_LENGHT-50, 8);
//		context.add(vsc3);
//		grid.moveTo(vsc3, Constants.GRID_LENGHT-50,8);
//		VehicleShapeCell vsc4=new VehicleShapeCell("test", Constants.GRID_LENGHT-50, 9);
//		context.add(vsc4);
//		grid.moveTo(vsc4, Constants.GRID_LENGHT-50,9);
//		VehicleShapeCell vsc5=new VehicleShapeCell("test", Constants.GRID_LENGHT-50, 10);
//		context.add(vsc5);
//		grid.moveTo(vsc5, Constants.GRID_LENGHT-50,10);
//		
		
//		VehicleShapeCell vsc3=new VehicleShapeCell("test", 50,16);
//		context.add(vsc3);
//		grid.moveTo(vsc3, vsc3.getX(),vsc3.getY());
//		
		
		
//		StoppedPed sped2=new StoppedPed("sped2",grid);
//		context.add(sped2);
//		grid.moveTo(sped2, 23,9);
//		StoppedPed sped3=new StoppedPed("sped3",grid);
//		context.add(sped3);
//		grid.moveTo(sped3, 33,9);
//		StoppedPed sped4=new StoppedPed("sped4",grid);
//		context.add(sped4);
//		grid.moveTo(sped4, 43,9);
//		StoppedPed sped5=new StoppedPed("sped5",grid);
//		context.add(sped5);
//		grid.moveTo(sped5, 53,9);
//		

		double endAt = 1333;
		RunEnvironment.getInstance().endAt(endAt);
		return context;
	}
	

	public ArrayList<GridPoint> getNeighb(GridPoint gp){
		Context context=ContextUtils.getContext(this);
		ArrayList<GridPoint> agp=new ArrayList<GridPoint>();
		GridCellNgh<GridPoint> nghc=new GridCellNgh<GridPoint>(grid,gp,GridPoint.class,1,1);
		List<GridCell<GridPoint>> gplist=nghc.getNeighborhood(false);
		for(int i=0;i<gplist.size();i++){
			agp.add(gplist.get(i).getPoint());
		}
		return agp;
	}
	
	public void calcPathField(GridValueLayer gvl, ArrayList<GridPoint> destArea){
		ArrayList<GridPoint> L=new ArrayList<GridPoint>();
		for(int i=0;i<destArea.size();i++){
			L.add(destArea.get(i));
			gvl.set(0,L.get(i).getX(),L.get(i).getY());
		}
		while(L.size()!=0){
			GridPoint pivot=L.get(0);
			double pivotValue=gvl.get(pivot.getX(),pivot.getY());
			L.remove(0);
			ArrayList<GridPoint> N=getNeighb(pivot);
			for(int i=0;i<N.size();i++){
				int x=N.get(i).getX();
				int y=N.get(i).getY();
				double newvalue=pivotValue;
				double nvalue=gvl.get(x,y);
				//escludo cella centrale
				//if(i!=4){
					if(i==0||i==2||i==5||i==7){
						newvalue+=Math.sqrt(2);
					}else{
						newvalue+=1;
					}
				//}
				if(nvalue==Constants.maxint||nvalue>newvalue){
					gvl.set(newvalue, x,y);
					L.add(N.get(i));
				}	
			}
		}
	}
	
	

}
