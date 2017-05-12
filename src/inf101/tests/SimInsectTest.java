package inf101.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.examples.SimBird;
import inf101.simulator.objects.examples.SimInsect;

public class SimInsectTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}

	/**
	 * Test scenario: Places a bug in a habitat. The bug
	 * should move within 200 steps.
	 */
	@Test
	public void willMoveTest() {
		Habitat hab = new Habitat(main, 500, 500);

		SimInsect bug = new SimInsect(new Position(350, 250), hab, 1.0);
	
		hab.addObject(bug);
		Position pos1 = bug.getPosition();
		
		for (int i = 0; i < 200; i++) {
			
			hab.step();
		}
		Position pos2 = bug.getPosition();
		if(pos1 == pos2){
			fail("Haven't moved!");
		}
	}
	
	/**
	 * Test scenario: Places bug in a habitat. The bug should die within 2600 steps.
	 */
	@Test
	public void willDieTest() {
		Habitat hab = new Habitat(main, 500, 500);
		SimInsect bug = new SimInsect(new Position(350, 250), hab, 1.0);
		hab.addObject(bug);
		
		for (int i = 0; i < 2600; i++) {
			hab.step();
		}
		assertFalse("Animal should be gone", bug.exists());
	}
}
