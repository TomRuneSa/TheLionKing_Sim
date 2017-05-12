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

public class SimMaleLionTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}

	/**
	 * Test scenario: Places a warthog and marecat with a Malelion in a habitat.
	 * The lion should eat both objects within 500 steps because of movement and
	 * the eyesight of the marecat.
	 */
	@Test
	public void willEatTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimMaleLion sim1 = new SimMaleLion(new Position(230, 250), hab);
		SimMareCat feed1 = new SimMareCat(new Position(250, 250), hab);
		

		hab.addObject(sim1);
		hab.addObject(feed1);
		

		for (int i = 0; i < 500; i++) {
			sim1.SetGetNutrition(299);
			feed1.SetGetNutrition(500);
			
			hab.step();
		}

		assertFalse("Food1 should be gone", feed1.exists());
		
	}



	@Test
	public void willDieTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimMaleLion sim1 = new SimMaleLion(new Position(250, 250), hab);
		hab.addObject(sim1);
		sim1.SetGetNutrition(100);
		for (int i = 0; i < 1000; i++) {
			hab.step();
		}
		assertFalse("Animal should be gone", sim1.exists());
	}


}
