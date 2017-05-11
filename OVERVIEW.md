# Oversikt

Student:
Tom Rune Sæverås
Yap006
tomrunesaeveras@hotmail.no

## Om funksjonaliteten

Common for all moving objects is their healthbar. Every object this accounts for has a maximum nutrition of 1000, which decreases a certain amount for each time step() is called. The nutrition is sent to "hungerStatus" to decide how hungry the object is. "hungerStatus" returns 1 for full, 0 for hungry, and -1 for starving. This decides how the object should behave. 

Rules of the program:
- Lions
	- Male
		- If the male lion is full, he moves around aimlessly. Both the male and female lion has a bar which decides 		how ready they are to mate. As long as the male is full, and the mating bar is within the range of being ready 		to mate, the male lion will search for a female lion within the field of vision. If he finds one, he will 		approach and chech if she is ready to mate as well. 
		If the male is hungry, he will check within his range of vision if there is any edible objects. The lion eats 		warthogs, marecats and hyenas. As long as he's just hungry he'll just go for the food that he sees as the best 		food, and ignore the other food.
		If the male is starving, he will eat the closest possible food within his field of vision. 
		If the male is either hungry or starving, he won't care about mating, only for food.
		
- Female
		- The female lion has the mostly the same traits as the male. The exception is that when the female is ready 		to mate, she doesn't go searching for a male, she let's the male come to her. If she get's impregnated, she 		will "give birth" to a lion cub. However, she will only do this as long as she's not hungry or starving. The 		female lion is more independent than the male, as she doesn't have anyone following her. 
		
- Cub
		- The cub does not spawn from the beginning, it only appears when the male and female lion is ready for it. 		When the cub is born, it will try to find it's father. If it finds the father, it will follow the father 		until it get's hungry, then it will break away from the father and look foor food. It has the same eating 		traits as its mother and father, except it doesn't eat hyenas, but insect in addition to warthogs and 		marecats.
		
- Monkey 
		- As long as the monkey isn't hungry or starving it'll just walk around aimlessly, but try to avoid hyenas, 		since hyenas can eat the monkey. When the monkey is hungry or starving, it'll search for bananas and eat them.
		 
- Warthog
		- The warthog only walks around trying to avoid hyenas and male/female lions when it's not hungry. If it is 		hungry, it looks for food. The only food it'll eat is insects. If the marecat dies, the warthog will, out of 		grief, commit suicide.
		
- Marecat
		- The marecat is quite similar to the warthog when it comes to eating. If the marecat isn't looking for food 		it searches for the warthog, as the marecat think that the warthog can protect it. But the warthog does 		nothing to protect the marecat. If the warthog dies, the marecat will commit suicide to avoid beeing eaten.
		
- Insect
		- The insects does nothing but being eaten. The insect just walks around with no meaning and doesn't try to 		avoid anything. 
		
- Bird
		- The bird looks for the male lionBut once the bird gets hungry, it strays away from the lion and looks for 		food. 
		
- Hyena
		- The hyena is the most dangerous animal after the lions. It can eat anything. But it doesn't eat insect, 		because it doesn't like them, and it doesn't eat the lion cub, in fear of the male lion. If the hyena isn't 		hungry, it will look for other hyenas, because hyenas are stronger in a pack. If a hyena is starving, it'll get 		desperate enough to go after a lion, if the pack has 3 or more members.
		
- Bananatree
		- The bananatree will grow a certain amount each step, and when it's big enough, it will give a banana.
		
- Banana
		- The banana exists when the bananatree is fully grown. The monkey eats the bananas.

## Beskrivelse av systemet  

###Common methods:
- getClosestFood(): 
	-Creates an object that goes through the habitat in a circuit of radius + 400. If the objects hits an object that is 	edible for the animal, it will check if it's within field of vision. As long as it is, it will check if it's the 	closest object that has been found so far. If it is, that's the IDibleObject that will be returned.

- getBestFood(): 
	-Creates an object that goes through the habitat in a circuit of radius + 400. If the objects hits an object that is 	edible for the animal, it will check if it's within field of vision. If it is, the object will be added to an 	arrayList that has been created for this. This list will always be cleared every time this method is called. If 	there 	are no objects in the list, null will be returned. A new compare is then created. The list is then sorted 	with the 	help of the compare that was just created. The list is sorted from smallest to biggest, so the 	EdibleObject that is 	returned is the last object in the list.
	
- setGetNutrition()
	- A method that allows other classes to change the nutrition value of this object. It takes in a value, and sets the 	nutrition value of the object to the value it gets as input. I made this method so that it'll be 	easier for 	testing.

- GetNutrition()
	- A method that allows other classes to see the nutrition of another object. I made this method so that it'll be 	easier for testing.
	
- Class: HungerStatus
	- A class that I created so that I could find out how hungry any animal was at any given time. It takes in the 	nutrition value of the animal, returns 1 for full, 0 for hungry, and -1 for starving. This way it's easier to decide 	how an animal should behave based on how hungry it is.

###Step-methods:

*Every step() method will be described in short here. For more details please see the step() method of the object you wish to look at.*

-SimMaleLion
	-If the lion is full, it will just walk around aimlessly. Unless its horny, then it will search for a female to 	impregnate. As long as there hasn't been born a cub, it will get "hornier" each step. If it gets to horny without 	impregnating a female, it will blow of steam and the bar will clear. 
	If the lion is moderately hungry, it will search for warthogs, marecats and hyenas to eat. It will go for the best 	food it can see. If the lion is starving, it will search for the same animals to eat, but instead of going for the 	best one, it will go for the closest one so it'll get food as fast as possible.

-SimFemaleLion
	- If the lion is full, it will just walk around aimlessly. Unless it gets impregnated, then it will give birth to a 	cub. This will only happen once.
	If the lion is moderately hungry, it will search for warthogs, marecats and hyenas to eat. It will go for the best 	food it can see. If the lion is starving, it will search for the same animals to eat, but instead of going for the 	best one, it will go for the closest one so it'll get food as fast as possible.
-Cub
	-

## Svar på spørsmål
1. The Position.move() takes in a direction in which the object should move, and a distance of which the object should move. If the distance is equals to zero, which means that there isn't any longer to move the object, it returns the position it's in before it was asked to move "0 distance". If not it moves the object in the direction that's asked, and returns the new position, by making a new position using the Position.makePos() method. 
It's different from the Position class in lab5 because in lba5 it uses private double values, that's changed continuously, instead of making new positions. 

2. The difference between AbstractSimObject and AbstractMovingObject is that SimObject is the class that is moving the object. MovingObject figures out the spees of which the object should be moving in. 

3. The other sub-classes can change the position by calling public- methods to change the position.

4. directionTo calculates a direction towards another position. distanceTo calculates the distance to another position. 

5. I would say that it would be smart to make the speed-variable private, because otherwise other methods and class can change it's value, even though it's originally set as protected. It can still be changed even though it's "protected".

6.  


## Kilder til media

* Rammeverkkode: © Anya Helene Bagge (basert på tidligere utgaver, laget av Anya Helene Bagge, Anneli Weiss og andre).

* pipp.png, bakgrunn.png © Anya Helene Bagge, This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License
