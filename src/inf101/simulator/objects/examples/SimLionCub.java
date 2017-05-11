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

public class SimLionCub extends AbstractMovingObject {
	private static final double defaultSpeed = 1.5;
	private static Habitat habitat;
	private Image img = MediaHelper.getImage("simba.png");
	private ArrayList<IEdibleObject> foodLionCub = new ArrayList<>();
	private double nutrition = 1000.0;
	private double barValue = 1.0;
	private int steps = 0;
	private static DirectionGenerator dirGen = new DirectionGenerator();

	public SimLionCub(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
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

			if (obj instanceof SimWarthog || obj instanceof SimMareCat || obj instanceof SimInsect) {
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

	public IEdibleObject getBestFood() {
		foodLionCub.clear();
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (obj instanceof SimWarthog || obj instanceof SimMareCat || obj instanceof SimInsect) {
				
				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);
				
				if (angle < 45 && angle > -45) {
					foodLionCub.add((IEdibleObject) obj);
				}
			}
		}
		if (foodLionCub.size() == 0) {
			return null;
		}
		Compare compare = new Compare();
		Collections.sort(foodLionCub, compare);
		return (IEdibleObject) foodLionCub.get(foodLionCub.size() - 1);
		
	}

	@Override
	public double getHeight() {
		return 90;
	}
	
	@Override
	public double getWidth() {
		return 90;
	}

	@Override 
	public void step() {
		
		boolean follow = false;
		nutrition -= 0.2;
		barValue = nutrition / 1000;
		int hunger = hungerStatus.hungerStatus(nutrition);		
		
		for (ISimObject danger : habitat.allObjects()) {
			if (danger instanceof SimHyena) {
				if (distanceTo(danger) < 200) {
					Direction dir1 = directionTo(danger);
					Direction dir2 = dir1.turnBack();
					dir = dir.turnTowards(dir2, 2.2);
				}
			}
		}
		
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
					// SimEvent event = new SimEvent(this, "CUUUUNT", null,
					// null);
					// habitat.triggerEvent(event);
				}
			}
		}
		if (hunger > 0) {
			for (ISimObject father : habitat.nearbyObjects(this, getRadius() + 400)) {
				if (father instanceof SimMaleLion) {
					double simRepAngle = this.getPosition().directionTo(father.getPosition()).toAngle();
					double simAngle = this.getDirection().toAngle();
					double angle = angleFix(simRepAngle, simAngle);

					if (angle < 45 && angle > -45) {
						follow = true;
						dir = dir.turnTowards(super.directionTo(father.getPosition()), 2.1);
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
