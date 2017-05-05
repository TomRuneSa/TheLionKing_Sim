package inf101.simulator.objects.examples;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.SimEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimInsect extends AbstractMovingObject implements IEdibleObject {
	private static final double defaultSpeed = 1.0;
	private static Habitat habitat;
	private static final double NUTRITION_FACTOR = 10;
	private Image img = MediaHelper.getImage("bug2.jpg");
	private double energyLevel = 1;
	private double size = 1.0;
	public SimInsect(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		
	}
	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.translate(0, getHeight());
		context.scale(1, -1);
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
		super.drawBar(context, energyLevel, 0, Color.PINK, Color.BLUE);
//		GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2, VIEW_DISTANCE, 0, VIEW_ANGLE);
		context.setStroke(Color.YELLOW.deriveColor(0.0, 1.0, 1.0, 0.5));
	}
	@Override
	public double getHeight() {
		return 50;
	}

	@Override
	public double getWidth() {
		return 50;
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
	public double getNutritionalValue() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public void step() {
		if (energyLevel > 0) {
			energyLevel -= 0.0009;
		}
	//Goes toward center if 
			dir = dir.turnTowards(directionTo(habitat.getCenter()), 0.5);

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
