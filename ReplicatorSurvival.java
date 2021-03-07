package EcoSim;
import java.util.*;

/* Notes:
 * 
 *	Coords are in the form < Int + "," + Int > 
 * 		- x increases L2R, y increases T2B
 * 	All AI classes' positional maps require a LinkedHashMap to maintain order of each individual replicator statistics/pos
 * 
 * 
 * 
 * 
 */

public class ReplicatorSurvival {
	
	private int yCanvas;
	private int xCanvas;
	private int trees;
	private char treeSym = 'T';
	private int stones;
	private char stoneSym = 'x';
	private int food;
	private char foodSym = '*';
	private int water;
	private char waterSym = '^';
	private int replicators;
	private char repSym = '@';
	
	private Map<String,Character> pointMap = new HashMap<String,Character>();
	//private char[] screenPlane = new char[pointPlane.size()];
	
	private Map<String,Character> treeMap = new HashMap<String,Character>(); // make second part of HashMap integer from 0-1 for ArrayList
	private Map<String,Character> stoneMap = new HashMap<String,Character>();
	
	private Map<String,Character> foodMap = new HashMap<String,Character>();
	private Map<String,Character> waterMap = new HashMap<String,Character>();

	
	private Map<String,Character> replicatorPos = new LinkedHashMap<String,Character>();
	private Map<String,Character> predatorPos = new LinkedHashMap<String,Character>();
	
	
	
		// Constructor
	public ReplicatorSurvival(int xCanvas, int yCanvas, int trees, int stones, int food, int water, int replicators) 
	{
		this.yCanvas = yCanvas;
		this.xCanvas = xCanvas;
		this.trees = trees;
		this.stones = stones;
		this.food = food;
		this.water = water;
		this.replicators = replicators;
	}
	
	
	
		// Trees Generation
	public void makeTreePoints() 
	{
		Random randY = new Random();
		Random randX = new Random();
		for(int i=0; i<trees; i++)
		{
			int yC = randY.nextInt(yCanvas-2)+1; // keeps random coords within canvas edge boundaries
			int xC = randX.nextInt(xCanvas-2)+1;
			treeMap.put(Integer.toString(xC) + "," + Integer.toString(yC),treeSym);
			System.out.println("Planting trees..." + Integer.toString(xC) + "," + Integer.toString(yC));
		}
	}
	
		// Stones Generation
	public void makeStonePoints() 
	{
		Random randY = new Random();
		Random randX = new Random();
		for(int i=0; i<stones; i++)
		{
			int yC = randY.nextInt(yCanvas-2)+1;
			int xC = randX.nextInt(xCanvas-2)+1;
			stoneMap.put(Integer.toString(xC) + "," + Integer.toString(yC),stoneSym);
			System.out.println("Setting stones..." + Integer.toString(xC) + "," + Integer.toString(yC));
		}
		
	}
	
	
		// Food Generation
	public void makeFoodPoints() 
	{
		Random randY = new Random();
		Random randX = new Random();
		for(int i=0; i<food; i++)
		{
			int yC = randY.nextInt(yCanvas-2)+1; // to keep Spawns within boundaries, limit .nextInt by <canvas - (2*limit)> + 1*limit
			int xC = randX.nextInt(xCanvas-2)+1;
			foodMap.put(Integer.toString(xC) + "," + Integer.toString(yC),foodSym);
			System.out.println("Cooking food..." + Integer.toString(xC) + "," + Integer.toString(yC));
		}
		
	}
	
	
		// Water generation
	public void makeWaterPools()
	{
		int poolSize = 6;
		Random randY = new Random();
		Random randX = new Random();
		for(int i=0; i<water; i++)
		{
			// core water points
			int CoreyC = randY.nextInt(yCanvas-(2*poolSize))+poolSize;
			int CorexC = randX.nextInt(xCanvas-(2*poolSize))+poolSize;
			waterMap.put(Integer.toString(CorexC) + "," + Integer.toString(CoreyC),waterSym);
			System.out.println("Digging lakes..." + Integer.toString(CorexC) + "," + Integer.toString(CoreyC));
			
			// surrounding water points
			for(int y=0; y<(Math.pow(poolSize, 3)); y++) // pool density
			{
				int yC = randY.nextInt(CoreyC - (CoreyC - (2 * poolSize) ) ) + (CoreyC - poolSize);
				int xC = randX.nextInt(CorexC - (CorexC - (2 * poolSize) ) ) + (CorexC - poolSize);
				if(
						waterMap.containsKey(Integer.toString(xC-1)+","+Integer.toString(yC)) ||
						waterMap.containsKey(Integer.toString(xC+1)+","+Integer.toString(yC)) ||
						waterMap.containsKey(Integer.toString(xC)+","+Integer.toString(yC+1)) ||
						waterMap.containsKey(Integer.toString(xC)+","+Integer.toString(yC-1))
						)
				{
					waterMap.put(Integer.toString(xC) + "," + Integer.toString(yC),waterSym);
					System.out.println("   Pouring lakes..." + Integer.toString(xC) + "," + Integer.toString(yC));
				}
			}
		}
	}
		
	
		// Replicator generation
	public void spawnReplicators() 
	{
		Random randY = new Random();
		Random randX = new Random();
		for(int i=0; i<replicators; i++)
		{
			int yC = randY.nextInt(yCanvas-2)+1; // to keep Spawns within boundaries, limit .nextInt by <canvas - (2*limit)> + 1*limit
			int xC = randX.nextInt(xCanvas-2)+1;
			replicatorPos.put(Integer.toString(xC) + "," + Integer.toString(yC),repSym);
			System.out.println("Replicating Replicators..." + Integer.toString(xC) + "," + Integer.toString(yC));
		}
		
	}
	
	
		// Getter Methods
	public int getYCanvas() {
		return yCanvas;
	}
	
