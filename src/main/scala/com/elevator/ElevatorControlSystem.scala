package com.elevator

object ElevatorControlSystem {

  trait ElevatorControlSystem {
    def pickUp(person: Person): Unit
    def updateState(): Unit
    def status: List[Elevator]
  }

  case class NearestElevatorControlSystem(private var elevators: List[Elevator]) extends ElevatorControlSystem {
    val MaxFloors = 16


    def pickUp(person: Person): Unit = {
      val nearestElevator = elevators.maxBy(_.feasibilityFunction(person.pickUp, person.direction))
      elevators = elevators.map {
        case e if e == nearestElevator => nearestElevator.addRequest(person)
        case x                         => x
      }
    }

    def status: List[Elevator] = elevators

    def updateState(): Unit = {
      elevators = elevators.map(_.updateState())
    }

  }

}
