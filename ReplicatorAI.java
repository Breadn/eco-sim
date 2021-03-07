package EcoSim;
import java.util.*;

/* Notes:
 * MOVEMENT:
 * 	- Random ints (0-2) determine cardinal direction of movement with 0 = N||W, 1 = static, = 2 = S||E 
 * 	- Constant int MVMT_SPEED determine magnitude of movement
 * 
 * HUNGER:
 *  - Resilience is min value HNGR_BAR can be before AI searches for food, feels hungry
 * 
 */

public class ReplicatorAI extends AI
{
	
	private ReplicatorSurvival rs;
	// Lists that hold each individual replicator's stats
	private List<Integer> MVMT_SPEED = new ArrayList<Integer>();
	private List<Integer> SGHT_RADIUS = new ArrayList<Integer>(); 
	
	private List<Integer> HNGR_SETPOINT = new ArrayList<Integer>();
	private List<Integer> HNGR_BAR = new ArrayList<Integer>();
	private List<Integer> HNGR_RATE = new ArrayList<Integer>();
	private List<Integer> HNGR_RESILIENCE = new ArrayList<Integer>();
	
	private List<Integer> THST_SETPOINT = new ArrayList<Integer>();
	private List<Integer> THST_BAR = new ArrayList<Integer>();
	private List<Integer> THST_RATE = new ArrayList<Integer>();
	private List<Integer> THST_RESILIENCE = new ArrayList<Integer>();

	
	public ReplicatorAI(ReplicatorSurvival rs) {
		this.rs = rs;
		
		initSpeed();
		initSight();
		initHungerSetPoint();
		initHungerBar();
		initHungerRate();
		initHungerResilience();
		initThirstSetPoint();
		initThirstBar();
		initThirstRate();
		initThirstResilience();
	}
	
