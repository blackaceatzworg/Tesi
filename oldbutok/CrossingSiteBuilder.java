/**
 * 
 */
package CrossingSiteModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;
import repast.simphony.engine.schedule.*;

/**
 * @author Lor3nz
 *
 */
public class CrossingSiteBuilder implements ContextBuilder<Object> {

	Grid<Object> grid;
	
	@Override
	public Context build(Context<Object> context) {
		// TODO Auto-generated method stub
		context.setId("CrossingSiteModel");
		DataCollector.setCurrentVehicleNumber(0);
		Parameters params=RunEnvironment.getInstance().getParameters();
		boolean regulation=(Boolean) params.getValue("regulation");
		int numberOfLane=(Integer)params.getValue("lane");
		int regulationType=Values.RegulatedRoad;
		String regulatedString="Stripes";
		String laneString="";
		if(regulation==false){
			regulationType=Values.FreeRoad;
			regulatedString="NOStripes";
		}
		if(numberOfLane==1){
			laneString="singleLane";
		}else{
			laneString="doubleLane";
		}
		
		Date dateNow = new Date ();
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
        StringBuilder nowMMDDYYYY = new StringBuilder( date.format(dateNow));
		FileOutputStream fosVeh=null;
		FileOutputStream fosPed=null;
		String filecount=nowMMDDYYYY+"_passedV_"+laneString+"_"+regulatedString;
		String filecountPed=nowMMDDYYYY+"_passedPed"+laneString+"_"+regulatedString;
		try {
			fosVeh=new FileOutputStream(filecount);
			fosPed=new FileOutputStream(filecountPed);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//SINGLE LANE ROAD
		if(numberOfLane==Values.singleLaneRoad){
			
			//int nPed=(Integer) params.getValue("nPed"); 
			//Build grid projection
			grid=gridGenerator("grid",Values.xgrid,Values.ygridSingle,context);
			
			//Pedestrian Destination
			Destination dest1=new Destination(156,Values.ygridSingle-3,"DestinationFF","CrossingField","Pavementfield");
			context.add(dest1);
			dest1.setHpos(Values.north);
			dest1.setId("d1");
			grid.moveTo(dest1, 156,Values.ygridSingle-3);
			
			Destination dest2=new Destination(156,3,"DestinationFF1","CrossingField1","Pavementfield1");
			context.add(dest2);
			dest2.setHpos(Values.south);
			dest2.setId("d2");
			grid.moveTo(dest2, 156,3);
			
//			Destination dest1=new Destination(24,18);
//			context.add(dest1);
//			grid.moveTo(dest1, 24,18);
			
			//Vehicle destination
			Destination Vdest=new Destination(0,9,"CarDestinationFF",null,null);
			context.add(Vdest);
			grid.moveTo(Vdest, 0,9);
			//
			//Add pedestrian distance field
			GridValueLayer gvl=new GridValueLayer("DestinationFF",false,new WrapAroundBorders(),Values.xgrid,Values.ygridSingle);
			context.addValueLayer(gvl);
			for(int i=0;i<=Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygridSingle-1;j++){
					double dist=Math.abs(i-dest1.getxCor())+Math.abs(j-dest1.getyCor());//+100;
					gvl.set(dist,i,j);
					}
				}
			for(int i=0;i<=Values.xgrid;i++){
				gvl.set(10000, i,0);
				gvl.set(10000, i,Values.ygridSingle-1);
			}
			for(int i=0;i<=Values.ygridSingle;i++){
				gvl.set(10000, 0,i);
				gvl.set(10000, Values.xgrid-1,i);
			}
			
			GridValueLayer gvl1=new GridValueLayer("DestinationFF1",false,new WrapAroundBorders(),Values.xgrid,Values.ygridSingle);
			context.addValueLayer(gvl1);
			for(int i=0;i<=Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygridSingle-1;j++){
					double dist=Math.abs(i-dest2.getxCor())+Math.abs(j-dest2.getyCor());//+100;
					gvl1.set(dist,i,j);
					}
				}
			for(int i=0;i<=Values.xgrid;i++){
				gvl1.set(10000, i,0);
				gvl1.set(10000, i,Values.ygridSingle-1);
			}
			for(int i=0;i<=Values.ygridSingle;i++){
				gvl1.set(10000, 0,i);
				gvl1.set(10000, Values.xgrid-1,i);
			}
			//Add crossing field
			
			
			GridValueLayer rdd=new GridValueLayer("CrossingField",false,new WrapAroundBorders(),Values.xgrid,Values.ygridSingle);
			context.addValueLayer(rdd);
			int curbs=0;
			if(dest1.getyCor()<=Values.ygridSingle-1&&dest1.getyCor()>=23){
				curbs=22;
			}
			if(dest1.getyCor()<=4&&dest1.getyCor()>=0){
				curbs=5;
			}
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygridSingle-1;j++){
					double dist=Math.abs(j-dest1.getyCor());
					rdd.set(dist,i,j);
					}
			}
			
			GridValueLayer rdd1=new GridValueLayer("CrossingField1",false,new WrapAroundBorders(),Values.xgrid,Values.ygridSingle);
			context.addValueLayer(rdd1);
			int curbs1=0;
			if(dest2.getyCor()<=Values.ygridSingle-1&&dest2.getyCor()>=23){
				curbs=22;
			}
			if(dest2.getyCor()<=4&&dest2.getyCor()>=0){
				curbs=5;
			}
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygridSingle-1;j++){
					double dist=Math.abs(j-dest2.getyCor());
					rdd1.set(dist,i,j);
					}
			}

			//Add PAVEMENT  field distance field
			GridValueLayer pavDF=new GridValueLayer("Pavementfield",false,new WrapAroundBorders(),Values.xgrid,Values.ygridSingle);
			context.addValueLayer(pavDF);
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygridSingle;j++){
					double dist=Math.abs(i-dest2.getxCor());
					pavDF.set(dist,i,j);
					}
			}
			for(int i=0;i<=Values.xgrid;i++){
				pavDF.set(10000, i,0);
				pavDF.set(10000, i,Values.ygridSingle-1);
			}
			for(int i=0;i<=Values.ygridSingle;i++){
				pavDF.set(10000, 0,i);
				pavDF.set(10000, Values.xgrid-1,i);
			}
			
			GridValueLayer pavDF1=new GridValueLayer("Pavementfield1",false,new WrapAroundBorders(),Values.xgrid,Values.ygridSingle);
			context.addValueLayer(pavDF1);
			int pivot1=0;
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygridSingle-1;j++){
					double dist=Math.abs(i-dest1.getxCor());
					pavDF1.set(dist,i,j);
					}
			}
			for(int i=0;i<=Values.xgrid;i++){
				pavDF1.set(10000, i,0);
				pavDF1.set(10000, i,Values.ygridSingle-1);
			}
			for(int i=0;i<=Values.ygridSingle;i++){
				pavDF1.set(10000, 0,i);
				pavDF1.set(10000, Values.xgrid-1,i);
			}
			//Add car distance field
					GridValueLayer gvlCar=new GridValueLayer("CarDestinationFF",false,new WrapAroundBorders(),Values.xgrid,Values.ygridSingle);
					context.addValueLayer(gvlCar);
					for(int i=0;i<=Values.xgrid-1;i++){
						for(int j=0;j<=Values.ygridSingle-1;j++){
							double dist=Math.abs(i-Vdest.getxCor())+Math.abs(j-Vdest.getyCor());//+100;
							gvlCar.set(dist,i,j);
							}
						}
			
			//ENVIRONMENT
			//add curb
			StaticElement curb;
			for(int i=0;i<Values.xgrid;i++){
				curb=new StaticElement(new GridPoint(i,5),"curb",gvl.get(i,5));
				context.add(curb);
				grid.moveTo(curb,i,5);
			}
			
			for(int i=0;i<Values.xgrid;i++){
				curb=new StaticElement(new GridPoint(i,Values.ygridSingle-5),"curb",gvl.get(i,Values.ygridSingle-5));
				context.add(curb);
				grid.moveTo(curb,i,Values.ygridSingle-6);
			}
			
			//add walkable
			StaticElement walkable;
			for(int i=0;i<Values.xgrid;i++){
				for(int j=0;j<5;j++){
					walkable=new StaticElement(new GridPoint(i,j),"walkable",gvl.get(i,j));
					context.add(walkable);
					grid.moveTo(walkable,i,j);
				}
			}
			for(int i=0;i<Values.xgrid;i++){
				for(int j=Values.ygridSingle-5;j<Values.ygridSingle;j++){
					walkable=new StaticElement(new GridPoint(i,j),"walkable",gvl.get(i,j));
					context.add(walkable);
					grid.moveTo(walkable,i,j);
				}
			}
			
			//add roadWay
					StaticElement roadway;
					for(int i=0;i<Values.xgrid;i++){
						for(int j=6;j<14;j++){
							roadway=new StaticElement(new GridPoint(i,j),"roadway",rdd.get(i,j));
							context.add(roadway);
							grid.moveTo(roadway,i,j);
						}
//						for(int j=14;j<22;j++){
//							roadway=new StaticElement(new GridPoint(i,j),"roadway",rdd.get(i,j));
//							context.add(roadway);
//							grid.moveTo(roadway,i,j);
//						}
					}
			
					PedGenerator ag1=new PedGenerator("PedOf1",dest1,regulationType,filecountPed);
					context.add(ag1);
					PedGenerator ag2=new PedGenerator("PedOf2",dest2,regulationType,filecountPed);
					context.add(ag2);
					VehicleGenerator vg1=new VehicleGenerator("VehOf1",Vdest,numberOfLane,filecount);
					context.add(vg1);
		}else{
//			//DOUBLE LANE ROAD
			//Build grid projection
			grid=gridGenerator("grid",Values.xgrid,Values.ygrid,context);
			
			//Pedestrian Destination
			Destination dest1=new Destination(186,Values.ygrid-3,"DestinationFF","CrossingField","Pavementfield");
			context.add(dest1);
			dest1.setHpos(Values.north);
			dest1.setId("d1");
			grid.moveTo(dest1, 186,Values.ygrid-3);
			
			Destination dest2=new Destination(186,3,"DestinationFF1","CrossingField1","Pavementfield1");
			context.add(dest2);
			dest2.setHpos(Values.south);
			dest2.setId("d2");
			grid.moveTo(dest2, 186,3);
			

			//Vehicle destination
			Destination Vdest=new Destination(0,9,"CarDestinationFF",null,null);
			context.add(Vdest);
			grid.moveTo(Vdest, 0,9);
			
			Destination Vdest1=new Destination(0,17,"CarDestinationFF",null,null);
			context.add(Vdest);
			grid.moveTo(Vdest, 0,17);
			
			//
			//Add pedestrian distance field
			GridValueLayer gvl=new GridValueLayer("DestinationFF",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
			context.addValueLayer(gvl);
			for(int i=0;i<=Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygrid-1;j++){
					double dist=Math.abs(i-dest1.getxCor())+Math.abs(j-dest1.getyCor());//+100;
					gvl.set(dist,i,j);
					}
				}
			for(int i=0;i<=Values.xgrid;i++){
				gvl.set(10000, i,0);
				gvl.set(10000, i,Values.ygrid-1);
			}
			for(int i=0;i<=Values.ygrid;i++){
				gvl.set(10000, 0,i);
				gvl.set(10000, Values.xgrid-1,i);
			}
			
			GridValueLayer gvl1=new GridValueLayer("DestinationFF1",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
			context.addValueLayer(gvl1);
			for(int i=0;i<=Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygrid-1;j++){
					double dist=Math.abs(i-dest2.getxCor())+Math.abs(j-dest2.getyCor());//+100;
					gvl1.set(dist,i,j);
					}
				}
			for(int i=0;i<=Values.xgrid;i++){
				gvl1.set(10000, i,0);
				gvl1.set(10000, i,Values.ygrid-1);
			}
			for(int i=0;i<=Values.ygrid;i++){
				gvl1.set(10000, 0,i);
				gvl1.set(10000, Values.xgrid-1,i);
			}
			//Add crossing field
			
			
			GridValueLayer rdd=new GridValueLayer("CrossingField",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
			context.addValueLayer(rdd);
			int curbs=0;
			if(dest1.getyCor()<=Values.ygrid-1&&dest1.getyCor()>=23){
				curbs=22;
			}
			if(dest1.getyCor()<=4&&dest1.getyCor()>=0){
				curbs=5;
			}
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygrid-1;j++){
					double dist=Math.abs(j-dest1.getyCor());
					rdd.set(dist,i,j);
					}
			}
			
			GridValueLayer rdd1=new GridValueLayer("CrossingField1",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
			context.addValueLayer(rdd1);
			int curbs1=0;
			if(dest2.getyCor()<=Values.ygrid-1&&dest2.getyCor()>=23){
				curbs=22;
			}
			if(dest2.getyCor()<=4&&dest2.getyCor()>=0){
				curbs=5;
			}
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygrid-1;j++){
					double dist=Math.abs(j-dest2.getyCor());
					rdd1.set(dist,i,j);
					}
			}

			//Add PAVEMENT  field distance field
			GridValueLayer pavDF=new GridValueLayer("Pavementfield",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
			context.addValueLayer(pavDF);
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygrid;j++){
					double dist=Math.abs(i-dest2.getxCor());
					pavDF.set(dist,i,j);
					}
			}
			for(int i=0;i<=Values.xgrid;i++){
				pavDF.set(10000, i,0);
				pavDF.set(10000, i,Values.ygrid-1);
			}
			for(int i=0;i<=Values.ygrid;i++){
				pavDF.set(10000, 0,i);
				pavDF.set(10000, Values.xgrid-1,i);
			}
			
			GridValueLayer pavDF1=new GridValueLayer("Pavementfield1",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
			context.addValueLayer(pavDF1);
			int pivot1=0;
			for(int i=0;i<Values.xgrid-1;i++){
				for(int j=0;j<=Values.ygrid-1;j++){
					double dist=Math.abs(i-dest1.getxCor());
					pavDF1.set(dist,i,j);
					}
			}
			for(int i=0;i<=Values.xgrid;i++){
				pavDF1.set(10000, i,0);
				pavDF1.set(10000, i,Values.ygrid-1);
			}
			for(int i=0;i<=Values.ygrid;i++){
				pavDF1.set(10000, 0,i);
				pavDF1.set(10000, Values.xgrid-1,i);
			}
			//Add car distance field
					GridValueLayer gvlCar=new GridValueLayer("CarDestinationFF",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
					context.addValueLayer(gvlCar);
					for(int i=0;i<=Values.xgrid-1;i++){
						for(int j=0;j<=Values.ygrid-1;j++){
							double dist=Math.abs(i-Vdest.getxCor())+Math.abs(j-Vdest.getyCor());//+100;
							gvlCar.set(dist,i,j);
							}
						}
			// add anticipation dynamic field
					GridValueLayer anticipationField=new GridValueLayer("AnticipationField",false,new WrapAroundBorders(),Values.xgrid,Values.ygrid);
					context.addValueLayer(anticipationField);
					for(int i=0;i<=249;i++){
						for(int j=0;j<Values.ygrid;j++){
							double val=0;//+100;
							anticipationField.set(val,i,j);
							}
						}
			
			
			//ENVIRONMENT
			//add curb
			StaticElement curb;
			for(int i=0;i<Values.xgrid;i++){
				curb=new StaticElement(new GridPoint(i,5),"curb",gvl.get(i,5));
				context.add(curb);
				grid.moveTo(curb,i,5);
			}
			
			for(int i=0;i<Values.xgrid;i++){
				curb=new StaticElement(new GridPoint(i,Values.ygrid-5),"curb",gvl.get(i,Values.ygrid-5));
				context.add(curb);
				grid.moveTo(curb,i,Values.ygrid-6);
			}
			
			//add walkable
			StaticElement walkable;
			for(int i=0;i<Values.xgrid;i++){
				for(int j=0;j<5;j++){
					walkable=new StaticElement(new GridPoint(i,j),"walkable",gvl.get(i,j));
					context.add(walkable);
					grid.moveTo(walkable,i,j);
				}
			}
			for(int i=0;i<Values.xgrid;i++){
				for(int j=Values.ygrid-5;j<Values.ygrid;j++){
					walkable=new StaticElement(new GridPoint(i,j),"walkable",gvl.get(i,j));
					context.add(walkable);
					grid.moveTo(walkable,i,j);
				}
			}
			
			//add roadWay
					StaticElement roadway;
					for(int i=0;i<Values.xgrid;i++){
						for(int j=6;j<14;j++){
							roadway=new StaticElement(new GridPoint(i,j),"roadway",rdd.get(i,j));
							context.add(roadway);
							grid.moveTo(roadway,i,j);
						}
						for(int j=14;j<22;j++){
							roadway=new StaticElement(new GridPoint(i,j),"roadway",rdd.get(i,j));
							context.add(roadway);
							grid.moveTo(roadway,i,j);
						}
					}
					
			
			
			
			//Pedestrian generator
					PedGenerator ag1=new PedGenerator("PedOf1",dest1,regulationType,filecountPed);
					context.add(ag1);
					PedGenerator ag2=new PedGenerator("PedOf2",dest2,regulationType,filecountPed);
					context.add(ag2);
					VehicleGenerator vg1=new VehicleGenerator("VehOf1",Vdest,numberOfLane,filecount);
					context.add(vg1);
					VehicleGenerator vg2=new VehicleGenerator("VehOf2",Vdest1,numberOfLane,filecount);
					context.add(vg2);
						
		}
		
		if (RunEnvironment.getInstance().isBatch()){
			double endAt = (Double)params.getValue("runlength");
			RunEnvironment.getInstance().endAt(endAt);
		}
		double endAt = (Double)params.getValue("runlengthnormal");
		RunEnvironment.getInstance().endAt(endAt);
		
		return context;
	}
	////end context build.
	
	
	//grid generator
		public Grid<Object> gridGenerator(String id, int lenght, int width,Context<Object> context ){
			GridFactory gridF=GridFactoryFinder.createGridFactory(null);
			SimpleGridAdder<Object> sga=new SimpleGridAdder<Object>();
			GridBuilderParameters<Object> gbp=new GridBuilderParameters<Object>(new StrictBorders(),sga,true,lenght,width);
			//GridBuilderParameters.singleOccupancy2D(adder, borderRule, xSize, ySize)
			Grid<Object> grid=gridF.createGrid(id, context, gbp);
			return grid;
		}
		
		
}
