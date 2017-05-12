package inf101.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.examples.SimInsect;
import inf101.simulator.objects.examples.SimLionCub;

public class SimLionCubTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}

	/**
	 * Test scenario: Places a lion cub and an insect in a habitat. The lion
	 * should eat the food object within 500 steps because of movement and the
	 * eyesight of the lion.
	 */
	@Test
	public void willEatTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimLionCub cub = new SimLionCub(new Position(230, 250), hab);
		SimInsect feed1 = new SimInsect(new Position(250, 250), hab, 1);

		hab.addObject(cub);
		hab.addObject(feed1);

		for (int i = 0; i < 500; i++) {
			cub.SetGetNutrition(299);
			hab.step();
		}

		assertFalse("Food1 should be gone", feed1.exists());

	}

	/**
	 * Test scenario: Places a lion cub in a habitat. The lion should die within
	 * 1000 steps.
	 */
	@Test
	public void willDieTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimLionCub cub = new SimLionCub(new Position(250, 250), hab);
		hab.addObject(cub);
		cub.SetGetNutrition(100);
		for (int i = 0; i < 1000; i++) {
			hab.step();
		}
		assertFalse("Animal should be gone", cub.exists());
	}

}
