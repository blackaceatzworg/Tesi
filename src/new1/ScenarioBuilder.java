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
		Constants.vehicleCounter=0;
		Constants.crossedPedCounter=0;
		context.setId("new1");
		//TODO parametrized value
				Parameters params=RunEnvironment.getInstance().getParameters();
				int numberOfLaneParam=(Integer)params.getValue("numberOfLaneParam");
				int numberOfVehicle=(Integer)params.getValue("numberOfVehicle");
				int anticipationModule=(Integer)params.getValue("anticipationModule");
				int numberOfPedestrian=(Integer)params.getValue("numOfPed");
				int pedOffsetGeneration=(Integer)params.getValue("pedx");
				int scenarioHeight=Constants.SINGLE_GRID_HEIGHT;
				int secondLaneOffset=0;
				if(numberOfLaneParam!=1){
					scenarioHeight=Constants.DOUBLE_GRID_HEIGHT;
					secondLaneOffset=8;
				}
				Date dateNow = new Date ();
				SimpleDateFormat date = new SimpleDateFormat("dd_MM__HH_mm");
		        StringBuilder nowMMDDYYYY = new StringBuilder( date.format(dateNow));

		//data recover
//		FileOutputStream pedDirectionLog=null;
//		FileOutputStream vehDirectionLog=null;
		
//		FileOutputStream VehicleCounter=null;
//		String VehicleCounterFilename="VCount_V@Lane"+numberOfVehicle+"_antMod"+anticipationModule+"_"+nowMMDDYYYY;
//		PrintStream vehicleCounterPrintStream=null;
//		
//		FileOutputStream PedestrianCounter=null;
//		String PedestrianCounterFilename="generatedPed"+numberOfPedestrian+"@offset"+pedOffsetGeneration+"_"+nowMMDDYYYY;
//		PrintStream pedestrianCounterPrintStream=null;
		
		FileOutputStream testFOS=null;
		String TestFilename="Test_Veh"+numberOfVehicle+"_Ped"+numberOfPedestrian+"_Pedoffset"+pedOffsetGeneration+"_"+nowMMDDYYYY+".txt";
//		String TestFilename="test";
		PrintStream testStream=null;
		
		
		
