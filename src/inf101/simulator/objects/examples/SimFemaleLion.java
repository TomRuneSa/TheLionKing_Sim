package inf101.simulator.objects.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class SimFemaleLion extends AbstractMovingObject {
	private static final double defaultSpeed = 1.5;
	private static Habitat habitat;
	private Image img = MediaHelper.getImage("sarabi.png");
	private ArrayList<IEdibleObject> foodLion = new ArrayList<>();
	private double nutrition = 1000.0;
	private double barValue = 1.0;
	private double hBar = 0.90;
	private boolean born = false;

	public SimFemaleLion(Position pos, Habitat hab) {
		super(new Direction(0), pos, defaultSpeed);
		SimFemaleLion.habitat = hab;
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.translate(0, getHeight());
		context.scale(1, -1);
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
		super.drawBar(context, barValue, 0, Color.RED, Color.BLUE);
		super.drawBar(context, hBar, 3 / 2, Color.YELLOW, Color.GREEN);
		// GraphicsHelper.strokeArcAt(context, getWidth() / 2, getHeight() / 2,
		// VIEW_DISTANCE, 0, VIEW_ANGLE);
		// context.setStroke(Color.YELLOW.deriveColor(0.0, 1.0, 1.0, 0.5));
		;
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

	public boolean getBorn() {
		return born;
	}

	@Override
	public double getHeight() {
		return 141;
	}

	@Override
	public double getWidth() {
		return 141;
	}

	public double getHBar() {
		return hBar;
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

	public double getNutrition() {
		return nutrition;
	}
	public void SetGetNutrition(double nutrition){
		if(nutrition>1000||nutrition<0){
			System.out.println("The nutrition has to be more than 0 and less than 1000");
			return;
		}
		this.nutrition = nutrition;
	}
	
	@Override
	public void step() {
		if (born) {
			hBar = 0;
		} else {
			hBar += 0.0009;
			if (hBar > 1) {
				hBar = 0;
			}
		}
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
					// SimEvent event = new SimEvent(this, "CUUUUNT", null,
					// null);
					// habitat.triggerEvent(event);
				}
			}
		}
		if (hunger > 0 && !born) {
			for (ISimObject mate : habitat.nearbyObjects(this, getRadius() + 400)) {
				if (mate instanceof SimMaleLion) {
					if (((SimMaleLion) mate).getPregnant()) {
						habitat.addObject(new SimLionCub(this.getPosition(), habitat));
						born = true;

					}

				}
			}
		}
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
