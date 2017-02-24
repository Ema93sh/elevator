package com.elevator

import ElevatorState._
import Direction._
import PickUpOrDrop._

// TODO: Implement type alias for floor

case class Elevator(elevatorId: String, floor: Int, currentState: ElevatorState, var requestedFloors: List[(PickUpOrDrop, Int)]) {
  val MaxLevel = 16

  def selectDirection = {
    requestedFloors match {
      case Nil =>  Stationary
      case _  => {
        val nextFloor = requestedFloors.head._2
        if(nextFloor - floor > 0) GoingUp
        else GoingDown
      }
    }
  }

  //TODO: Clean Up
  def nextFloorAndState(direction: Direction) = (direction, floor + 1, floor - 1) match {
    case (Up, floor_above, floor_below)
        if requestedFloors.head._2 == floor       => (floor, Open)
    case (Up, floor_above, floor_below)
        if floor_above > MaxLevel && requestedFloors.isEmpty => (floor, Stationary)
    case (Up, floor_above, floor_below)
        if floor_above > MaxLevel               => (floor_below, GoingDown)
    case (Up, floor_above, floor_below)         => (floor_above, GoingUp)
    case (Down, floor_above, floor_below)
        if requestedFloors.head._2 == floor       => (floor, Open)
    case (Down, floor_above, floor_below)
        if floor_below < 1 && requestedFloors.isEmpty  => (floor, Stationary)
    case (Down, floor_above, floor_below)
        if floor_below < 1                      => (floor_above, GoingUp)
    case (Down, floor_above, floor_below)       => (floor_below, GoingDown)
  }

  def updateState = currentState match {
    case _ if requestedFloors.isEmpty => {
      this.copy(currentState = Stationary)
    }

    case GoingUp => {
      val (nextFloor, nextState) = nextFloorAndState(Up)
      this.copy(floor = nextFloor, currentState = nextState)
    }

    case GoingDown => {
      val (nextFloor, nextState) = nextFloorAndState(Down)
      this.copy(floor = nextFloor, currentState = nextState)
    }

    case Open => {
      this.copy(currentState = Close, requestedFloors = requestedFloors.filterNot(_._2 == floor))
    }

    case Close => this.copy(currentState = selectDirection)

    case Stationary => this.copy(currentState = selectDirection)
  }

}
