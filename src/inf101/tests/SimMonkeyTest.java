package inf101.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.examples.SimBanana;
import inf101.simulator.objects.examples.SimHyena;
import inf101.simulator.objects.examples.SimInsect;
import inf101.simulator.objects.examples.SimMareCat;
import inf101.simulator.objects.examples.SimMonkey;
import inf101.simulator.objects.examples.SimWarthog;

public class SimMonkeyTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}

	/**
	 * Test scenario: Places a monkey and a banana in a habitat. The monkey
	 * should eat the food within 500 steps because of movement and the
	 * eyesight of the monkey.
	 */
	@Test
	public void willEatTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimMonkey monkey = new SimMonkey(new Position(250, 250), hab);
		SimBanana feed1 = new SimBanana(new Position(350, 250), 1.0);
		hab.addObject(monkey);
		hab.addObject(feed1);

		for (int i = 0; i < 500; i++) {
			monkey.SetGetNutrition(500);
			hab.step();
		}

		assertFalse("Food should be gone", feed1.exists());
	}

	/**
	 * Test scenario: Places monkey in a habitat, and gets its energy before and after steps,
	 * and then compares them.
	 * After 200 steps the energy should have dropped.
	 */
	@Test
	public void willLoseEnergyTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimMonkey monkey = new SimMonkey(new Position(250, 250), hab);
		hab.addObject(monkey);

		double eBefore = monkey.getNutrition();

		for (int i = 0; i < 200; i++) {
			hab.step();
		}
		double eAfter = monkey.getNutrition();
		int compare = Double.compare(eBefore, eAfter);
		if (compare <= 0) {
			fail("Energy hasn't dropped");
		}
	}
	
	/**
	 * Test scenario: Places monkey in a habitat, and sets its nutrition to 100.
	 * The monkey should die within 600 steps.
	 */
	@Test
	public void willDieTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimMonkey monkey = new SimMonkey(new Position(250, 250), hab);
		hab.addObject(monkey);
		monkey.SetGetNutrition(100);
		for (int i = 0; i < 600; i++) {
			hab.step();
		}
		assertFalse("Animal should be gone", monkey.exists());
	}
	
	/**
	 * Test scenario: check that monkey avoid hyenas
	 * 
	 * Issue: It may happen that the monkey will get stuck between the edge of the habitat and the hyena,
	 * If that happens, the test will fail, because it'll get to close to the hyena.
	 */
	@Test
	public void avoidHyenas() {
		Habitat hab = new Habitat(main, 1000, 1000);
		SimMonkey monkey = new SimMonkey(new Position(100, 1000), hab);
		SimHyena hyena = new SimHyena(new Position(0, 500), hab, 2.3);
		hab.addObject(monkey);
		hab.addObject(hyena);

		for (int i = 0; i < 150000; i++) {
			hyena.SetGetNutrition(1000);
			monkey.SetGetNutrition(1000);
			if(hab.contains(monkey.getPosition(), 200)){
			hab.step();
			}
			if((monkey.getPosition().distanceTo(hyena.getPosition())) < monkey.getRadius() + hyena.getRadius()){
				fail("To close");
			}
		}
	}
}

