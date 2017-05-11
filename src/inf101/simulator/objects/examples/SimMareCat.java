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
	private ArrayList<IEdibleObject> foodBird = new ArrayList<>();
	private double nutrition = 1000.0;
	private double barValue = 1.0;
	private int steps = 0;
	private static DirectionGenerator dirGen = new DirectionGenerator();
	
	public SimMareCat(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		SimMareCat.habitat = hab;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		if(-90 < super.getDirection().toAngle() && super.getDirection().toAngle() < 90){
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
		return 50;
	}

	public IEdibleObject getBestFood() {
		foodBird.clear();
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (obj instanceof SimInsect) {

				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
					foodBird.add((IEdibleObject) obj);
				}
			}
		}
		if (foodBird.size() == 0) {
			return null;
		}
		Compare compare = new Compare();
		Collections.sort(foodBird, compare);
		return (IEdibleObject) foodBird.get(foodBird.size() - 1);

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

	@Override
	public void step() {
		
		boolean follow = false;
		
		steps++;
		nutrition -= 0.2;
		barValue = nutrition / 1000;
		int hunger = hungerStatus.hungerStatus(nutrition);

		for (ISimObject danger : habitat.allObjects()) {
			if (danger instanceof SimHyena) {
				if (distanceTo(danger) < 300) {
					Direction dir1 = directionTo(danger);
					Direction dir2 = dir1.turnBack();
					dir = dir.turnTowards(dir2, 2.2);
					accelerateTo(1.8 * defaultSpeed, 0.3);
				}
			}
		}
		
		if (hunger == 0) {
			IEdibleObject obj = getBestFood();
			if (obj != null) {
				dir = dir.turnTowards(super.directionTo(obj), 2);
				accelerateTo(1.6 * defaultSpeed, 0.3);
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
				if (this.distanceToTouch(obj) < 5) {
					double howMuchToEat = 1 - barValue;
					obj.eat(howMuchToEat);
					if (barValue < 1) {
						nutrition += obj.getNutritionalValue();
					}
//					SimEvent event = new SimEvent(this, "iii", null, null);
//					habitat.triggerEvent(event);

				}
			}
		}
		if(hunger > 0){
		for (ISimObject mate : habitat.allObjects()) {
			if (mate instanceof SimWarthog) {
				if (distanceTo(mate) < 400) {
					follow = true;
					dir = dir.turnTowards(super.directionTo(mate.getPosition()), 2.1);
				}
			}
		}
		}
		if(steps == 200 && !follow){
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
	

}
