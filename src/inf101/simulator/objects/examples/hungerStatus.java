package inf101.simulator.objects.examples;

import inf101.simulator.Habitat;
import inf101.simulator.Position;

public class hungerStatus {
	/** Returns int value based on how how hungry an object is.
	 * @param energy
	 * 			The amount of energy an object has
	 * 
	 */
	public static int hungerStatus(double energy){
		if(energy <= 1000 && energy >= 700){
			return 1;
		}
		if(energy < 700 && energy>=300 ){
			return 0;
		}
		if(energy < 300 && energy >= 0){
			return -1;
		}
		return 10;
	}

}
