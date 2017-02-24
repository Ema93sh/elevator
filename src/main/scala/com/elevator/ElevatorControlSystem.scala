package com.elevator

import ElevatorState._
import Direction._
import PickUpOrDrop._


object ElevatorControlSystem {

  trait ElevatorControlSystem {
    def pickUp(floor:Int, direction: Direction): Elevator
    def dropAt(floor:Int, elevator: String)
    def simulateOneStep()
  }


  case class NearestElevatorControlSystem(var elevators: List[Elevator]) extends ElevatorControlSystem {
    val MaxLevel = 16
    var timeInterval = 0

    def pad(f: Int, requestedFloors: List[(PickUpOrDrop, Int)]) = {
        (1 to 16).map(i =>
          if (i == f)  "[E]"
          else if( requestedFloors.map(_._2) contains i) {
              if (requestedFloors.find(_._2 == i).get._1 == PickUp) "[P]"
              else "[D]"
          }
          else "|~|"
        ) mkString "-"
    }


    def isSameDirection(elevator: Elevator, direction: Direction) = {
       (elevator.currentState == GoingUp && direction == Up) ||
       (elevator.currentState == GoingDown && direction == Down) ||
       (elevator.currentState == Stationary)
    }

    def pickUp(floor: Int, direction: Direction) = {
      val elevatorsInSameDirection = elevators.filter(e => isSameDirection(e, direction))
      if (elevatorsInSameDirection.isEmpty) {
        val nearestElevator = elevators.sortBy(e => (e.requestedFloors.map(_._2).max, e.requestedFloors.length)).head
        nearestElevator.requestedFloors = nearestElevator.requestedFloors :+ (PickUp, floor)
        nearestElevator
      }
      else {
        val floorDiff = elevatorsInSameDirection.map(_.floor).map(elevatorFloor => Math.abs(elevatorFloor - floor))
        val nearestElevator = elevatorsInSameDirection(floorDiff.zipWithIndex.min._2)
        nearestElevator.requestedFloors = nearestElevator.requestedFloors :+ (PickUp, floor)
        nearestElevator
      }
    }

    def dropAt(floor: Int, elevatorId: String) = {
      val elevatorToDrop = elevators.find(_.elevatorId == elevatorId).get
      elevatorToDrop.requestedFloors = elevatorToDrop.requestedFloors :+ (Drop, floor)
    }

    def simulateOneStep = {
      timeInterval += 1
      elevators = elevators.map(_.updateState)
    }

    def simulate(numberOfSteps: Int) = {
      (1 to numberOfSteps).foreach(_ => simulateOneStep)
    }

    def prettyPrint = {
      println("Time: " + timeInterval)
      elevators
        .map(elevator => pad(elevator.floor, elevator.requestedFloors) + " : " + elevator.elevatorId + " (" +  elevator.currentState + ")                   ")
        .foreach(println)
    }
  }

}