	public String getReplicatorStat(int repIndex) {
		return "Rep #"+repIndex+" Stats:"
				+ "\n MVMT_SPEED: " + MVMT_SPEED.get(repIndex)
				+ "\n SGHT_RADIUS: " + SGHT_RADIUS.get(repIndex)
				+ "\n HNGR_BAR: " + HNGR_BAR.get(repIndex)
				+ "\n HNGR_RATE: " + HNGR_RATE.get(repIndex)
				+ "\n HNGR_RESILIENCE: " + HNGR_RESILIENCE.get(repIndex)
				+ "\n THST_BAR: " + THST_BAR.get(repIndex)
				+ "\n THST_RATE: " + THST_RATE.get(repIndex)
				+ "\n THST_RESILIENCE: " + THST_RESILIENCE.get(repIndex);
	}
	
	
	private void initSpeed() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			MVMT_SPEED.add(1);
		}
	}
	
	private void initSight() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			SGHT_RADIUS.add(5);
		}
	}
	
	private void initHungerSetPoint() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			HNGR_SETPOINT.add(50);
		}
	}
	
	private void initHungerBar() {
		for(int i : HNGR_SETPOINT) {
			HNGR_BAR.add(i);
		}
	}
	
	private void initHungerRate() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			HNGR_RATE.add(1);
		}
	}
	
	private void initHungerResilience() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			HNGR_RESILIENCE.add(30);
		}
	}
	
	private void initThirstSetPoint() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			THST_SETPOINT.add(50);
		}
	}
	
	private void initThirstBar() {
		for(int i : THST_SETPOINT) {
			THST_BAR.add(i);
		}
	}
	
	private void initThirstRate() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			THST_RATE.add(1);
		}
	}
	
	private void initThirstResilience() {
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			THST_RESILIENCE.add(30);
		}
	}
	
	public void decideAction() {
		Map<String,Character> newPos = new LinkedHashMap<String,Character>();
		
		for(String AIcoord : rs.getReplicatorsPos().keySet()) {
			int AIindex = new ArrayList<String>(rs.getReplicatorsPos().keySet()).indexOf(AIcoord);
			System.out.println("\n");
			
			if(HNGR_BAR.get(AIindex) < HNGR_RESILIENCE.get(AIindex))
				System.out.println("Rep #"+AIindex+" is hungry!");
			
			if(THST_BAR.get(AIindex) < THST_RESILIENCE.get(AIindex))
				System.out.println("Rep #"+AIindex+" is thirsty!");
			
			//System.out.println("HNGR_BAR: " + HNGR_BAR.get(AIindex) + "/" + HNGR_SETPOINT.get(AIindex) + ":" + HNGR_RESILIENCE.get(AIindex));
			//System.out.println("THST_BAR: " + THST_BAR.get(AIindex) + "/" + THST_SETPOINT.get(AIindex) + ":" + THST_RESILIENCE.get(AIindex));
			String foodCoord = super.findFood(rs.getPointPlane(), rs.getReplicatorsPos(), newPos, rs.getFoodPoints(), SGHT_RADIUS, AIcoord);
			//System.out.println("foodCoord: " + foodCoord);
			String waterCoord = super.findWater(rs.getPointPlane(), rs.getReplicatorsPos(), newPos, rs.getWaterPoints(), SGHT_RADIUS, AIcoord);
			//System.out.println("waterCoord: " + waterCoord);
			String mateCoord = super.findMate(rs.getPointPlane(), rs.getReplicatorsPos(), newPos, SGHT_RADIUS, AIcoord);
			//System.out.println("mateCoord: " + mateCoord);
			// Priority decider
			// Chooses decision in face of multiple options
			/*
			 * 0 - hunger
			 * 1 - thirst
			 * 2 - reproduce
			 */
			
			int priority = -1;	
			if(!foodCoord.equals(",") && !waterCoord.equals(",")) {
				if(HNGR_BAR.get(AIindex) < HNGR_RESILIENCE.get(AIindex) && HNGR_BAR.get(AIindex) < THST_BAR.get(AIindex)) priority = 0;
				else if(THST_BAR.get(AIindex) < THST_RESILIENCE.get(AIindex)) priority = 1;
			}
			
			// Hungry
			if(priority == 0 || (HNGR_BAR.get(AIindex) < HNGR_RESILIENCE.get(AIindex) && rs.getFoodPoints().size()>0 && !foodCoord.equals(","))) { //HNGR_BAR.get(AIindex) < HNGR_RESILIENCE.get(AIindex) && rs.getFoodPoints().size()>0 && !foodCoord.equals(",")
				int cals = 10; // caloric intake per eat
				
				List<String> path = super.findClrPathTo(rs.getPointPlane(), rs.getReplicatorsPos(), newPos, AIcoord, foodCoord);
				if(MVMT_SPEED.get(AIindex) < path.size())
					newPos.put(path.get(MVMT_SPEED.get(AIindex)), rs.getReplicatorsSym());
				else
					newPos.put(path.get(path.size()-1), rs.getReplicatorsSym());
				
				if(RSop.isNextTo(AIcoord, foodCoord)) {
					while(HNGR_BAR.get(AIindex) < HNGR_SETPOINT.get(AIindex)) 
						consume(HNGR_BAR, AIindex, cals);
				}
			}
			
			// Thirsty
			else if(priority == 1 || (THST_BAR.get(AIindex) < THST_RESILIENCE.get(AIindex) && rs.getWaterPoints().size()>0 && !waterCoord.equals(","))) {
				int gals = 10; // gallons of water intake per drink
				
				List<String> path = super.findClrPathTo(rs.getPointPlane(), rs.getReplicatorsPos(), newPos, AIcoord, waterCoord);
				if(MVMT_SPEED.get(AIindex) < path.size())
					newPos.put(path.get(MVMT_SPEED.get(AIindex)), rs.getReplicatorsSym());
				else
					newPos.put(path.get(path.size()-1), rs.getReplicatorsSym());
				
				if(RSop.isNextTo(AIcoord, waterCoord)) {
					while(THST_BAR.get(AIindex) < THST_SETPOINT.get(AIindex)) 
						consume(THST_BAR, AIindex, gals);
				}
			}
			
			// Arr, Mate-y
			else if(HNGR_BAR.get(AIindex) > HNGR_RESILIENCE.get(AIindex) && THST_BAR.get(AIindex) > THST_RESILIENCE.get(AIindex) && !mateCoord.equals(",")) {
				List<String> path = super.findClrPathTo(rs.getPointPlane(), rs.getReplicatorsPos(), newPos, AIcoord, mateCoord);
				if(MVMT_SPEED.get(AIindex) < path.size())
					newPos.put(path.get(MVMT_SPEED.get(AIindex)), rs.getReplicatorsSym());
				else
					newPos.put(path.get(path.size()-1), rs.getReplicatorsSym());
				
				if(RSop.isNextTo(AIcoord, mateCoord)) {
					//sexytime: make method to generate new AI with not only coord to add in (newPos), but with data (like hunger, thirst, sight, movement, etc)
					String coord = "1,1"; //TODO: find clear coord around the mates
					super.reproduce(
							coord, rs.getReplicatorsSym(), newPos, MVMT_SPEED, SGHT_RADIUS, 
							HNGR_SETPOINT, HNGR_BAR, HNGR_RATE, HNGR_RESILIENCE, 
							THST_SETPOINT, THST_BAR, THST_RATE, THST_RESILIENCE
							);
				}
			}
			
			// Wander
			else {
					System.out.println("Rep #"+AIindex+" is wandering.");
				String randCoord = super.wander(rs.getXCanvas(), rs.getYCanvas(), rs.getPointPlane(), rs.getReplicatorsPos(), newPos, MVMT_SPEED, AIcoord);
				List<String> path = super.findClrPathTo(rs.getPointPlane(), rs.getReplicatorsPos(), newPos, AIcoord, randCoord);
				if(MVMT_SPEED.get(AIindex) < path.size())
					newPos.put(path.get(MVMT_SPEED.get(AIindex)), rs.getReplicatorsSym());
				else
					newPos.put(path.get(path.size()-1), rs.getReplicatorsSym());
			}
						
		}
		rs.getReplicatorsPos().clear();
		rs.getReplicatorsPos().putAll(newPos);
	}
	
	
	public void hunger() {
		for(String AIcoord : rs.getReplicatorsPos().keySet()) {
			int AIindex = new ArrayList<String>(rs.getReplicatorsPos().keySet()).indexOf(AIcoord);
			super.diminish(HNGR_BAR, HNGR_RATE, AIindex);
		}
	}
	
	public void thirst() {
		for(String AIcoord : rs.getReplicatorsPos().keySet()) {
			int AIindex = new ArrayList<String>(rs.getReplicatorsPos().keySet()).indexOf(AIcoord);
			super.diminish(THST_BAR, THST_RATE, AIindex);
		}
	}
	
	public void deathByEnvironment() {
		super.deathByEnvironment(rs.getReplicatorsSym(), rs.getReplicatorsPos(), HNGR_BAR, THST_BAR);
	}
	
	
}






/*
public void move()
{
	Map<String, Character> newRepPos = new HashMap<String, Character>();
	for(String coord : rs.getReplicatorPos().keySet())
	{
		String newCoord = "";
		while(newCoord != " ") {
			int xDir = rand.nextInt(3);
			int yDir = rand.nextInt(3);
			int xCoord = RSop.getX(coord);
			int yCoord = RSop.getY(coord);
			
			if(xDir == 0) {
				xCoord -= MVMT_SPEED;
			}
			else if(xDir == 2) {
				xCoord += MVMT_SPEED;
			}
			
			if(yDir == 0) {
				yCoord -= MVMT_SPEED;
			}
			else if(yDir == 2) {
				yCoord += MVMT_SPEED;
			}
			newCoord = Integer.toString(xCoord) + "," + Integer.toString(yCoord);
		}
		newRepPos.put(newCoord, rs.getReplicatorSym());
	}
	rs.getReplicatorPos().clear();
	rs.getReplicatorPos().putAll(newRepPos);
}
*/