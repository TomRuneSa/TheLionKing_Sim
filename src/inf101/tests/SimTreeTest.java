package inf101.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.simulator.Habitat;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.examples.SimTree;

public class SimTreeTest {
	private SimMain main;

	@Before
	public void setup() {
		main = new SimMain();
	}
	
	/**
	 * Test scenario: Places a tree in a habitat, and checks for each step if the height has changed.
	 * And finaly checks if the tree is there.
	 */
	@Test
	public void growAndDestroyTreeTest(){
		Habitat hab = new Habitat(main, 500, 500);
		SimTree tree = new SimTree(new Position(50,50), hab);
		
		hab.addObject(tree);
		
		double height = tree.getHeight();
		
		for (int i = 0; i < 1400; i++){
			hab.step();
			if(height == tree.getHeight()){
				fail("Tree didn't grow");
			}		
		}
		assertFalse("Tree should be gone", tree.exists());
	}
}
