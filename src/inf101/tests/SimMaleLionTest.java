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
		SimWarthog feed2 = new SimWarthog(new Position(270, 250), hab);

		hab.addObject(sim1);
		hab.addObject(feed1);
		hab.addObject(feed2);

		for (int i = 0; i < 500; i++) {
			sim1.SetGetNutrition(299);
			feed1.SetGetNutrition(1000);
			feed2.SetGetNutrition(1000);
			hab.step();
		}

		assertFalse("Food1 should be gone", feed1.exists());
		assertFalse("Food should be gone", feed2.exists());
	}

	/**
	 * Test scenario: Places marecat in a habitat, and gets its energy before
	 * and after steps, and then compares them. After 200 steps the energy
	 * should have dropped.
	 */
	@Test
	public void willLoseEnergyTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimMaleLion sim1 = new SimMaleLion(new Position(250, 250), hab);
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

	/**
	 * Test scenario: Places male and female in a habitat, and sets their
	 * position. After xxx steps the male should be facing the female.
	 */
	@Test
	public void willFollowTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimMaleLion sim1 = new SimMaleLion(new Position(250, 250), hab);
		SimFemaleLion sim2 = new SimFemaleLion(new Position(270, 250), hab);
		hab.addObject(sim1);
		hab.addObject(sim2);

		for (int i = 0; i < 500; i++) {
			sim1.SetGetNutrition(1000);
			sim2.SetGetNutrition(1000);
			sim1.SetGethBar(0.95);
			sim2.SetGethBar(0.95);

			hab.step();
		}
		double ang = Math.abs(
				sim1.getPosition().directionTo(sim2.getPosition()).toAngle() - sim1.getDirection().toAngle());
		assertTrue(ang<45 || ang >(360-45));
//		assertTrue(Math.abs(
//				sim1.getPosition().directionTo(sim2.getPosition()).toAngle() - sim1.getDirection().toAngle()) < 45);
	}

	/**
	 * Test scenario: check that marecat avoid hyenas
	 */
	@Test
	public void impregnateTest() {
		Habitat hab = new Habitat(main, 1000, 1000);
		SimMaleLion Lion1 = new SimMaleLion(new Position(490, 500), hab);
		SimFemaleLion Lion2 = new SimFemaleLion(new Position(500, 500), hab);
	
		hab.addObject(Lion1);
		hab.addObject(Lion2);

		for (int i = 0; i < 2000; i++) {
			Lion1.SetGetNutrition(1000);
			Lion2.SetGetNutrition(1000);
			Lion1.SetGethBar(0.90);
			Lion2.SetGethBar(0.90);
			hab.step();	
		}
		assertTrue( "Should be pregnant" ,Lion1.getPregnant());
	}
}
