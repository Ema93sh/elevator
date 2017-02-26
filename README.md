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

* Elevator which is closer in distance to the floor.

* If no Elevator is in the same direction as the caller direction then choose the elevator based on which has the smaller number of request.

* It can also be extend to use capacity of the elevator as part of the heuristic.

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
* FCFS would be the worst performing Scheduling algorithm in this case because it wont choose the optimal request
to process instead it would just choose the first request that it came across.
* By Choosing the closest elevator that could be assigned to the person it would drastically reduce the waiting
time of the algorithm.
