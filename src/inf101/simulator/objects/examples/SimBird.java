package inf101.simulator.objects.examples;

import java.util.ArrayList;
import java.util.Collections;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimBird extends AbstractMovingObject implements IEdibleObject {
	private static final double defaultSpeed = 1.0;
	private static Habitat habitat;
	private static final double NUTRITION_FACTOR = 10;
	private Image img = MediaHelper.getImage("bird1.png");
	private double energyLevel = 1;
	private double size = 1.0;
	private static final double VIEW_DISTANCE = 400;
	private static final double VIEW_ANGLE = 45;
	private ArrayList<IEdibleObject> foodBird = new ArrayList<>();

	public SimBird(Position pos, Habitat hab) {
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
		 GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2,
		 VIEW_DISTANCE, 0, VIEW_ANGLE);
		context.setStroke(Color.YELLOW.deriveColor(0.0, 1.0, 1.0, 0.5));
	}

	public IEdibleObject getClosestFood() {
		double shorttDist = 401;
		ISimObject closestObject = null;
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {

			if (obj instanceof SimInsect) {
				double tempDist = this.distanceTo(Position.makePos((obj).getX(), obj.getY()));
				
				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
				
				if (tempDist < shorttDist) {
					closestObject = obj;
					shorttDist = tempDist;
				}
				}

				return (IEdibleObject) closestObject;
			}
		}

		return null;
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
		return 50;
	}

	@Override
	public void step() {
		if (energyLevel > 0) {
			energyLevel -= 0.0009;
		}
		IEdibleObject obj = getClosestFood();
		if (obj != null) {
			dir = dir.turnTowards(super.directionTo(obj), 2);
			if (this.distanceToTouch(obj) < 5) {
				double howMuchToEat = obj.getNutritionalValue();
				obj.eat(howMuchToEat);
//				if (energyLevel < MAX_ENERGY) {
//					energyLevel += howMuchToEat / 12;
//				}
				SimEvent event = new SimEvent(this, "CUUUUNT", null, null);
				habitat.triggerEvent(event);
				energyLevel += howMuchToEat;
			}
		}
		// Goes toward center if
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

	public double angleFix(double a, double b) {
		double angle = ((((a - b) % 360) + 540) % 360) - 180; // stackoverflow
		return angle;

	}

}
