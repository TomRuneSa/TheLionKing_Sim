package inf101.simulator.objects.examples;

import inf101.simulator.Direction;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import inf101.simulator.objects.IEdibleObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SimBanana extends AbstractSimObject implements IEdibleObject {
	private static final double NUTRITION_FACTOR = 10.0;
	private static final double DIAMETER = 25;
	private double size = 1.0;
	private Image img = MediaHelper.getImage("banana.jpg");

	public SimBanana(Position pos, double size) {
		super(new Direction(0), pos);
		this.size = size;

	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.translate(0, getHeight());
		context.scale(1, -1);
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
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
	public double getHeight() {
		return DIAMETER * size;
	}

	@Override
	public double getWidth() {
		return DIAMETER * size;
	}

	@Override
	public void step() {
	}

	@Override
	public double getNutritionalValue() {
		return size * NUTRITION_FACTOR;
	}

}