	public int getXCanvas() {
		return xCanvas;
	}
	
	public Map<String,Character> getTreePoints() 
	{
		return treeMap;
	}

	public Map<String,Character> getStonePoints() 
	{
		return stoneMap;
	}
	
	public Map<String,Character> getFoodPoints() 
	{
		return foodMap;
	}
	
	public Map<String,Character> getReplicatorsPos() 
	{
		return replicatorPos;
	}
	
	public Map<String,Character> getWaterPoints() {
		return waterMap;
	}
	
	public int getReplicatorsNum() 
	{
		return getReplicatorsPos().size();
	}
	
	public char getReplicatorsSym()
	{
		return repSym;
	}
	
	
	
	
	
	
	
	
// Point Plane Generation
	
	//Base background
	public void makePointPlane() 
	{
		
		// main init of pointPlane
		for(int y=0; y<=yCanvas; y++)
		{
			for(int x=0; x<=xCanvas; x++)
			{
				if(y==0 || y==yCanvas-1)
					pointMap.put(Integer.toString(x) + "," +  Integer.toString(y),'-');
				else if(x==0 || x==xCanvas-1)
					pointMap.put(Integer.toString(x) + "," +  Integer.toString(y),'|');
				else
				pointMap.put(Integer.toString(x) + "," +  Integer.toString(y),' ');
			}
		}
		
		// subsequent adding of other data points into pointPlane
		// !!! Priority of replacement is highest at bottom of for loops !!!
		
			// Trees
		for(String coord : treeMap.keySet())
		{
			pointMap.put(coord, treeMap.get(coord));
		}
		
			// Stones
		for(String coord : stoneMap.keySet())
		{
			pointMap.put(coord, stoneMap.get(coord));
		}
		
			// Food
		for(String coord : foodMap.keySet())
		{
			pointMap.put(coord, foodMap.get(coord));
		}
		
			// Water
		for(String coord : waterMap.keySet())
		{
			pointMap.put(coord, waterMap.get(coord));
		}
		
			// Replicators
		for(String coord : replicatorPos.keySet())
		{
			pointMap.put(coord, replicatorPos.get(coord));
		}
	}
	
	

	public Map<String,Character> getPointPlane() {
		return pointMap;
	}
		
	// Point Plane Printing		
		// Problem: Create a method to get the pointPlane values
		// and put into an array according to their coordinate position. 
		// Print that array to create a visual depiction of the world.
	
		public void printPointPlane() {
			for(int y=0; y<yCanvas; y++)
			{
				for(int x=0; x<xCanvas; x++)
				{
					String coord = Integer.toString(x) + "," + Integer.toString(y);
					System.out.print(pointMap.get(coord));
					System.out.print(" ");
				}
				System.out.print("\n");
				
			}
		}
	
	
	
	
	
}