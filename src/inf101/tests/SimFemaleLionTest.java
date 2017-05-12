package inf101.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.examples.SimFemaleLion;
import inf101.simulator.objects.examples.SimHyena;
import inf101.simulator.objects.examples.SimInsect;
import inf101.simulator.objects.examples.SimMaleLion;
import inf101.simulator.objects.examples.SimMareCat;
import inf101.simulator.objects.examples.SimMonkey;
import inf101.simulator.objects.examples.SimWarthog;

public class SimFemaleLionTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}

	/**
	 * Test scenario: Places a female lion and a marecat in a habitat. The lion
	 * should eat the food objects within 500 steps because of movement and the
	 * eyesight of the lion.
	 */
	@Test
	public void willEatTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimFemaleLion female = new SimFemaleLion(new Position(230, 250), hab);
		SimMareCat feed1 = new SimMareCat(new Position(250, 250), hab);

		hab.addObject(female);
		hab.addObject(feed1);

		for (int i = 0; i < 500; i++) {
			female.SetGetNutrition(299);
			feed1.SetGetNutrition(500);

			hab.step();
		}

		assertFalse("Food1 should be gone", feed1.exists());

	}

	/**
	 * Test scenario: Places FemaleLion in a habitat. The lion should die within
	 * 1000 steps.
	 */
	@Test
	public void willDieTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimFemaleLion female = new SimFemaleLion(new Position(250, 250), hab);
		hab.addObject(female);
		female.SetGetNutrition(100);
		for (int i = 0; i < 1000; i++) {
			hab.step();
		}
		assertFalse("Animal should be gone", female.exists());
	}

	/**
	 * Test scenario: Places horny MaleLion, and a horny female lion in a
	 * habitat. Within 2 steps, a cub should be born.
	 * 
	 * Issue: If the step counter is to high, it will try to play a musicfile
	 * that it can't reach, and therefore will fail.
	 */
	@Test
	public void giveBirthTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimFemaleLion female = new SimFemaleLion(new Position(250, 250), hab);
		SimMaleLion male = new SimMaleLion(new Position(249, 250), hab);

		hab.addObject(female);
		hab.addObject(male);

		female.SetGetNutrition(1000);
		male.SetGetNutrition(1000);
		for (int i = 0; i < 2; i++) {
			female.SetGethBar(0.95);
			male.SetGethBar(0.95);
			hab.step();
		}
		if (!female.getBorn()) {
			fail("Should be born");
		}

	}

}
