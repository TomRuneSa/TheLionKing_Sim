# Oversikt

Student:
Tom Rune Sæverås
Yap006
tomrunesaeveras@hotmail.no

## Om funksjonaliteten

Common for all moving objects is their healthbar. Every object this accounts for has a maximum nutrition of 1000, which decreases a certain amount for each time step() is called. The nutrition is sent to "hungerStatus" to decide how hungry the object is. "hungerStatus" returns 1 for full, 0 for hungry, and -1 for starving. This decides how the object should behave. 

###Rules of the program:
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
		- The warthog only walks around trying to avoid hyenas and male/female lions when it's not hungry. If it is 		hungry, it looks for food. The only food it'll eat is insects. 
		
- Marecat
		- The marecat is quite similar to the warthog when it comes to eating. If the marecat isn't looking for food 		it searches for the warthog, as the marecat think that the warthog can protect it. But the warthog does 		nothing to protect the marecat. 
		
- Insect
		- The insects does nothing but being eaten. The insect just walks around with no meaning and doesn't try to avoid anything. 
		
- Bird
		- The bird looks for the male lionBut once the bird gets hungry, it strays away from the lion and looks for 		food. 
		
- Hyena
		- The hyena is the most dangerous animal after the lions. It can eat anything. But it doesn't eat insect, 		because it doesn't like them, and it doesn't eat the lion cub, because it doesn't hurt children.. If the hyena isn't 		hungry, it will look for other hyenas, because hyenas are stronger in a pack. If a hyena is starving, it'll get 		desperate enough to go after a lion, if the pack has 3 or more members.
		
- Bananatree
		- The bananatree will grow a certain amount each step, and when it's big enough, it will give a banana.
		
- Banana
		- The banana exists when the bananatree is fully grown. The monkey eats the bananas.

## Beskrivelse av systemet  

###Common methods:

-The tasks that was given from 1.1 to 1.6 was discused and solved in cooperation with: 
-Markus Mahlum
-Rune Vatne
-Toerres Angeltveit



-When the lion cub is born, the female will trigger an event that tells all listeners to go towards the center of the habitat. In this case, all the listeners are all movable objects except insects, male/female lions and the lion cub. The lions will turn towards the rock.
When the cub is born, a step counter starts, and as long as that counter is under a certain value, all animals will live in harmony, and will have full nutrition. When the counter reaches the top value, behavior will return to normal.


- getClosestFood(): 
	-Creates an object that goes through the habitat in a circuit of radius + 400. If the objects hits an object that is 	edible for the animal, it will check if it's within field of vision. As long as it is, it will check if it's the closest object that has been found so far. If it is, that's the IDibleObject that will be returned.

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
	-If the lion is full, it will just walk around aimlessly. Unless its horny, then it will 	search for a female to 	impregnate. As long as there hasn't been born a cub, it will get 	"hornier" each step. If it gets to horny without 	impregnating a female, it will blow of 	steam and the bar will clear. 
	If the lion is moderately hungry, it will search for warthogs, marecats and hyenas to eat. 	It will go for the best food it can see. If the lion is starving, it will search for the 	same animals to eat, but instead of going for the 	best one, it will go for the closest 	one so it'll get food as fast as possible.

-SimFemaleLion
	- If the lion is full, it will just walk around aimlessly. Unless it gets impregnated, 	then it will give birth to a 	cub. This will only happen once.
	If the lion is moderately hungry, it will search for warthogs, marecats and hyenas to eat. 	It will go for the best 	food it can see. If the lion is starving, it will search for 	the same animals to eat, but instead of going for the 	best one, it will go for the 	closest one so it'll get food as fast as possible.

-Cub
	- When the cub is born, it'll be born without beeing hungry, a mere miracle. As long as 	he's not hungry, he will by default look for his father, and follow him. But if he is 	following his father, and gets hungry, he will stray away so that he can look for food. 	The cub eats warthogs, marecats and insects. The order of eating is the same as the male 	and female lion. If hes moderately hungry, he'll go for the closest food, if hes starving 	he'll go for the closest food. The cub cannot be eaten by anyone, as no one will hurt a 	cub. 
	
- Monkey
	- If the monkey is full, he will walk around aimlesly, and try to avoid hyenas. If he's 	not full, he'll look for bananas. 
	
- Warthog
	- The warthog is kind of stupid, he only thinks about two things: Food, and avoid beeing 	eaten by hyenas or lions. When he's not hungry he'll walk around minding his own business. 	When he does get hungry, he'll go looking for insects to eat.
	
- Marecat
	- The marecat is always trying to follow the warthog as long as the marecat isn't hungry. 	He searches for the warthog as far as he can see, and then follows him until he gets 	hungry. 	When its hungry it'll stray away from the warthog and go look for insects to 	eat. He will try 	to aviod hyenas and lions to the best of his abilities. 

