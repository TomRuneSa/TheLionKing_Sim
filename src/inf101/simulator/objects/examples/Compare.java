package inf101.simulator.objects.examples;

import java.util.Comparator;

import inf101.simulator.objects.IEdibleObject;

public class Compare implements Comparator<IEdibleObject> {

	@Override
	public int compare(IEdibleObject e1, IEdibleObject e2) {
		
		double food1 = e1.getNutritionalValue();
		double food2 = e2.getNutritionalValue();
		return Double.compare(food1, food2);
		
	}
	
	
	

}
