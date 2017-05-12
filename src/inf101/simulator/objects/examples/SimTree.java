package inf101.simulator.objects.examples;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SimTree extends AbstractSimObject {
	private Image img = MediaHelper.getImage("bananatree.png");
	private double height = 20.0;
	private double width = 20.0;
	private static Habitat habitat;

	public SimTree(Position pos, Habitat hab) {
		super(new Direction(0), pos);
		this.habitat = hab;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.translate(0, getHeight());
		context.scale(1, -1);
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void step() {
		
		if (height <= 70) {
			height += 0.05;
			width += 0.05;
			// Gets the height and the width of the three to grow a vertain
			// amount each round.
		} else {
			habitat.addObject(new SimBanana(this.getPosition(), 1));
			this.destroy();
			// When the three is to big, it will spawn a banana, and the three
			// will be destroyed.
		}

	}

}
