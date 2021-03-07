package EcoSim;

import java.util.*;

public abstract class AI {
	/*
	 * Notes
	 * 
	 * HashMaps
	 * - pointPlane is map containing literal locations of all objects
	 * - posMap is map containing positions of all AIs
	 * - newPos is dynamically updated map containing positions of all AI (prevents AI self-annihilation)
	 * 
	 */

	private Random rand = new Random();
	
	

	
	

	public void consume(List<Integer> TYPE_BAR, int AIindex, int val) {
		TYPE_BAR.set(AIindex, TYPE_BAR.get(AIindex)+val);
	}
	
	// Individual AI method: Hunger specified AI
	public void diminish(List<Integer> TYPE_BAR, List<Integer> TYPE_RATE, int AIindex) {
		TYPE_BAR.set(AIindex, TYPE_BAR.get(AIindex)-TYPE_RATE.get(AIindex));
	}
	
	// Individual AI method: Returns coord of closest food
	public String findFood(Map<String,Character> pointPlane, Map<String,Character> posMap, Map<String,Character> newPos, Map<String,Character> foodMap, List<Integer> SGHT_RADIUS, String AIcoord) {
			int AIindex = new ArrayList<String>(posMap.keySet()).indexOf(AIcoord);
			double minDist = 999999999; // better way for maximum upper limit?
			String closestFoodCoord = ",";
			
			// Identifies closestFoodCoord in SGHT_RADIUS
			for(String foodCoord : foodMap.keySet()) {
				double dist = RSop.getDistVal(AIcoord, foodCoord);
				
				if(dist<SGHT_RADIUS.get(AIindex) && dist<minDist) {
					minDist = dist;
					closestFoodCoord = foodCoord;
				}
			}
			
			if(!closestFoodCoord.equals(",")) {
				List<String> path = findClrPathTo(pointPlane, posMap, newPos, AIcoord, closestFoodCoord); // determine if foodCoord is "visible"
				if(path.size()>0 && RSop.isNextTo(path.get(path.size()-1), closestFoodCoord))
					return closestFoodCoord;
			}
			return ",";
		}
	

	public String findWater(Map<String,Character> pointPlane, Map<String,Character> posMap, Map<String,Character> newPos, Map<String,Character> waterMap, List<Integer> SGHT_RADIUS, String AIcoord) {
		int AIindex = new ArrayList<String>(posMap.keySet()).indexOf(AIcoord);
		double minDist = 999999999;
		String closestWaterCoord = ",";
		
		for(String waterCoord : waterMap.keySet()) {
			
			double dist = RSop.getDistVal(AIcoord, waterCoord);
			if(dist<SGHT_RADIUS.get(AIindex) && dist<minDist) {
				minDist = dist;
				closestWaterCoord = waterCoord;
			}
		}
	
		if(!closestWaterCoord.equals(",")) {
			List<String> path = findClrPathTo(pointPlane, posMap, newPos, AIcoord, closestWaterCoord);
			if(path.size()>0 && RSop.isNextTo(path.get(path.size()-1), closestWaterCoord))
				return closestWaterCoord;
		}
		return ",";
	}
	
	
	// When food & hydration levels are above a survivalability point, find mate
	public String findMate(Map<String,Character> pointPlane, Map<String,Character> posMap, Map<String,Character> newPos, List<Integer> SGHT_RADIUS, String selfCoord) {
		int AIindex = new ArrayList<String>(posMap.keySet()).indexOf(selfCoord);
		double minDist = 999999999;
		String closestOtherCoord = ",";
		
		for(String otherCoord : posMap.keySet()) {
			if(!otherCoord.equals(selfCoord)) {
				double dist = RSop.getDistVal(selfCoord, otherCoord);
				if(dist<SGHT_RADIUS.get(AIindex) && dist<minDist) {
					minDist = dist;
					closestOtherCoord = otherCoord;
				}
			}
		}
	
		if(!closestOtherCoord.equals(",")) {
			List<String> path = findClrPathTo(pointPlane, posMap, newPos, selfCoord, closestOtherCoord);
			if(path.size()>0 && RSop.isNextTo(path.get(path.size()-1), closestOtherCoord))
				return closestOtherCoord;
		}
		return ",";
	}
	
