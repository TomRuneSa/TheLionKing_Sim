package inf101.simulator;

import inf101.simulator.objects.examples.Blob;
import inf101.simulator.objects.examples.SimAnimal;
import inf101.simulator.objects.examples.SimBird;
import inf101.simulator.objects.examples.SimFeed;
import inf101.simulator.objects.examples.SimFemaleLion;
import inf101.simulator.objects.examples.SimLionCub;
import inf101.simulator.objects.examples.SimHyena;
import inf101.simulator.objects.examples.SimInsect;
import inf101.simulator.objects.examples.SimMaleLion;
import inf101.simulator.objects.examples.SimRepellant;
import inf101.simulator.objects.examples.SimRock;
import inf101.simulator.objects.examples.SimTree;
import inf101.simulator.objects.examples.SimWarthog;
import inf101.simulator.objects.examples.SimMareCat;
import inf101.simulator.objects.examples.SimMonkey;

public class Setup {
	/** This method is called when the simulation starts */
	public static void setup(SimMain main, Habitat habitat) {
		// habitat.addObject(new SimAnimal(new Position(400, 400), habitat));
		// habitat.addObject(new Blob(new Direction(0), new Position(400, 400),
		// 1));
		habitat.addObject(new SimInsect(new Position(400, 400), habitat, main.getRandom().nextDouble() * 2 + 0.5));
		habitat.addObject(new SimWarthog(main.randomPos(), habitat));
		habitat.addObject(new SimBird(main.randomPos(), habitat));
		habitat.addObject(new SimMareCat(main.randomPos(), habitat));
		habitat.addObject(new SimHyena(main.randomPos(), habitat, 2.3));
		habitat.addObject(new SimMaleLion(main.randomPos(), habitat));
		habitat.addObject(new SimFemaleLion(main.randomPos(), habitat));
		habitat.addObject(new SimTree(main.randomPos(), habitat));
		habitat.addObject(new SimMonkey(main.randomPos(), habitat));
		habitat.addObject(new SimRock(new Position(1649.8333333333335, 247.0), habitat));

		for (int i = 0; i < 1; i++) {

			SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimInsect(pos, habitat,
					main.getRandom().nextDouble() * 1.5 + 0.5), "SimInsect", "bug.png");
			SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimBird(pos, habitat), "SimBird",
					"bird1.png");
			SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimHyena(pos, habitat, 2.3), "SimHyena",
					"hyena.png");
		}

	}

	/**
	 * This method is called for each step, you can use it to add objects at
	 * random intervals
	 */
	public static void step(SimMain main, Habitat habitat) {
		if(!SimFemaleLion.getCircleOfLife()){
		if (main.getRandom().nextInt(400) == 0) {
			habitat.addObject(new SimTree(main.randomPos(), habitat));
			habitat.addObject(new SimInsect(main.randomPos(),habitat,
					main.getRandom().nextDouble() * 1.5 + 0.5));
		}
	}
	}
}
