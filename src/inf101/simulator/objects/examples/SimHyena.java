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
import inf101.simulator.objects.ISimListener;
import inf101.simulator.objects.ISimObject;
import inf101.simulator.objects.SimEvent;
import inf101.util.generators.DirectionGenerator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SimHyena extends AbstractMovingObject implements IEdibleObject, ISimListener {
	private static final double defaultSpeed = 1.3;
	private static Habitat habitat;
	private static final double DIAMETER = 40;
	private static final double NUTRITION_FACTOR = 90;
	private double size = 1.0;
	private Image img = MediaHelper.getImage("hyena.png");
	private ArrayList<IEdibleObject> foodHyena = new ArrayList<>();
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
	private int packSize = 1;// A counter that keeps count of how many hyenas
								// there are in the "pack"

	public SimHyena(Position pos, Habitat hab, double size) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		this.size = size;
		this.habitat.addListener(this, this);
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
		for (ISimObject food : habitat.nearbyObjects(this, getRadius() + 400)) {

			if (food instanceof SimMareCat || food instanceof SimWarthog || food instanceof SimMonkey
					|| food instanceof SimBird) {
				// The objects that are edible for the hyena.
				double tempDist = this.distanceTo(Position.makePos((food).getX(), food.getY()));
				// Creates a double value that is the distance from this object
				// to the edibleObject.
				double simRepAngle = this.getPosition().directionTo(food.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
					// Makes sure that the closest food only will be returned if
					// it's in the field of vision,
					// 45 degrees in both directions.
					if (tempDist < shorttDist) {
						closestObject = food;
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
		return DIAMETER * size;
	}

	@Override
	public double getWidth() {
		return DIAMETER * size;
	}

	public int GetPackSize() {
		packSize = 1;
		for (ISimObject mate : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (mate instanceof SimHyena) {
				packSize++;
				// Goes through nearby objects in search of other hyenas. If
				// found, the packSize will increase by one.
			}
		}
		return packSize;
	}

	@Override
	public double eat(double howMuch) {
		double deltaSize = Math.min(size, howMuch / NUTRITION_FACTOR);
		// Finds the minimal value of the size and the input/nutrition factor of
		// this object.
		size -= deltaSize;
		// The size decreases by "howMuch".
		nutrition -= howMuch * NUTRITION_FACTOR;
		// The nutrition decreases by howmuch* the nutrition factor of this
		// object.
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

	public double getNutrition() {
		return nutrition;// Returns the nutrition value
	}

	public void SetGetNutrition(double nutrition) {
		if (nutrition > 1000 || nutrition < 0) {
			System.out.println("The nutrition has to be more than 0 and less than 1000");
			return;
		}
		this.nutrition = nutrition;// Sets the animals nutrition to the new
									// value.
	}

	public IEdibleObject getBestFood() {
		foodHyena.clear();// Clears the arraylist, so that objects wont be added
		// more than 1 time.
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (obj instanceof SimMareCat || obj instanceof SimWarthog || obj instanceof SimMonkey
					|| obj instanceof SimBird) {
				// The objects that are edible for the hyena.
				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
					foodHyena.add((IEdibleObject) obj);
					// Makes sure that the food only will be added to the list
					// if it's in the field of vision,
					// 45 degrees in both directions.
				}
			}
		}

		if (foodHyena.size() == 0) {
			return null;
		}
		Compare compare = new Compare();
		Collections.sort(foodHyena, compare);
		// The food is compared using the new compare
		return (IEdibleObject) foodHyena.get(foodHyena.size() - 1);
		// Returns the last object in the list, as this is the object with most
		// food in it.
	}

	public IEdibleObject getLions() {
		double shorttDist = 401;
		ISimObject closestObject = null;
		for (ISimObject food : habitat.nearbyObjects(this, getRadius() + 400)) {

			if (food instanceof SimMaleLion || food instanceof SimFemaleLion) {
				double tempDist = this.distanceTo(Position.makePos((food).getX(), food.getY()));
				double simRepAngle = this.getPosition().directionTo(food.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
					if (tempDist < shorttDist) {
						closestObject = food;
						shorttDist = tempDist;
					}
				}
				return (IEdibleObject) closestObject;
			}
		}
		return null;
		// This method is the same as getClosestFood(), only for male and female
		// lions.
	}

	@Override
	public void step() {
		int pack = GetPackSize();// Keeps controll over how big the hyena pack
				// is
		if(!SimFemaleLion.getCircleOfLife()){
		steps++;
		// Increases the step counter
		nutrition -= 0.2;
		// The objects nutrition will decrease each time step() is called.
		barValue = nutrition / 1000;
		// Calculates the value that will be shown in the bar over this objects
		// head.
		int hunger = hungerStatus.hungerStatus(nutrition);
		// Find out how hungry this object is.
		if (pack < 3) {
			// If there aren't 3 or more members of the pack, the hyenas will be
			// scared for the lions
			for (ISimObject run : habitat.allObjects()) {
				if (run instanceof SimMaleLion || run instanceof SimFemaleLion) {
					if (distanceTo(run) < 500) {
						// The hyena senses danger within a certain range.
						Direction dir1 = directionTo(run);
						Direction dir2 = dir1.turnBack();
						dir = dir.turnTowards(dir2, 2.2);
						accelerateTo(1.8 * defaultSpeed, 0.3);
						// Creates a direction towards the danger, another
						// direction in the opposite direction,
						// and then turns towards the opposite direction
					}
				}
			}
		}
		if (hunger == 0) {
			IEdibleObject obj = getBestFood();
			// If the hyena only is moderately hungry, it will search for the
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
			// If the hyena only is moderately hungry, it will search for the
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
				if (mate instanceof SimHyena) {
					// The cub will by default search for its father.
					double simRepAngle = this.getPosition().directionTo(mate.getPosition()).toAngle();
					double simAngle = this.getDirection().toAngle();
					double angle = angleFix(simRepAngle, simAngle);

					if (angle < 45 && angle > -45) {
						dir = dir.turnTowards(super.directionTo(mate.getPosition()), 2.1);
						// If the father is within sights, the follow boolean
						// will be true, and the cub will turn towards the
						// direction of the father
					}
				}
			}
		}
		if (hunger < 0 && pack >= 3) {
			// If the hyena is starving, and the pack is big enough, the hyena
			// will dare to try and eat the lions
			IEdibleObject obj = getLions();
			if (obj != null) {

				SimEvent event = new SimEvent(this, "Nam", this, obj.getPosition());
				habitat.triggerEvent(event);
				// Creates an event that tells all the hyenas that they are
				// attacking the lion, and everyone should come to the location
				// they're calling from.

				dir = dir.turnTowards(super.directionTo(obj), 2.4);
				accelerateTo(1.8 * defaultSpeed, 0.3);
				if (this.distanceToTouch(obj) < 5) {
					double howMuchToEat = 1 - barValue;
					obj.eat(howMuchToEat);
					if (barValue < 1) {
						nutrition += obj.getNutritionalValue();
					}

				}
			}
		}
		if (steps == 150) {
			Random i = new Random();
			Direction dr = dirGen.generate(i);
			dir = dir.turnTowards(dr, 20);
			steps = 0;
			// Creates a new direction every 200 steps, as long as the cub isn't
			// following its father, and resets the steps counter
		}

		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
			dir = dir.turnTowards(directionTo(habitat.getCenter()), 5);
			if (!habitat.contains(getPosition(), getRadius())) {
				// we're actually outside
				accelerateTo(1.2 * defaultSpeed, 0.1);
			}
		}
		}
		else{
			nutrition = 1000;
		}

		accelerateTo(defaultSpeed, 0.1);

		if (nutrition < 0.1) {
			super.destroy();
			// This object will die if it's nutrition goes to far down
		}
		super.step();
	}

	public double angleFix(double a, double b) {
		double angle = ((((a - b) % 360) + 540) % 360) - 180;
		return angle;
		// This part of the code was found on stackoverflow.
		// It creates a double value that corresponds to an angle. It does this
		// with two double values that it gets as input.
	}

	@Override
	public void eventHappened(SimEvent event) {
		if (event.getType().equals("Go")) {
			dir = dir.turnTowards(directionTo((Position) event.getData()), .5);
		}
		// Tells what's supposed to happen when this event is triggered, which
		// is that the listeners will move towards å position
	}

}
