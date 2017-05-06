package inf101.simulator;

import inf101.simulator.objects.examples.Blob;
import inf101.simulator.objects.examples.SimAnimal;
import inf101.simulator.objects.examples.SimBird;
import inf101.simulator.objects.examples.SimFeed;
import inf101.simulator.objects.examples.SimInsect;
import inf101.simulator.objects.examples.SimRepellant;
import inf101.simulator.objects.examples.SimWarthog;
import inf101.simulator.objects.examples.SimMareCat;

public class Setup {
	/** This method is called when the simulation starts */
	public static void setup(SimMain main, Habitat habitat) {
//		habitat.addObject(new SimAnimal(new Position(400, 400), habitat));
//		habitat.addObject(new SimInsect(new Position(400, 400), habitat, main.getRandom().nextDouble()*2+0.5));
//		habitat.addObject(new Blob(new Direction(0), new Position(400, 400), 1));
		habitat.addObject(new SimWarthog(new Position(400, 400), habitat));
		habitat.addObject(new SimMareCat(main.randomPos(), habitat));

		for (int i = 0; i < 3; i++)
			habitat.addObject(new SimRepellant(main.randomPos()));

		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimFeed(pos, main.getRandom().nextDouble()*2+0.5), "SimFeed™", SimFeed.PAINTER);
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimRepellant(pos), "SimRepellant™",
				SimRepellant.PAINTER);
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimAnimal(pos, habitat), "SimAnimal",
				"pipp.png");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimInsect(pos, habitat, main.getRandom().nextDouble()*1.5+0.5), "SimInsect",
				"bug2.jpg");
		SimMain.registerSimObjectFactory((Position pos, Habitat hab) -> new SimBird(pos, habitat), "SimBird",
				"bird1.png");
		
	}

	/**
	 * This method is called for each step, you can use it to add objects at
	 * random intervals
	 */
	public static void step(SimMain main, Habitat habitat) {
		if (main.getRandom().nextInt(300) == 0)
			habitat.addObject(new SimFeed(main.randomPos(), main.getRandom().nextDouble()*2+0.5));

	}
}
