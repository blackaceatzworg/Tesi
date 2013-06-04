package new1;

import java.util.ArrayList;
import java.util.Random;

import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;

public class PedTicker {
	ArrayList<DestinationCell> destinationChoices;  
	ArrayList<DestinationCell> destinationConflicts;
	
	
	public PedTicker(){
		this.destinationChoices=new ArrayList<DestinationCell>();
		this.destinationConflicts=new ArrayList<DestinationCell>();
	}
	
	@ScheduledMethod(start=0, interval=5, priority=0)
	public void pedTurn(){
		this.solvePedConflict();
		this.setAnticipationPeds();
//		this.evalPeds();
		this.movePedestrians();
	}
	
	public ArrayList<Pedestrian> getPedList(){
		@SuppressWarnings("unchecked")
		final Iterable<Pedestrian> peds=RunState.getInstance().getMasterContext().getObjects(Pedestrian.class);
		final ArrayList<Pedestrian> pedList=new ArrayList<Pedestrian>();
		for(final Pedestrian ped:peds){
			pedList.add(ped);
		}
		return pedList;
	}
	
	/**
	 * 
	 * Each Pedestrian choose his destination, which is added to general destination list. Then, conflict beetween pedestrian are solved.
	 * */
	public void solvePedConflict(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			//ped.chooseDestination2();
			ped.setMotionstate(true);
			destinationChoices.add(ped.chooseDestination());
		}
		this.checkDestinationConflicts(destinationChoices);
	}
	
	
	/**
	 * Solve conflict among pedestrians.
	 * */
	public void checkDestinationConflicts(ArrayList<DestinationCell> dcList){
		final ArrayList<Pedestrian> pedList=getPedList();
		Random r=new Random();
		boolean move=r.nextBoolean();
		for(int i=0;i<dcList.size();i++){
			for(int j=i+1;j<dcList.size();j++){
				DestinationCell dc=dcList.get(i);
				DestinationCell dc2=dcList.get(j);
				int dcx= dc.getX();
				int dcy= dc.getY();
				String dcId=dc.getPedId();
				int dc2x= dc2.getX();
				int dc2y= dc2.getY();
				String dc2Id=dc2.getPedId();
				if(dcx==dc2x&&dcy==dc2y&&!dcId.equals(dc2Id)){
					if(move){
						this.destinationConflicts.add(dc2);
						
						for(final Pedestrian ped:pedList){
							if(ped.getId().equals(dc2Id)){
								ped.setMotionstate(false);
							}
						}
					}else{
						this.destinationConflicts.add(dc);
						for(final Pedestrian ped:pedList){
							if(ped.getId().equals(dcId)){
								ped.setMotionstate(false);
							}
						}
					}
				}
			}
		}
	}
	
	public void setAnticipationPeds(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			ped.project();
//			ped.getAnticipation().updatePedAnticipation(, y, ped.getHeading(), ped.getAnticipation().getAnticipationCells());
		}
	}
	public void evalPeds(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			ped.evaluate();
		}
	}
	public void movePedestrians(){
		final ArrayList<Pedestrian> pedList=getPedList();
		for(final Pedestrian ped:pedList){
			if(ped.isMotionstate()){
			ped.movement();
			}else{
				System.out.println(ped.getId()+" in conflict, stopped");
			}
		}
		this.destinationChoices.removeAll(destinationChoices);
		this.destinationConflicts.removeAll(destinationConflicts);
	}
	
	

}
