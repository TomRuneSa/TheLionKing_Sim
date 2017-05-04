package inf101.simulator.objects.examples;

import java.util.function.Consumer;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.SimMain;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimListener;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimAnimal extends AbstractMovingObject implements ISimListener {
	private static final double defaultSpeed = 1.0;
	private static Habitat habitat;
	private Image img = MediaHelper.getImage("pipp.png");
	private double energyLevel = 1;
	private static final double MAX_ENERGY = 1.0;
	private static final double MIN_ENERGY = 0.0;
	private static final double VIEW_DISTANCE = 400;
	private static final double VIEW_ANGLE = 40;
	public static final Consumer<GraphicsContext> PAINTER = (GraphicsContext context) -> {
		SimAnimal obj = new SimAnimal(new Position(0, 0), habitat);
		context.scale(0.6 / obj.getWidth(), 0.8 / obj.getHeight());
		obj.draw(context);
	};

	public SimAnimal(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		hab.addListener(this, this);
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
		super.drawBar(context, energyLevel, 0, Color.PINK, Color.BLUE);
		GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2, VIEW_DISTANCE, 0, VIEW_ANGLE);
		context.setStroke(Color.YELLOW.deriveColor(0.0, 1.0, 1.0, 0.5));
	}

	public IEdibleObject getBestFood() {
		return getClosestFood();
	}

	public IEdibleObject getClosestFood() {
		double shorttDist = 401;
		ISimObject closestObject = null;
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {

			if (obj instanceof IEdibleObject) {
				double tempDist = this.distanceTo(Position.makePos((obj).getX(), obj.getY()));
				if (tempDist < shorttDist) {
					closestObject = obj;
					shorttDist = tempDist;
				}
				// food.add((IEdibleObject) obj);
				// Compare compare = new Compare();
				// Collections.sort(food, compare);
				// return getBestFood();
			}
			return (IEdibleObject) closestObject;
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
	public void step() {

		if (energyLevel > MIN_ENERGY) {
			energyLevel -= 0.0009;
		}

		IEdibleObject obj = getClosestFood();
		if (obj != null) {
			dir = dir.turnTowards(super.directionTo(obj), 2);
			if (this.distanceToTouch(obj) < 5) {
				double howMuchToEat = obj.getNutritionalValue();
				obj.eat(howMuchToEat);
				if (energyLevel < MAX_ENERGY) {
					energyLevel += howMuchToEat / 12;
				}
				SimEvent event = new SimEvent(this, "CUUUUNT", null, null);
				habitat.triggerEvent(event);
				energyLevel += howMuchToEat;
			}
		}
		for (ISimObject rep : habitat.allObjects()) {
			if (rep instanceof SimRepellant) {
				if (distanceTo(rep) < 200) {
					Direction dir1 = directionTo(rep);
					Direction dir2 = dir1.turnBack();
					dir = dir.turnTowards(dir2, 2.2);
				}
			}
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

		if (energyLevel < MIN_ENERGY) {
			super.destroy();
			SimEvent dead = new SimEvent(this, "DEAD CUNT", null, null);
			habitat.triggerEvent(dead);
		}

		super.step();
	}

	@Override
	public void eventHappened(SimEvent event) {
		super.say(event.getType());

	}

}