-Insect
	- The insect does nothing but get eaten. It just wanders around the habitat. 
	
-Bird
	- If the bird isn't hungry, it will try to find the male lion and follow it for 	protection. If it is hungry though, it will stop following the lion and go look for 	insects to eat. The bird can get eaten by hyenas. 
	
-Hyena
	- The hyena fears nothing except the lions, which they will try to avoid. Allthough, if a 	member of a hyena pack that has 3 members or more is starving, it will call on the rest of 	the pack and attack the lion for food. 
	If it's starving or is only moderately hungry it'll eat marecats, warthogs or birds. The 	hyena will not eat the lion cub, as they won't hurt children.  
	
-Bananatree
	- The bananatree will spawn at random positions, and for each step, it will grow. When 	it's fully grown, it will turn into a banana. 

- Banana
	- The banana is a result of the bananatree being fully grown, and will spawn on the 	position

## Svar på spørsmål

1. The Position.move() takes in a direction in which the object should move, and a distance of which the object should move. If the distance is equals to zero, which means that there isn't any longer to move the object, it returns the position it's in before it was asked to move "0 distance". If not it moves the object in the direction that's asked, and returns the new position, by making a new position using the Position.makePos() method. 
It's different from the Position class in lab5 because in lba5 it uses private double values, that's changed continuously, instead of making new positions. 

2. The difference between AbstractSimObject and AbstractMovingObject is that SimObject is the class that is moving the object. MovingObject figures out the spees of which the object should be moving in. 

3. The other sub-classes can change the position by calling public- methods to change the position.

4. directionTo calculates a direction towards another position. distanceTo calculates the distance to another position. 

5. I would say that it would be smart to make the speed-variable private, because otherwise other methods and class can change it's value, even though it's originally set as protected. It can still be changed even though it's "protected".

6.  To change the direction of an object, one have to call on the methods turnTowards, turnRight/turnLeft/turnBack. Or one can call turn(), which returnes a new direction
 An example: dir = dir.turnTowards(direction, angle); The object will then move towards the direction that is asked. 
 
From what I can tell, there already are methods to change direction, as mentioned above. turnLeft/Back/Left.


## Kilder til media

* Rammeverkkode: © Anya Helene Bagge (basert på tidligere utgaver, laget av Anya Helene Bagge, Anneli Weiss og andre).

* pipp.png, bakgrunn.png © Anya Helene Bagge, This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License

### None of the pictures I have used in this program has been made by me, and I do not take any form of credit for them. 

*banana.png --> https://australianbananas.com.au/Images/Home/RipenessBlend.png

*bananatree.png --> https://vignette1.wikia.nocookie.net/farmville/images/5/5f/Banana1-icon.png/revision/latest?cb=20101115203830

*bird1.png--> https://opengameart.org/sites/default/files/Untitled_6.png

*bug.png --> https://opengameart.org/sites/default/files/SSAG_beetle_logo_by_shiroikuro.jpg

*hyena.png --> https://s-media-cache-ak0.pinimg.com/736x/0d/07/e5/0d07e5cabddb9d523f690e114129ff68.jpg

*mufasa.png--> https://vignette4.wikia.nocookie.net/disney/images/d/d3/Mufasa_KHII.png/revision/latest/scale-to-width-down/250?cb=20151105010746

*pumba.png --> https://vignette2.wikia.nocookie.net/p__/images/1/10/Pumba.png/revision/latest?cb=20150311142045&path-prefix=protagonist

*rafiki.png --> http://vignette1.wikia.nocookie.net/disney/images/9/9e/Rafikidisney.jpeg/revision/latest?cb=20120730031111

*rock.png --> http://vignette1.wikia.nocookie.net/disney/images/4/47/Pride-Rock-Dawn-%28The_Lion_King%29.jpg/revision/latest?cb=20120403232025

*sarabi.png --> https://s-media-cache-ak0.pinimg.com/originals/6d/e8/2e/6de82e6d3dd0208971ef95169c0a8095.png

*savannah.png --> https://pixabay.com/p-1326836/?no_redirect

*simba.png --> https://vignette3.wikia.nocookie.net/parody/images/c/cd/Young_simba_lion_king.png/revision/latest?cb=20150125233927

*timon.png --> https://vignette3.wikia.nocookie.net/disney/images/3/32/Timon-icon.png/revision/latest?cb=20130818140155


###Music:
*The timeless classic "Circle of life", originally made by Elton John, here covered -->
http://musicpleer.audio/#!5db78a6e8b7047156ac37109dfa3e2f1


 