	// Generate a new AI at specified coord (with certain genetics?)
	public void reproduce (
			String coord, char AIsym, Map<String,Character> newPos, List<Integer> MVMT_SPEED, List<Integer> SGHT_RADIUS,
			List<Integer> HNGR_SETPOINT, List<Integer> HNGR_BAR, List<Integer> HNGR_RATE, List<Integer> HNGR_RESILIENCE,
			List<Integer> THST_SETPOINT, List<Integer> THST_BAR, List<Integer> THST_RATE, List<Integer> THST_RESILIENCE
			){
		newPos.put(coord, AIsym); MVMT_SPEED.add(1); SGHT_RADIUS.add(5);
		HNGR_SETPOINT.add(50); HNGR_BAR.add(50); HNGR_RATE.add(1); HNGR_RESILIENCE.add(30);
		THST_SETPOINT.add(50); THST_BAR.add(50); THST_RATE.add(1); THST_RESILIENCE.add(30);
	}
	
	// Individual AI method: Return random coord in radius of MVMT_SPEED
	public String wander(int xCanvas, int yCanvas, Map<String,Character> pointPlane, Map<String,Character> posMap, Map<String,Character> newPos, List<Integer> MVMT_SPEED, String AIcoord) {
		int AIindex = new ArrayList<String>(posMap.keySet()).indexOf(AIcoord);
		int xCoord = RSop.getX(AIcoord);
		int yCoord = RSop.getY(AIcoord);
		String randCoord = AIcoord;
		while(pointPlane.get(randCoord)!=' ' || newPos.containsKey(randCoord)) { // collision detection
			xCoord = RSop.getX(AIcoord) + rand.nextInt((MVMT_SPEED.get(AIindex)*2)+1) - MVMT_SPEED.get(AIindex);
			yCoord = RSop.getY(AIcoord) + rand.nextInt((MVMT_SPEED.get(AIindex)*2)+1) - MVMT_SPEED.get(AIindex);
			while((xCoord<1 || xCoord>xCanvas-1) || (yCoord<1 || yCoord>yCanvas-1)) {
				xCoord = RSop.getX(AIcoord) + rand.nextInt((MVMT_SPEED.get(AIindex)*2)+1) - MVMT_SPEED.get(AIindex);
				yCoord = RSop.getY(AIcoord) + rand.nextInt((MVMT_SPEED.get(AIindex)*2)+1) - MVMT_SPEED.get(AIindex);
			}
			randCoord = Integer.toString(xCoord) + "," + Integer.toString(yCoord);
		}
		return randCoord;
	}
	
	// Pathfinding: Find a direct, unobstructed path to target coord. If not, terminate path to position before obstruction.
	public static List<String> findClrPathTo(Map<String,Character> pointPlane, Map<String,Character> posMap, Map<String,Character> newPos, String AIcoord, String targetCoord) {
		List<String> path = new ArrayList<String>();
		String bestCoord = AIcoord;
		
		path.add(AIcoord);
		
		for(int x=-1; x<2; x++) {
			for(int y=-1; y<2; y++) {
				String testCoord = Integer.toString(RSop.getX(AIcoord)+x) + "," + Integer.toString(RSop.getY(AIcoord)+y);
				if(RSop.getDistVal(testCoord, targetCoord)-RSop.getDistVal(bestCoord, targetCoord)<0) {
					bestCoord = testCoord;
				}
			}		
		}	
		
		if((pointPlane.get(bestCoord)==' ' || pointPlane.get(bestCoord)==pointPlane.get(targetCoord)) && !newPos.containsKey(bestCoord) && !path.contains(targetCoord))
			path.addAll(findClrPathTo(pointPlane, posMap, newPos, bestCoord, targetCoord));
		
		else if(path.contains(targetCoord) && pointPlane.get(targetCoord)!=' ') // reached end
			path.remove(path.size()-1);
		
		return path;
	}
	
	
	// Pathfinding: (WIP) Find any clear path to target
	/* TODO
	 *  - Fix self-annihilation
	 *  - Fix recursion
	 */
	public static List<String> findPathTo(Map<String,Character> pointPlane, Map<String,Character> posMap, String AIcoord, String targetCoord) {
		List<String> path = new ArrayList<String>();
		List<String> pCoords = new ArrayList<String>();
		boolean nextTo = false;
		
		path.add(AIcoord);
		
		for(int x=-1; x<2; x++) {
			for(int y=-1; y<2; y++) {
				String testCoord = Integer.toString(RSop.getX(targetCoord)+x) + "," + Integer.toString(RSop.getY(targetCoord)+y);
				if(path.contains(testCoord)) nextTo = true;
			}		
		}	
		
		if((!path.contains(targetCoord) && pointPlane.get(targetCoord)==' ') || (!nextTo)) {
			for(int x=-1; x<2; x++) {
				for(int y=-1; y<2; y++) {
					String testCoord = Integer.toString(RSop.getX(AIcoord)+x) + "," + Integer.toString(RSop.getY(AIcoord)+y);
					if(pointPlane.get(testCoord)==' ') {
						pCoords.add(testCoord);
					}
				}		
			}
			
			// && RSop.getDistVal(testCoord, targetCoord)-RSop.getDistVal(bestCoord, targetCoord)<0
			String bestCoord = pCoords.get(0);
			for(String coord : pCoords) {
				if(RSop.getDistVal(coord, targetCoord)-RSop.getDistVal(bestCoord, targetCoord)<0) {
					bestCoord = coord;
				}
			}
			path.addAll(findClrPathTo(pointPlane, posMap, posMap, bestCoord, targetCoord));
		}
		return path;
	}
	
	
	
