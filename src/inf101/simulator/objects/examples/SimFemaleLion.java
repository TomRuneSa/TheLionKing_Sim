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

public class SimFemaleLion extends AbstractMovingObject implements IEdibleObject, ISimListener {
	private static final double defaultSpeed = 1.6;
	private static Habitat habitat;
	private Image img = MediaHelper.getImage("sarabi.png");
	private ArrayList<IEdibleObject> foodLion = new ArrayList<>();

	private static final double NUTRITION_FACTOR = 100;
	private double size = 1.0;
	private static boolean circleOfLife = false;
	private int harmony = 0;
	private double nutrition = 1000.0;// A value for the nutrition of this
										// object.
	private double barValue = 1.0;// A bar that shows the health of this object.
	private double hBar = 0.95;// A bar that shows how "horny" the male is.
	private boolean born = false;// A boolean that shows if the cub has been born
									// the female.
	private int steps = 0;// The amount of steps that's been taken.
	private static DirectionGenerator dirGen = new DirectionGenerator();// A
																		// directionGenerator
																		// to
																		// generate
																		// random
																		// directions.

	public SimFemaleLion(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		SimFemaleLion.habitat = hab;
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
		super.drawBar(context, hBar, 3 / 2, Color.YELLOW, Color.GREEN);
	}
	
	public boolean getBorn() {
		return born;// Return whether a cub has been born or not.
	}

	@Override
	public double getHeight() {
		return 141;
	}

	@Override
	public double getWidth() {
		return 141;
	}

	public void SetGethBar(double value) {
		if (value > 1 || value < 0) {
			System.out.println("The value has to be between 0 and 100");
			return;
		}
		this.hBar = value;// Sets the value of the hBar to the new value.
	}

	public double getHBar() {
		return hBar;// Returns the value in hBar.
	}

	public double getNutrition() {
		return nutrition;// Returns the nutrition value
	}

	public void SetGetNutrition(double nutrition) {
		if (nutrition > 1000 || nutrition < 0) {
			System.out.println("The nutrition has to be more than 0 and less than 1000");
			return;
		}
		this.nutrition = nutrition;// Set's the animals nutrition to the new
									// value.
	}
	
	@Override
	public double eat(double howMuch) {
		double deltaSize = Math.min(size, howMuch / NUTRITION_FACTOR);
		//Finds the minimal value of the size and the input/nutrition factor of this object.
		size -= deltaSize;
		//The size decreases by "howMuch".
		nutrition -= howMuch*NUTRITION_FACTOR;
		//The nutrition decreases by howmuch* the nutrition factor of this object.
		if (size == 0)
			destroy();
		//if the size goes all the way down to 0, this object will die.
		return deltaSize * NUTRITION_FACTOR;
	}

	@Override
	public double getNutritionalValue() {
		return size * NUTRITION_FACTOR;
	}
	
	public IEdibleObject getClosestFood() {
		double shorttDist = 401;
		ISimObject closestObject = null;
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			
			if (obj instanceof SimHyena || obj instanceof SimWarthog || obj instanceof SimMareCat) {
				// The objects that are edible for the female lion.
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
	
	public IEdibleObject getBestFood() {
		foodLion.clear(); // Clears the arraylist, so that objects wont be added
		// more than 1 time.
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (obj instanceof SimHyena || obj instanceof SimWarthog || obj instanceof SimMareCat) {
				// The objects that are edible for the female lion.
				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);
				
				if (angle < 45 && angle > -45) {
					foodLion.add((IEdibleObject) obj);
					// Makes sure that the food only will be added to the list
					// if it's in the field of vision,
					// 45 degrees in both directions.
				}
			}
		}
		if (foodLion.size() == 0) {
			return null;
		}
		Compare compare = new Compare();
		Collections.sort(foodLion, compare);
		// The food is compared using the new compare
		return (IEdibleObject) foodLion.get(foodLion.size() - 1);
		//Returns the last object in the list, as this is the object with most food in it.
		
	}

