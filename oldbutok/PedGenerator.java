package CrossingSiteModel;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class PedGenerator {

	
	private String id;
	private Destination destination;
	private int crossingType;
	private String filename;
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	private int counter;
	
	static Parameters params=RunEnvironment.getInstance().getParameters();
	//int numberOfPedestrian=(Integer)params.getValue("nPed");
	double pPed=(Double)params.getValue("pPed");
	
	public PedGenerator(String ID, Destination d, int CrossingType, String filename) {
		this.setId(ID);
		this.setDestination(d);
		this.setCrossingType(CrossingType);
		this.setFilename(filename);
	}
	
	@ScheduledMethod(start=0,interval=1)
		public void generatePedestrian(){
			Random r=new Random();
			double pPedRandom=r.nextDouble();
			//if(DataCollector.getCurrentPedestrianNumber()<numberOfPedestrian){
				if(pPedRandom<pPed){
					System.out.print(pPed+" "+pPedRandom);
				if(this.getCrossingType()==Values.RegulatedRoad){
				addPedestrian();}
				else{
					addPedestrian(300);
				}
				}
			//}
		}
	public void addPedestrian(){
		Destination d=this.getDestination();
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		Random rand=new Random();
		int x=rand.nextInt(10)+d.getxCor();
		int y=0;
		if(d.getyCor()>=env.getDimensions().getHeight()-3){
			y=3;
		}else{
			y=env.getDimensions().getHeight()-3;
		}
		String idp=this.getId();
		final Pedestrian p=new Pedestrian(idp+this.getCounter(),d,this.getFilename());
		this.setCounter(this.getCounter()+1);
		p.setCurrentPosition(new GridPoint(x,y));
		p.setCrossingType(this.getCrossingType());
		context.add(p);
		env.moveTo(p,x,y);
		DataCollector.setCurrentPedestrianNumber(DataCollector.getCurrentPedestrianNumber()+1);
	}
	
	public void addPedestrian(int xgrd){
		Destination d=this.getDestination();
		Context<Object> context =ContextUtils.getContext(this);
		Grid<Object> env=(Grid<Object>)context.getProjection("grid");
		Random rand=new Random();
		int x=rand.nextInt(xgrd)+10;
		int y=0;
		if(d.getyCor()>=env.getDimensions().getHeight()-3){
			y=3;
		}else{
			y=env.getDimensions().getHeight()-3;
		}
		String idp=this.getId();
		
		final Pedestrian p=new Pedestrian(idp+this.getCounter(),d,this.getFilename());
		this.setCounter(this.getCounter()+1);
		p.setCurrentPosition(new GridPoint(x,y));
		p.setCrossingType(this.getCrossingType());
		context.add(p);
		env.moveTo(p,x,y);
		DataCollector.setCurrentPedestrianNumber(DataCollector.getCurrentPedestrianNumber()+1);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public int getCrossingType() {
		return crossingType;
	}

	public void setCrossingType(int crossingType) {
		this.crossingType = crossingType;
	}
	

}
