package inf101.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.examples.SimTree;

public class SimBananaTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}
	
	/**
	 * Test scenario: Places a tree in a habitat, and checks if after the tree is destroyed,
	 * there is a banana on the spot the tree used to be on.
	 */
	@Test
	public void getSpawnedTest(){
		Habitat hab = new Habitat(main, 500, 500);
		SimTree tree = new SimTree(new Position(50,50), hab);
		
		hab.addObject(tree);
		
		for (int i = 0; i < 1500; i++){	
			Position pos = tree.getPosition();
			if(pos == null){
				fail("Should be banana here");
			}
			hab.step();
		}
		
		assertFalse("Tree should be gone", tree.exists());
		
	}
}
