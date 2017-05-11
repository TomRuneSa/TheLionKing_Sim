package inf101.simulator.objects.examples;

import java.util.Random;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.SimEvent;
import inf101.util.generators.DirectionGenerator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimInsect extends AbstractMovingObject implements IEdibleObject {
	private static final double defaultSpeed = 1.0;
	private static Habitat habitat;
	private static final double NUTRITION_FACTOR = 10;
	private static final double DIAMETER = 25;
	private Image img = MediaHelper.getImage("bug.png");
	private double energyLevel = 1;
	private double size = 1.0;
	private int steps = 0;
	private static DirectionGenerator dirGen = new DirectionGenerator();
	
	public SimInsect(Position pos, Habitat hab, double size) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		this.size = size;
		
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		if(-90 < super.getDirection().toAngle() && super.getDirection().toAngle() < 90){
			context.translate(0, getHeight());
			context.scale(1.0, -1.0);
			
		}
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
		super.drawBar(context, energyLevel, 0, Color.PINK, Color.BLUE);
	}
	
	public double getEnergy(){
		return energyLevel;
	}
	
	@Override
	public double getHeight() {
		return DIAMETER * size;
	}

	@Override
	public double getNutritionalValue() {
		return size * NUTRITION_FACTOR;
	}

	@Override
	public double getWidth() {
		return DIAMETER * size;
	}
	@Override
	public double eat(double howMuch) {
		double deltaSize = Math.min(size, howMuch / NUTRITION_FACTOR);
		size -= deltaSize;
		if (size == 0)
			destroy();
		return deltaSize * NUTRITION_FACTOR;
	}

	
	@Override
	public void step() {
		if (energyLevel > 0) {
			energyLevel -= 0.00039;
		}
		if(steps == 200){
			Random i = new Random();
			Direction dr = dirGen.generate(i);
			dir= dir.turnTowards(dr, 15);
			steps = 0;
		}

			// go towards center if we're close to the border
			if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
				dir = dir.turnTowards(directionTo(habitat.getCenter()), 5);
				if (!habitat.contains(getPosition(), getRadius())) {
					// we're actually outside
					accelerateTo(5 * defaultSpeed, 0.3);
				}
			}

			accelerateTo(defaultSpeed, 0.1);

			if (energyLevel < 0) {
				super.destroy();
				}
			super.step();
		}

}
