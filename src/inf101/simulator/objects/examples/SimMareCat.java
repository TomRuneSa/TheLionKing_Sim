package inf101.simulator.objects.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import inf101.simulator.Direction;
import inf101.simulator.Habitat;
import inf101.simulator.MediaHelper;
import inf101.simulator.Position;
import inf101.simulator.objects.AbstractMovingObject;
import inf101.simulator.objects.IEdibleObject;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import inf101.util.generators.DirectionGenerator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimMareCat extends AbstractMovingObject implements IEdibleObject {
	private static final double defaultSpeed = 1.0;
	private static Habitat habitat;
	private static final double NUTRITION_FACTOR = 10;
	private Image img = MediaHelper.getImage("Timon.png");
	private double size = 1.0;
	private ArrayList<IEdibleObject> insects = new ArrayList<>();
	private double nutrition = 1000.0;// A value for the nutrition of this
										// object.
	private double barValue = 1.0;// A bar that shows the health of this object.
	private int steps = 0;// The amount of steps that's been taken.
	private static DirectionGenerator dirGen = new DirectionGenerator();// A
																		// directionGenerator
																		// to
																		// generate
																		// random
																		// directions.

	public SimMareCat(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		SimMareCat.habitat = hab;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		if (-90 < super.getDirection().toAngle() && super.getDirection().toAngle() < 90) {
			context.translate(0, getHeight());
			context.scale(1.0, -1.0);

		}
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
		super.drawBar(context, barValue, 0, Color.RED, Color.BLUE);
	}

	public IEdibleObject getClosestFood() {
		double shorttDist = 401;
		ISimObject closestObject = null;
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {

			if (obj instanceof SimInsect) {
				// The objects that are edible for the marecat.
				double tempDist = this.distanceTo(Position.makePos((obj).getX(), obj.getY()));
				// Creates a double value that is the distance from this object
				// to the edibleObject.
				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
					// Makes sure that the closest food only will be returned if
					// it's in the field of vision,
					// 45 degrees in both directions.
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
		// Finds the minimal value of the size and the input/nutrition factor of
		// this object.
		size -= deltaSize;
		// The size decreases by "howMuch".
		if (size == 0)
			destroy();
		// if the size goes all the way down to 0, this object will die.
		return deltaSize * NUTRITION_FACTOR;
	}

	@Override
	public double getNutritionalValue() {
		return size * NUTRITION_FACTOR;
		// Returns the nutrition value of this object, if someone wants to eat
		// it, they know how much it can eat.
	}

	public IEdibleObject getBestFood() {
		insects.clear();
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (obj instanceof SimInsect) {
			// The objects that are edible for the monkey.
			double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
			double simAngle = this.getDirection().toAngle();
			double angle = angleFix(simRepAngle, simAngle);

			if (angle < 45 && angle > -45) {
				insects.add((IEdibleObject) obj);
				// Makes sure that the banana only will be added to the list
				// if it's in the field of vision,
				// 45 degrees in both directions.
			}
		}
		}

		if (insects.size() == 0) {
			return null;
		}
		Compare compare = new Compare();
		Collections.sort(insects, compare);
		// The food is compared using the new compare
		return (IEdibleObject) insects.get(insects.size() - 1);
		// Returns the last object in the list, as this is the object with most
		// food in it.
	}

	public void SetGetNutrition(double nutrition) {
		if (nutrition > 1000 || nutrition < 0) {
			System.out.println("The nutrition has to be more than 0 and less than 1000");
			return;
		}
		this.nutrition = nutrition;// Sets the animals nutrition to the new
									// value.
	}

	public double getNutrition() {
		return nutrition;// Returns the nutrition value
	}

	@Override
	public void step() {

		boolean follow = false;

		steps++;
		// Increases the step counter
		nutrition -= 0.2;
		// The objects nutrition will decrease each time step() is called.
		barValue = nutrition / 1000;
		// Calculates the value that will be shown in the bar over this objects
		// head.
		int hunger = hungerStatus.hungerStatus(nutrition);
		// Find out how hungry this object is.

		for (ISimObject danger : habitat.allObjects()) {
			if (danger instanceof SimHyena || danger instanceof SimMaleLion || danger instanceof SimFemaleLion) {
				if (distanceTo(danger) < 200) {
					// The cub senses danger within a certain range.
					Direction dir1 = directionTo(danger);
					Direction dir2 = dir1.turnBack();
					dir = dir.turnTowards(dir2, 2.2);
					// Creates a direction towards the danger, another direction
					// in the opposite direction,
					// and then turns towards the opposite direction
				}
			}
		}

		if (hunger == 0) {
			IEdibleObject obj = getBestFood();
			// If the warthog only is moderately hungry, it will search for the
			// best food.
			if (obj != null) {
				dir = dir.turnTowards(super.directionTo(obj), 2.4);
				accelerateTo(1.8 * defaultSpeed, 0.3);
				if (this.distanceToTouch(obj) < 5) {
					double howMuchToEat = 1 - barValue;
					// Eats the maximum amount it can without the bar going over
					// it's limit.
					obj.eat(howMuchToEat);
					if (barValue < 1) {
						nutrition += obj.getNutritionalValue();
						// The nutrition has the objects nutritional value
						// added.
					}
				}
			}
		}
		if (hunger < 0) {
			IEdibleObject obj = getClosestFood();
			// If the marecat only is moderately hungry, it will search for the
			// best food,
			if (obj != null) {
				dir = dir.turnTowards(super.directionTo(obj), 2.4);
				// Turns towards the food and accelerates to catch up
				accelerateTo(1.8 * defaultSpeed, 0.3);
				if (this.distanceToTouch(obj) < 5) {
					double howMuchToEat = 1 - barValue;
					obj.eat(howMuchToEat);
					// Eats the maximum amount it can without the bar going over
					// it's limit.
					if (barValue < 1) {
						nutrition += obj.getNutritionalValue();
						// The nutrition has the objects nutritional value
						// added.
					}
				}
			}
		}
		if (hunger > 0) {
			for (ISimObject mate : habitat.nearbyObjects(this, getRadius() + 400)) {
				if (mate instanceof SimMaleLion) {
					// The marecat will by default search for the warthog.
					double simRepAngle = this.getPosition().directionTo(mate.getPosition()).toAngle();
					double simAngle = this.getDirection().toAngle();
					double angle = angleFix(simRepAngle, simAngle);

					if (angle < 45 && angle > -45) {
						follow = true;
						dir = dir.turnTowards(super.directionTo(mate.getPosition()), 2.1);
						// If the warthog is within sights, the follow boolean
						// will be true, and the marecat will turn towards the
						// direction of the warthog
					}
				}
			}
		}

		if (steps == 200 && !follow) {
			Random i = new Random();
			Direction dr = dirGen.generate(i);
			dir = dir.turnTowards(dr, 15);
			steps = 0;
			// Creates a new direction every 200 steps, as long as the cub isn't
			// following its father, and resets the steps counter
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

		if (nutrition < 0.1) {
			super.destroy();
			//This object will die if it's nutrition goes to far down
		}
		super.step();
	}

	public double angleFix(double a, double b) {
		double angle = ((((a - b) % 360) + 540) % 360) - 180; 
		return angle;
		//This part of the code was found on stackoverflow. 
		//It creates a double value that corresponds to an angle. It does this with two double values that it gets as input. 
	}

}
