# Oversikt

Student:
Tom Rune Sæverås
Yap006
tomrunesaeveras@hotmail.no

## Om funksjonaliteten

## Svar på spørsmål
1. The Position.move() takes in a direction in which the object should move, and a distance of which the object should move. If the distance is equals to zero, which means that there isn't any longer to move the object, it returns the position it's in before it was asked to move "0 distance". If not it moves the object in the direction that's asked, and returns the new position, by making a new position using the Position.makePos() method. 
It's different from the Position class in lab5 because in lba5 it uses private double values, that's changed continuously, instead of making new positions. 

2. The difference between AbstractSimObject and AbstractMovingObject is that SimObject is the class that is moving the object. MovingObject figures out the spees of which the object should be moving in. 

3. The other sub-classes can change the position by calling public- methods to change the position.

4. directionTo calculates a direction towards another position. distanceTo calculates the distance to another position. 

5. I would say that it would be smart to make the speed-variable private, because otherwise other methods and class can change it's value, even though it's originally set as protected. It can still be changed even though it's "protected". 


## Kilder til media

* Rammeverkkode: © Anya Helene Bagge (basert på tidligere utgaver, laget av Anya Helene Bagge, Anneli Weiss og andre).

* pipp.png, bakgrunn.png © Anya Helene Bagge, This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License
