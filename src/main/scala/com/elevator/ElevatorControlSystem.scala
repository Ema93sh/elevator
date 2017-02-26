package com.elevator

import ElevatorEnums.Direction._
import ElevatorEnums.ElevatorState._
import ElevatorEnums.PersonState._

object ElevatorControlSystem {

  trait ElevatorControlSystem {
    def pickUp(person: Person)
    def updateState()
    def status: List[Elevator]
  }

  case class NearestElevatorControlSystem(private var elevators: List[Elevator]) extends ElevatorControlSystem {
    val MaxFloors = 16


    def pickUp(person: Person) = {
      val nearestElevator = elevators.maxBy(_.feasibilityFunction(person.pickUp, person.direction))
      elevators = elevators.map {
        case e if e == nearestElevator => nearestElevator.addRequest(person)
        case x                         => x
      }
    }

    def status = elevators

    def updateState = {
      elevators = elevators.map(_.updateState)
    }

  }

}
