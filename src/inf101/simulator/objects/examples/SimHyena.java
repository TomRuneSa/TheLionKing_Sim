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
	private static final double defaultSpeed = 1.5;
	private static Habitat habitat;
	private static final double DIAMETER = 40;
	private static final double NUTRITION_FACTOR = 100;
	private double size = 1.0;
	private Image img = MediaHelper.getImage("hyena.jpg");
	private ArrayList<IEdibleObject> foodHyena = new ArrayList<>();
	private double nutrition = 1000.0;
	private double barValue = 1.0;
	private int packSize = 1;
	private int steps = 0;
		
	private static DirectionGenerator dirGen = new DirectionGenerator();
//-90< super.getDirection().toAngle
	public SimHyena(Position pos, Habitat hab, double size) {
		super(new Direction(0), pos, defaultSpeed);
		this.habitat = hab;
		this.size = size;
		this.habitat.addListener(this, this);
	}

	@Override
	public void draw(GraphicsContext context) {
		super.draw(context);
		context.translate(0, getHeight());
		context.scale(1, -1);
		context.drawImage(img, 1.0, 0.0, getWidth(), getHeight());
		super.drawBar(context, barValue, 0, Color.RED, Color.BLUE);
	}

	public IEdibleObject getClosestFood() {
		double shorttDist = 401;
		ISimObject closestObject = null;
		for (ISimObject food : habitat.nearbyObjects(this, getRadius() + 400)) {

			if (food instanceof SimMareCat || food instanceof SimWarthog || food instanceof SimMonkey || food instanceof SimBird) {
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
	}

	@Override
	public double getHeight() {
		return DIAMETER * size;
	}

	@Override
	public double getWidth() {
		return DIAMETER * size;
	}
	public int GetPackSize(){
		packSize = 1;
		for (ISimObject mate : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (mate instanceof SimHyena) {
				packSize++;	
			}
		}
		return packSize;
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

	
	public IEdibleObject getBestFood() {
		foodHyena.clear();
		for (ISimObject obj : habitat.nearbyObjects(this, getRadius() + 400)) {
			if (obj instanceof SimMareCat || obj instanceof SimWarthog || obj instanceof SimMonkey || obj instanceof SimBird) {

				double simRepAngle = this.getPosition().directionTo(obj.getPosition()).toAngle();
				double simAngle = this.getDirection().toAngle();
				double angle = angleFix(simRepAngle, simAngle);

				if (angle < 45 && angle > -45) {
					foodHyena.add((IEdibleObject) obj);
				}
			}
		}
		if (foodHyena.size() == 0) {
			return null;
		}
		Compare compare = new Compare();
		Collections.sort(foodHyena, compare);
		return (IEdibleObject) foodHyena.get(foodHyena.size() - 1);

	}
	
	public IEdibleObject getLions(){
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
	}
	
	

	
	@Override
	public void step() {
		steps++;
		int pack = GetPackSize();
		nutrition -= 0.2;
		barValue = nutrition / 1000;
		int hunger = hungerStatus.hungerStatus(nutrition);
		if(pack<3){
		for (ISimObject run : habitat.allObjects()) {
			if (run instanceof SimMaleLion || run instanceof SimFemaleLion) {
				if (distanceTo(run) < 500) {
					Direction dir1 = directionTo(run);
					Direction dir2 = dir1.turnBack();
					dir = dir.turnTowards(dir2, 2.2);
				}
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
					 SimEvent event = new SimEvent(this, "Nam", this, this.getPosition());
					 habitat.triggerEvent(event);
					 
				}
			}
		}
		if (hunger > 0) {
			for (ISimObject mate : habitat.nearbyObjects(this, getRadius() + 400)) {
				if (mate instanceof SimHyena) {
					double simRepAngle = this.getPosition().directionTo(mate.getPosition()).toAngle();
					double simAngle = this.getDirection().toAngle();
					double angle = angleFix(simRepAngle, simAngle);

					if (angle < 45 && angle > -45) {			
						dir = dir.turnTowards(super.directionTo(mate.getPosition()), 2.1);
				}
			}
		}
		}
		if(hunger < 0 && pack>=3){
			 
			IEdibleObject obj = getLions();
			if (obj != null) {
				
				SimEvent event = new SimEvent(this, "Nam", this, obj.getPosition());
				 habitat.triggerEvent(event);
				 
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
	public void eventHappened(SimEvent event) {
		if(event.getType().equals("Nam")){
			dir = dir.turnTowards(directionTo((Position)event.getData()), 2.5);
		}
		
	}

}
