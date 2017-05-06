package inf101.simulator.objects.examples;

import inf101.simulator.Habitat;
import inf101.simulator.Position;

public class hungerStatus {
	public static int hungerStatus(double energy){
		if(energy <= 1000 && energy >= 800){
			return 1;
		}
		if(energy < 800 && energy>=400 ){
			return 0;
		}
		if(energy < 400 && energy >= 0){
			return -1;
		}
		return 10;
	}

}