//		FileOutputStream fieldLog=null;
		FileOutputStream nordcurblog=null;
		PrintStream ncprint=null;
		FileOutputStream southcurblog=null;
		PrintStream scprint=null;
		FileOutputStream nelog=null;
		PrintStream neprint=null;
		FileOutputStream nwlog=null;
		PrintStream nwprint=null;
		FileOutputStream selog=null;
		PrintStream seprint=null;
		FileOutputStream swlog=null;
		PrintStream swprint=null;
		
		try{
//			pedDirectionLog=new FileOutputStream("PedDirectionLog");
//			vehDirectionLog=new FileOutputStream("vehDirectionLog");
			
//			VehicleCounter=new FileOutputStream(VehicleCounterFilename);
//			vehicleCounterPrintStream = new PrintStream(new FileOutputStream(VehicleCounterFilename,true));
//			vehicleCounterPrintStream.println("id,tick,speed(float),speed(discrete),Tot v");
//			
//			PedestrianCounter=new FileOutputStream(VehicleCounterFilename);
//			pedestrianCounterPrintStream= new PrintStream(new FileOutputStream(PedestrianCounterFilename,true));
//			vehicleCounterPrintStream.println();
//			
			testFOS=new FileOutputStream(TestFilename);
			testStream= new PrintStream(new FileOutputStream(TestFilename,true));
			testStream.println("tick Veh"+numberOfVehicle+"_Ped"+numberOfPedestrian);
			
//			fieldLog=new FileOutputStream("fieldLog");
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
		
		final GridValueLayer northDestFF=new GridValueLayer(Constants.NorthDestFF,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(northDestFF);

		final GridValueLayer southDestFF=new GridValueLayer(Constants.southDestFF,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(southDestFF);
		
		final GridValueLayer nordCurbFF=new GridValueLayer(Constants.northCurbFF,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(nordCurbFF);
		
		
		final GridValueLayer southCurbFF=new GridValueLayer(Constants.southCurbFF,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(southCurbFF);
		
		final GridValueLayer southEastFF=new GridValueLayer(Constants.southEastDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(southEastFF);
		
		final GridValueLayer southWestFF=new GridValueLayer(Constants.southWestDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(southWestFF);
		
		final GridValueLayer northEastFF=new GridValueLayer(Constants.northEastDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(northEastFF);
		
		final GridValueLayer northWestFF=new GridValueLayer(Constants.northWestDest,true,new StrictBorders(),Constants.GRID_LENGHT,scenarioHeight);
		context.addValueLayer(northWestFF);
		
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<scenarioHeight;j++){
				southDestFF.set(Constants.maxint,i,j);
				northDestFF.set(Constants.maxint,i,j);
				northWestFF.set(Constants.maxint,i,j);
				northEastFF.set(Constants.maxint,i,j);
				southWestFF.set(Constants.maxint,i,j);
				southEastFF.set(Constants.maxint,i,j);
				southCurbFF.set(Constants.maxint,i,j);
				nordCurbFF.set(Constants.maxint,i,j);
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
		
		ArrayList<GridPoint> southDest=new ArrayList<GridPoint>();
		ArrayList<GridPoint> northDest=new ArrayList<GridPoint>();
		for(int i=Constants.GRID_LENGHT/2;i<Constants.GRID_LENGHT/2+6;i++){
			southDest.add(new GridPoint(i,0));
			northDest.add(new GridPoint(i,scenarioHeight-1));
		}
		this.calcPathField(southDestFF, southDest);
		this.calcPathField(northDestFF, northDest);
		
		
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
//				if(nordCurbFF.get(i,j)==0||southCurbFF.get(i,j)==0||
//						southEastFF.get(i,j)==0||
//						southWestFF.get(i,j)==0||northEastFF.get(i,j)==0
//						||northWestFF.get(i,j)==0||southDestFF.get(i,j)==0||northDestFF.get(i,j)==0){
//					cell.setDestination(true);
//				}
			}
		}
		//south walkway
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=0; j<Constants.WALKWAY_HEIGHT;j++){
				final SurfaceCell cell=new SurfaceCell(i,j,Constants.Walkway,false);
				context.add(cell);
				grid.moveTo(cell,i,j);
//				if(nordCurbFF.get(i,j)==0||southCurbFF.get(i,j)==0||
//						southEastFF.get(i,j)==0||
//						southWestFF.get(i,j)==0||northEastFF.get(i,j)==0
//						||northWestFF.get(i,j)==0||southDestFF.get(i,j)==0||northDestFF.get(i,j)==0){
//					cell.setDestination(true);
//				}
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
//			if(nordCurbFF.get(i,4)==0||southCurbFF.get(i,4)==0||
//					southEastFF.get(i,4)==0||
//					southWestFF.get(i,4)==0||northEastFF.get(i,4)==0
//					||northWestFF.get(i,4)==0){
//				cell.setDestination(true);
//			}
//			if(nordCurbFF.get(i,13+secondLaneOffset)==0||southCurbFF.get(i,13+secondLaneOffset)==0||
//					southEastFF.get(i,13+secondLaneOffset)==0||
//					southWestFF.get(i,13+secondLaneOffset)==0||northEastFF.get(i,13+secondLaneOffset)==0
//					||northWestFF.get(i,13+secondLaneOffset)==0){
//				cell2.setDestination(true);
//			}
		}
		//roadway
		for(int i=0;i<Constants.GRID_LENGHT;i++){
			for(int j=5;j<13+secondLaneOffset;j++){
				final SurfaceCell cell=new SurfaceCell(i,j,Constants.Roadway,false);
				context.add(cell);
				grid.moveTo(cell,i,j);
//				if(nordCurbFF.get(i,j)==0||southCurbFF.get(i,j)==0||
//						southEastFF.get(i,j)==0||
//						southWestFF.get(i,j)==0||northEastFF.get(i,j)==0
//						||northWestFF.get(i,j)==0||southDestFF.get(i,j)==0||northDestFF.get(i,j)==0){
//					cell.setDestination(true);
//				}
			}
		}
		
		//zebracross
		//Accessories for visualization
		for(int i=Constants.GRID_LENGHT/2;i<Constants.GRID_LENGHT/2+6;i++){
			for(int j=5;j<13+secondLaneOffset;j++){
				if(j%2==0){
				final ZebraCross zb=new ZebraCross();
				context.add(zb);
				grid.moveTo(zb, i,j);}
			}
		}
		
		
		//Pedestrian routes
		
		ArrayList<String> northSouthRoute=new ArrayList<String>();
		northSouthRoute.add(nordCurbFF.getName());
		northSouthRoute.add(southCurbFF.getName());
		northSouthRoute.add(southDestFF.getName());
		//northSouthRoute.add(southWestFF.getName());
		
		ArrayList<String> southNorthRoute=new ArrayList<String>();
		southNorthRoute.add(southCurbFF.getName());
		southNorthRoute.add(nordCurbFF.getName());
		southNorthRoute.add(northDestFF.getName());
		
		
		

		//Agent actors
		AgentManager scenarioManager=new AgentManager(TestFilename,1000);
		context.add(scenarioManager);
		
//		VehicleGenerator vg=new VehicleGenerator("vehG",context,13,6,Constants.E,VehicleCounterFilename);
		VehicleGenerator vg=new VehicleGenerator("vehG",context,13,6,Constants.E);
		context.add(vg);
//		
////		PedGenerator pedg=new PedGenerator("pedG",context,northSouthRoute,southNorthRoute,PedestrianCounterFilename);
		PedGenerator pedg=new PedGenerator("pedG",context,northSouthRoute,southNorthRoute);
		context.add(pedg);
		

		
//		TEST VEHICLE + oblstacle///////////////////////
//		
//		Vehicle veh2=new Vehicle(VehicleCounterFilename,"v2",1,Constants.E,grid,12,5);
//		veh2.getAnticipation().initVehicleAnticipation(veh2.getId(), grid,context);
//		veh2.getAnticipation().setVehicleAnticipation(veh2.getHeading(),Constants.GRID_LENGHT-10,6,80, veh2.getSpeedZone());
//		veh2.getVehicleShape().initVehicleShape(12,5, veh2.getId(), grid, context);
//		veh2.getVehicleShape().setVehicleShape(Constants.GRID_LENGHT-11,6, Constants.E);
//		context.add(veh2);
//		grid.moveTo(veh2,Constants.GRID_LENGHT-11,6);
//		
//		VehicleShapeCell vsc1=new VehicleShapeCell("test-v", 310, 6);
//		context.add(vsc1);
//		grid.moveTo(vsc1, 310,6);
////////	
//		
//		StoppedPed sped2=new StoppedPed("test-p",grid);
//		context.add(sped2);
//		grid.moveTo(sped2, 20,6);
//		sped2.project(context, 20, 6);
//		VehicleShapeCell ac;
//		for(int i=Constants.GRID_LENGHT/2-6;i<Constants.GRID_LENGHT/2+12;i++){
//			for(int j=5;j<12;j++){
//				ac=new  VehicleShapeCell("test-v", i, j);
////				ac.setIndex(1);
//				context.add(ac);
//				grid.moveTo(ac, i,j);
//			}
//		}
		for(int j=scenarioHeight-1;j>=0;j--){
			for(int i=0;i<Constants.GRID_LENGHT;i++){
				try {
					//p = new PrintStream(new FileOutputStream("fieldLog",true));
					//p.print(Math.floor(nordCurbFF.get(i,j)*1000)/1000+" ");
					ncprint=new PrintStream(new FileOutputStream("nclog",true));
					ncprint.print(Math.floor(nordCurbFF.get(i,j)*1000)/1000+" ");
					
					scprint=new PrintStream(new FileOutputStream("sclog",true));
					scprint.print(Math.floor(southCurbFF.get(i,j)*1000)/1000+" ");
					
//					neprint=new PrintStream(new FileOutputStream("nelog",true));
//					neprint.print(Math.floor(northEastFF.get(i,j)*1000)/1000+" ");
//					
//					nwprint=new PrintStream(new FileOutputStream("nwlog",true));
//					nwprint.print(Math.floor(northWestFF.get(i,j)*1000)/1000+" ");
//					
//					seprint=new PrintStream(new FileOutputStream("selog",true));
//					seprint.print(Math.floor(southEastFF.get(i,j)*1000)/1000+" ");
//					
//					swprint=new PrintStream(new FileOutputStream("swlog",true));
//					swprint.print(Math.floor(southWestFF.get(i,j)*1000)/1000+" ");
					
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


		double endAt = 13333;
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