	public static boolean getCircleOfLife(){
		return circleOfLife;
	}
	
	@Override
	public void step() {
		
		if(!circleOfLife){
		if (born) {
			hBar = 0;
			//if a cub has been born, the hBar will always be 0.
		} else {
			hBar += 0.0007;
			//The hBar will increase each time step() is called.
			if (hBar > 1) {
				hBar = 0;
				//If the bar goes over the limit of 1, it will reset to 0.
			}
		}
		steps++;
		//Increases the step counter
		nutrition -= 0.1;
		//The objects nutrition will decrease each time step() is called. 
		barValue = nutrition / 1000;
		//Calculates the value that will be shown in the bar over this objects head. 
		int hunger = hungerStatus.hungerStatus(nutrition);
		//Find out how hungry this object is. 

		if (hunger == 0) {
			IEdibleObject obj = getBestFood();
			//If the object only is moderately hungry, it will search for the best food. 
			if (obj != null) {
				dir = dir.turnTowards(super.directionTo(obj), 2.4);
				accelerateTo(1.8 * defaultSpeed, 0.3);
				if (this.distanceToTouch(obj) < 5) {
					double howMuchToEat = 1 - barValue;
					//Eats the maximum amount it can without the bar going over it's limit.
					obj.eat(howMuchToEat);
					if (barValue < 1) {
						nutrition += obj.getNutritionalValue();
						//The nutrition has the objects nutritional value added.
					}
				}
			}
		}
		if (hunger < 0) {
			IEdibleObject obj = getClosestFood();
			//If the object only is moderately hungry, it will search for the best food,
			if (obj != null) {
				dir = dir.turnTowards(super.directionTo(obj), 2.4);
				//Turns towards the food and accelerates to catch up.
				accelerateTo(1.8 * defaultSpeed, 0.3);
				if (this.distanceToTouch(obj) < 5) {
					double howMuchToEat = 1 - barValue;
					obj.eat(howMuchToEat);
					//Eats the maximum amount it can without the bar going over it's limit.
					if (barValue < 1) {
						nutrition += obj.getNutritionalValue();
						//The nutrition has the objects nutritional value added.
					}
				}
			}
		}
		if (hunger > 0 && !born) {
			for (ISimObject mate : habitat.nearbyObjects(this, getRadius() + 400)) {
				//If the female isn't hungry, and a cub hasn't been born, she will search for a Male lion to impregnated her.
				if (mate instanceof SimMaleLion) {
					if (((SimMaleLion) mate).getPregnant()) {
						habitat.addObject(new SimLionCub(this.getPosition(), habitat));
						born = true;
						circleOfLife = true;
						//Checks if the male lion has impregnated her. If he has, a cub will be born on-site (added to the habitat)
						//and sets born to be true.
					}
				}
			}
		}
		
		
		if (steps == 200) {
			Random i = new Random();
			Direction dr = dirGen.generate(i);
			dir = dir.turnTowards(dr, 15);
			steps = 0;
			//Creates a new direction every 200 steps and resets the steps counter
		}
		// go towards center if we're close to the border
		if (!habitat.contains(getPosition(), getRadius() * 1.2)) {
			dir = dir.turnTowards(directionTo(habitat.getCenter()), 5);
			if (!habitat.contains(getPosition(), getRadius())) {
				// we're actually outside
				accelerateTo(5 * defaultSpeed, 0.3);
			}
		}
		}
		else{
			SimEvent event = new SimEvent(this, "Long live the king!", this, habitat.getCenter());
			habitat.triggerEvent(event);
			harmony++;
			
				MediaHelper.getSound("circle.wav").play();
			
			if(harmony >= 5000){
				circleOfLife = false;
			}
			nutrition = 1000;
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

	@Override
	public void eventHappened(SimEvent event) {
		if (event.getType().equals("Long live the king!")) {
			for (ISimObject rock : habitat.allObjects()){
				if(rock instanceof SimRock){
					dir = dir.turnTowards(this.directionTo(rock), .7);
				}
			}
		}
	}
}
