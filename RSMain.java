package EcoSim;
import java.io.*;
	
public class RSMain {
	public static void main(String args[]) throws IOException {
		
		
		BufferedReader readIn = new BufferedReader(new InputStreamReader(System.in));   
		
													// xCanvas, yCanvas, trees, stones, food, water, Replicators
		ReplicatorSurvival rs = new ReplicatorSurvival(30, 		30, 	 10, 	0, 	10,   3,	 3);
	
				
		
	// main() run
		// generate coords
		rs.makeTreePoints();
		rs.makeStonePoints();
		rs.makeFoodPoints();
		rs.makeWaterPools();
		rs.spawnReplicators();
		// output points
		System.out.println("World Generation Done!\n");
		rs.makePointPlane();
		rs.printPointPlane();
	// AI Inits
		ReplicatorAI rAI = new ReplicatorAI(rs);
	
		
	//  test run
		System.out.println(rs.getWaterPoints().keySet());
		for(int i=0; i<rs.getReplicatorsNum(); i++) {
			System.out.println(rAI.getReplicatorStat(i) +"\n");
		}
		
		System.out.println("Reps: " + rs.getReplicatorsNum());
		
		
	// main cycle
		int day = 1;
		boolean loop = true;
		while(loop)
		{
			String cont = readIn.readLine();
			if(!cont.equals("exit")) {
				
				rAI.decideAction();
				rAI.hunger();
				rAI.thirst();
				rAI.deathByEnvironment();
				
				rs.makePointPlane();
				rs.printPointPlane();
				
				System.out.println("Day " + day);
				System.out.println("Reps: " + rs.getReplicatorsNum());
				day++;
			}
			else loop=!loop;
		}
	}
}
