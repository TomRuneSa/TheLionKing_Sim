package inf101.simulator.objects.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import inf101.simulator.Direction;
import inf101.simulator.GraphicsHelper;
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

public class SimMaleLion extends AbstractMovingObject implements IEdibleObject {
	private static final double defaultSpeed = 1.5;
	private static Habitat habitat;
	private Image img = MediaHelper.getImage("mufasa.png");
	private ArrayList<IEdibleObject> foodLion = new ArrayList<>();
	private static final double DIAMETER = 40;
	private static final double NUTRITION_FACTOR = 100;
	private double size = 1.0;
	private double nutrition = 1000.0;
	private double barValue = 1.0;
	private double hBar = 0.91;
	private boolean impregnate = false;
	private boolean done = false;
	private int steps = 0;
	private static DirectionGenerator dirGen = new DirectionGenerator();

	public SimMaleLion(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
	}
	
	public void SetGethBar(double value){
		if(value>100 || value<0){
			System.out.println("The value has to be between 0 and 100");
			return;
		}
		this.hBar = value;
	}
	public double getHBar() {
		return hBar;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.translate(0, getHeight());
		context.scale(1, -1);
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
		super.drawBar(context, barValue, 0, Color.RED, Color.BLUE);
		super.drawBar(context, hBar, 3 / 2, Color.YELLOW, Color.GREEN);
	}

	public boolean getPregnant() {
		return impregnate;
	}

	public IEdibleObject getClosestFood() {
		double shorttDist = 401;
		ISimObject closestObject = null;
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {

			if (obj instanceof SimHyena || obj instanceof SimWarthog || obj instanceof SimMareCat) {
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
		return 141;
	}

	@Override
	public double getWidth() {
		return 141;
	}
	
	public void SetGetNutrition(double nutrition){
		if(nutrition>1000||nutrition<0){
			System.out.println("The nutrition has to be more than 0 and less than 1000");
			return;
		}
		this.nutrition = nutrition;
	}
	
	public double getNutrition() {
		return nutrition;
	}

	public IEdibleObject getBestFood() {
		foodLion.clear();
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (obj instanceof SimHyena || obj instanceof SimWarthog || obj instanceof SimMareCat) {

				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
					foodLion.add((IEdibleObject) obj);
				}
			}
		}
		if (foodLion.size() == 0) {
			return null;
		}
		Compare compare = new Compare();
		Collections.sort(foodLion, compare);
		return (IEdibleObject) foodLion.get(foodLion.size() - 1);

	}

	@Override
	public void step() {
		if (done) {
			hBar = 0;
		} else {
			hBar += 0.0009;
			if (hBar > 1) {
				hBar = 0;
			}
		}
		steps++;
		nutrition -= 0.1;
		barValue = nutrition / 1000;
		int hunger = hungerStatus.hungerStatus(nutrition);

		if (hunger == 0) {
			IEdibleObject obj = getBestFood();
			if (obj != null) {
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
		if (hunger < 0) {
			IEdibleObject obj = getClosestFood();
			if (obj != null) {
				dir = dir.turnTowards(super.directionTo(obj), 2.4);
				accelerateTo(1.8 * defaultSpeed, 0.3);
				if (this.distanceToTouch(obj) < 5) {
					double howMuchToEat = 1 - barValue;
					obj.eat(howMuchToEat);
					if (barValue < 1) {
						nutrition += obj.getNutritionalValue();
					}
					// SimEvent event = new SimEvent(this, "Yum", null,
					// null);
					// habitat.triggerEvent(event);
				}
			}
		}
		if (hunger > 0 && (hBar > 0.85 && hBar < 1.00)) {
			for (ISimObject mate : habitat.nearbyObjects(this, getRadius() + 400)) {
				if (mate instanceof SimFemaleLion) {
					double simRepAngle = this.getPosition().directionTo(mate.getPosition()).toAngle();
					double simAngle = this.getDirection().toAngle();
					double angle = angleFix(simRepAngle, simAngle);

					if (((SimFemaleLion) mate).getHBar() > 0.85 && ((SimFemaleLion) mate).getHBar() < 1.00) {
					if (angle < 45 && angle > -45) {
						dir = dir.turnTowards(super.directionTo(mate.getPosition()), 2.5);
						if(this.distanceToTouch(mate) < 5){
							impregnate = true;
							if(((SimFemaleLion) mate).getBorn()){
								done = true;
								
							}
						}
					}
					}
				}
			}
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

		if (nutrition < 0.1) {
			super.destroy();
		}
		super.step();
	}

	public double angleFix(double a, double b) {
		double angle = ((((a - b) % 360) + 540) % 360) - 180; // stackoverflow
		return angle;

	}

	@Override
	public double eat(double howMuch) {
		double deltaSize = Math.min(size, howMuch / NUTRITION_FACTOR);
		size -= deltaSize;
		nutrition -= howMuch*NUTRITION_FACTOR;
		if (size == 0)
			destroy();
		return deltaSize * NUTRITION_FACTOR;
	}
	

	@Override
	public double getNutritionalValue() {
		return size * NUTRITION_FACTOR;
	}

}
