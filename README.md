Elevator Scheduling:
====================

To Run the simulation
---------------------

```

sbt run

```

Run Tests
---------
```

sbt test

```



Algorithm:
==========

Heuristic
---------

The main idea behind my algorithm is choosing the elevator that is closest to the floor that has called for the elevator. The closeness should also consider the elevator direction. The priority for closeness is:

* Elevator is in the same direction as the caller request.

* Elevator which is closer in distance to the floor

* If no Elevator is in the same direction as the caller direction then choose the elevator based on which has the smaller number of request

* It can also be extend to use capacity of the elevator as part of the heuristic

Analysis:
--------
The flowing could be used to measure the performance of the heuristic
* Average waiting time pickup request
* Average waiting time for drop request
* Max waiting time for pickup request (This can be used to make sure no one is stuck in one place for long time)
* Max waiting time for drop request
* Total Time to finish all the request

Comparison to FCFS:
-------------------
* Nearest Scheduling would reduce the average wait time for the a pickup request when compared
to the FCFS because it always chooses the optimal elevator to pickup the person at the floor.



Code:
======
App
---
The main app which runs the simulation, it also prints out to the console for each time of the simulation.

Elevator
-------
This contains the elevator class which holds the request and current state of the elevator and a update method which will move the elevator to next state based on the request and current state of the elevator.

Elevator State
--------------
This file holds the enums such as Direction and ElevatorState which are used in the app

Elevator Control System
----------------------
This class is the global system that distributes the request to the elevator based on the heuristics. It also has some helper methods to pretty print.

TODO:
=====
* **(Code Cleanup)** Create a cost function which gives values to each elevator based on the closeness, direction and assign the elevator with min cost

* **(Incomplete)** Right now checks only the head of the request queue and opens the door if it reaches the floor. Ideally it should check the entire request queue and see if current floor is part of the request and open. This will drastically improve the performance of the algorithm.

* **(Bug)** If the elevator is already at the floor and request occurs at the floor then add the request to that elevator


* Poor encapsulation. Elevator has mutable state that other entities are modifying. Display concerns tangled with control strategy.
* Odd mix of immutability / mutability. He sort of gets the idea of functional programming... but something is missing. There doesn't seem to be any pattern to when he chooses to mutate or not, and, he gets it wrong.
* Odd use of pattern matching. nextFloorAndState method, for example, is a completely counter-productive use of it. He may not have realized he could just assign those to vals and then use them in the pattern matching conditions still. Probably just an artifact of being new to Scala.
* odd placement of functionality resulting in unnecessary dependencies. For example, isSameDirection only needs the currentState; it either should've been placed in the elevator class itself, or, placed in a helper module that only took the necessary data to do the job. It'd be really great to find a way to use an implicit Ordering.
* the algorithm implementation is much more complex than it needs to be.
* counter-productive use of tuple positional accessors instead of case class with helpful field names
* algorithm has issues. On initial state (all floors at level 1), pickup 10 going up, then pickup 8 going up. Elevator 1 is assigned to service both requests, but it skips level 8, going to level 10, first.