	// Death checker by environment (hunger / thirst)
	public void deathByEnvironment(char repSym, Map<String,Character> posMap, List<Integer> HNGR_BAR, List<Integer> THST_BAR) {
		List<String> survivingAIs = new ArrayList<String>();
		int i=0;
		for(String AICoord : posMap.keySet()) {
			if(HNGR_BAR.get(i) > 0 && THST_BAR.get(i) > 0) survivingAIs.add(AICoord);
			i++;
		}
		posMap.clear();
		for(String AICoord : survivingAIs) {
			posMap.put(AICoord, repSym);
		}
	}
	
	
	
	
	
	
	
}



/*
	public void moveRandAll(Map<String,Character> pointPlane, Map<String,Character> posMap, List<Integer> MVMT_SPEED) {
		Map<String, Character> newPos = new LinkedHashMap<String, Character>();
		
		int AIIndex=0;
		for(String AIcoord : posMap.keySet()) {
			String newAICoord = AIcoord;
			int xDir = rand.nextInt(3);
			int yDir = rand.nextInt(3);
			int xCoord = RSop.getX(AIcoord);
			int yCoord = RSop.getY(AIcoord);
			String oldAICoord;
			
			int j=0;
			while(j<MVMT_SPEED.get(AIIndex)) {
				oldAICoord = newAICoord;
				if(xDir == 0) {
					xCoord -= 1;
				}
				else if(xDir == 2) {
					xCoord += 1;
				}
				
				if(yDir == 0) {
					yCoord -= 1;
				}
				else if(yDir == 2) {
					yCoord += 1;
				}
				newAICoord = Integer.toString(xCoord) + "," + Integer.toString(yCoord);
				j++;

				if(pointPlane.get(newAICoord)!=' ' || newPos.containsKey(newAICoord)) { // collision detection conditions
					newAICoord = oldAICoord;
					break;
				}
			}
			newPos.put(newAICoord, posMap.get(AIcoord));
			AIIndex++;
		}
		posMap.clear();
		posMap.putAll(newPos);
	}
	
	
	
	
	
	public void moveRandAll(Map<String,Character> pointPlane, Map<String,Character> posMap, List<Integer> MVMT_SPEED) {
		Map<String, Character> newPos = new LinkedHashMap<String, Character>();
		
		for(String AIcoord : posMap.keySet()) {
			String newCoord = RSop.getMoveableCoord(pointPlane, posMap, MVMT_SPEED, AIcoord, rand.nextInt(3), rand.nextInt(3));
			newPos.put(newCoord, posMap.get(AIcoord));
		}
		posMap.clear();
		posMap.putAll(newPos);
	}
	
	
	
	

	public static String getMoveableCoord(Map<String,Character> pointPlane, Map<String,Character> posMap, List<Integer> MVMT_SPEED, String AIcoord, int xDir, int yDir) {
		String newAIcoord = AIcoord;
		String oldAIcoord;
		int AIindex = new ArrayList<String>(posMap.keySet()).indexOf(AIcoord);
		int xCoord = RSop.getX(AIcoord);
		int yCoord = RSop.getY(AIcoord);
		
		int j=0;
		while(j<MVMT_SPEED.get(AIindex)) {
			oldAIcoord = newAIcoord;
			if(xDir == 0) {
				xCoord -= 1;
			}
			else if(xDir == 2) {
				xCoord += 1;
			}
			
			if(yDir == 0) {
				yCoord -= 1;
			}
			else if(yDir == 2) {
				yCoord += 1;
			}
			newAIcoord = Integer.toString(xCoord) + "," + Integer.toString(yCoord);
		
			if(pointPlane.get(newAIcoord)!=' ' || posMap.containsKey(newAIcoord)) { // collision detection conditions
				newAIcoord = oldAIcoord;
				break;
			}
			j++;
		}
		return newAIcoord;
	}
*/