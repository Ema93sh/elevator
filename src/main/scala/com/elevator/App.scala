package com.elevator

import scala.io._

import ElevatorControlSystem._
import ElevatorState._
import Direction._
import PickUpOrDrop._

object Application extends App {
   var status = "Starting Simulation"

   def clearConsole(count: Int) {
     print("\033[%dA".format(count));
     print("\033[K"*count)
   }

   def randomPickUpAndDrop(controlSystem: ElevatorControlSystem) {
     val r = scala.util.Random
     var randomFloor = Math.abs(r.nextInt()) % 16 + 1
     val dirIndex = Math.abs(r.nextInt()) % 2
     val randomDirection = List(Up, Down)(dirIndex)
     val assignedElevator = controlSystem.pickUp(randomFloor, randomDirection)
     var randomDropLocation = 1


     //TODO clean up
     if (randomDirection == Up) {
       val range = randomFloor to 16
       randomDropLocation = range(r.nextInt(range.length))
     }
     else {
       val range = 1 to randomFloor
       randomDropLocation = range(r.nextInt(range.length))
     }


     //TODO drop should be called after the pick up
     controlSystem.dropAt(randomDropLocation, assignedElevator.elevatorId)
     status = "Pick up at " + randomFloor + " and drop at " + randomDropLocation + " ---"
   }

   override def main(args: Array[String]) {

     val elevatorAlpha    = Elevator("alpha", 1, GoingUp, List())
     val elevatorBeta     = Elevator("beta", 1, GoingUp, List())
     val elevatorCharlie  = Elevator("charlie", 1, GoingUp, List())
     val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

     val elevatorControlSystem = NearestElevatorControlSystem(elevators)

     val simulationTime = 1000
     status = "Starting Simulation"

     elevatorControlSystem.prettyPrint
     println("Status Update:" + status)
     println("")

     (1 to 1000).foreach( currentTime => {

       Thread.sleep(200)
       clearConsole(6)

       elevatorControlSystem.simulate(1)

       elevatorControlSystem.prettyPrint
       println("Status Update:" + status)
       println("")

       if (currentTime % 10 == 0) {
         randomPickUpAndDrop(elevatorControlSystem)
       }
     })
   }
}
