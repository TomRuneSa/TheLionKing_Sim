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
import inf101.simulator.objects.examples.SimLionCub;
import inf101.simulator.objects.examples.SimMaleLion;
import inf101.simulator.objects.examples.SimMareCat;
import inf101.simulator.objects.examples.SimMonkey;
import inf101.simulator.objects.examples.SimWarthog;

public class SimHyenaTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}

	/**
	 * Test scenario: Places a warthog and a marecat with a starving hyena in a habitat. The hyena
	 * should eat both objects within 600 steps because of movement and the eyesight
	 * of the marecat.
	 */
	@Test
	public void willEatTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimHyena sim1 = new SimHyena(new Position(250, 250), hab, 2.5);
		SimMareCat feed1 = new SimMareCat(new Position(270, 250), hab);
		SimWarthog feed2 = new SimWarthog(new Position(290, 250), hab);

		hab.addObject(sim1);
		hab.addObject(feed1);
		hab.addObject(feed2);

		for (int i = 0; i < 600; i++) {
			sim1.SetGetNutrition(299);
			feed1.SetGetNutrition(400);
			feed2.SetGetNutrition(400);
			hab.step();
		}

		assertFalse("Food should be gone", feed1.exists());
		assertFalse("Food should be gone", feed2.exists());
	}

	/**
	 * Test scenario: Places a Lion cub with a starving hyena in a habitat. The hyena
	 * should not eat the cub within 600 steps.
	 */
	@Test
	public void willNotEatTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimHyena sim1 = new SimHyena(new Position(250, 250), hab, 2.5);
		SimLionCub cub = new SimLionCub(new Position(270, 250), hab);

		hab.addObject(sim1);
		hab.addObject(cub);
		
		for (int i = 0; i < 600; i++) {
			sim1.SetGetNutrition(299);
			cub.SetGetNutrition(1000);
			
			hab.step();
		}

		assertTrue("Food should not be gone", cub.exists());

	}
	/**
	 * Test scenario: Places marecat in a habitat, and gets its energy before
	 * and after steps, and then compares them. After 200 steps the energy
	 * should have dropped.
	 */
	@Test
	public void willLoseEnergyTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimHyena sim1 = new SimHyena(new Position(250, 250), hab, 2.3);
		hab.addObject(sim1);

		double eBefore = sim1.getNutrition();

		for (int i = 0; i < 200; i++) {
			hab.step();
		}
		double eAfter = sim1.getNutrition();
		int compare = Double.compare(eBefore, eAfter);
		if (compare <= 0) {
			fail("Energy hasn't dropped");
		}
	}

	/**
	 * Test scenario: Places marecat in a habitat, and sets its nutrition to
	 * 100. The marecat should die within 600 steps.
	 */
	@Test
	public void willDieTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimHyena sim1 = new SimHyena(new Position(250, 250), hab, 2.3);
		hab.addObject(sim1);
		sim1.SetGetNutrition(100);
		for (int i = 0; i < 600; i++) {
			hab.step();
		}
		assertFalse("Animal should be gone", sim1.exists());
	}

	/**
	 * Test scenario: check that marecat avoid hyenas
	 */
	@Test
	public void avoidLions() {
		Habitat hab = new Habitat(main, 1000, 1000);
		SimHyena sim1 = new SimHyena(new Position(250, 250), hab, 2.5);
		SimMaleLion male = new SimMaleLion(new Position(1700, 500), hab);
		
		hab.addObject(sim1);
		hab.addObject(male);

		for (int i = 0; i <1000; i++) {
			male.SetGetNutrition(1000);			
			sim1.SetGetNutrition(1000);
			hab.step();
			assertTrue(sim1.getPosition().distanceTo(male.getPosition()) > sim1.getRadius() + male.getRadius());
//			if ((sim1.getPosition().distanceTo(Lion1.getPosition())) < sim1.getRadius() + Lion1.getRadius() 
//			|| (sim1.getPosition().distanceTo(Lion2.getPosition())) < sim1.getRadius() + Lion2.getRadius()) {
//				fail("To close");
//			}
		}
	}
}
