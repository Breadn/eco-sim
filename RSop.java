package EcoSim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RSop {
	
	public static int getX(String coord) {
		return Integer.parseInt(coord.substring(0,coord.indexOf(",")));
	}
	
	public static int getY(String coord) {
		return Integer.parseInt(coord.substring(coord.indexOf(",")+1,coord.length()));
	}
	
	// Returns distance in form of coord string between two coords' x & y values
	public static String getDistCoord(String selfCoord, String targetCoord) {
		return Integer.toString( getX(targetCoord) - getX(selfCoord) ) + "," + Integer.toString( getY(targetCoord) - getY(selfCoord) );
	}
	
	// Return distance in form of double value
	public static double getDistVal(String selfCoord, String targetCoord) {
		int xDist = getX(getDistCoord(selfCoord,targetCoord));
		int yDist = getY(getDistCoord(selfCoord,targetCoord));
		return Math.pow( (Math.pow(xDist,2)+Math.pow(yDist,2)), 0.5);
	}
	
	// Determine if this coord is next to that coord
	public static boolean isNextTo(String thisCoord, String thatCoord) {
		for(int x=-1; x<2; x++)
			for(int y=-1; y<2; y++)
				if(thisCoord.equals((getX(thatCoord)+x) + "," + (getY(thatCoord)+y)))
						return true;
		return false;
	}
	
}
