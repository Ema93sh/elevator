package com.elevator

import ElevatorEnums.Direction._
import ElevatorEnums.ElevatorState._
import ElevatorEnums.RequestType._

object ElevatorControlSystem {

  trait ElevatorControlSystem {
    def pickUp(floor:Int, direction: Direction): Elevator
    def dropAt(floor:Int, elevator: String)
    def updateState()
    def getElevators: List[Elevator]
  }


  case class NearestElevatorControlSystem(private var elevators: List[Elevator]) extends ElevatorControlSystem {
    val MaxFloors = 16

    def pickUp(floor: Int, direction: Direction) = {
      val nearestElevator = elevators.maxBy(_.feasibilityFunction(floor, direction))
      nearestElevator.addRequest(Request(PickUp, floor))
      nearestElevator
    }

    def dropAt(floor: Int, elevatorId: String) = {
      val elevatorToDrop = elevators.find(_.elevatorId == elevatorId).get
      elevatorToDrop.addRequest(Request(Drop, floor))
    }

    def getElevators = elevators

    def updateState = {
      elevators = elevators.map(_.updateState)
    }

  }

}
