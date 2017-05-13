package inf101.simulator.objects.examples;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractSimObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SimRock extends AbstractSimObject {
	public static final Image img = MediaHelper.getImage("rock.png");
	private double height = 500.0;
	private double width = 500.0;
	private static Habitat habitat;
	
	public SimRock(Position pos, Habitat hab) {
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
		// TODO Auto-generated method stub
		
	}

}
