package inf101.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.examples.SimBird;
import inf101.simulator.objects.examples.SimInsect;

public class SimBirdTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}

	/**
	 * Test scenario: Places a bug and a bird in a habitat. The bird
	 * should eat the food within 200 steps because of movement and the
	 * eyesight of the bird.
	 */
	@Test
	public void willEatTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimBird bird = new SimBird(new Position(250, 250), hab);
		SimInsect feed1 = new SimInsect(new Position(350, 250), hab, 1.0);
		hab.addObject(bird);
		hab.addObject(feed1);

		for (int i = 0; i < 200; i++) {
			bird.SetGetNutrition(500);
			hab.step();
		}

		assertFalse("Food should be gone", feed1.exists());
	}

	/**
	 * Test scenario: Places bird in a habitat, and gets its energy before
	 * and after steps, and then compares them. After 200 steps the energy
	 * should have dropped.
	 */
	@Test
	public void willLoseEnergyTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimBird bird = new SimBird(new Position(250, 250), hab);
		hab.addObject(bird);

		double eBefore = bird.getNutrition();

		for (int i = 0; i < 200; i++) {
			hab.step();
		}
		double eAfter = bird.getNutrition();
		int compare = Double.compare(eBefore, eAfter);
		if (compare <= 0) {
			fail("Energy hasn't dropped");
		}
	}

	/**
	 * Test scenario: Places bird in a habitat, and sets its nutrition to
	 * 100. The bird should die within 600 steps.
	 */
	@Test
	public void willDieTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimBird bird = new SimBird(new Position(250, 250), hab);
		hab.addObject(bird);
		bird.SetGetNutrition(100);
		for (int i = 0; i < 600; i++) {
			hab.step();
		}
		assertFalse("Animal should be gone", bird.exists());
	}
}
