package com.elevator

import ElevatorEnums.ElevatorState._
import ElevatorEnums.Direction._



case class Elevator(elevatorId: String,
                    currentFloor: Int,
                    currentState: ElevatorState,
                    requests:  Set[Person]) {
  val MaxFloors = 16

  val direction = currentState match {
    case GoingUp   => Up
    case GoingDown => Down
    case _         => NoDirection
  }

  def addRequest(request: Person) = {
    this.copy(requests = requests + request)
  }

  def updateState = currentState match {
    case Stationary => whenStationary

    case GoingUp => going(Up)

    case GoingDown => going(Down)

    case Open => this.copy(currentState = Close, requests = requests.flatMap(_.updateState(currentFloor)))

    case Close => whenStationary
  }

  def feasibilityFunction(requestFloor: Int, requestDirection: Direction) = {
    //TODO: Refactor! Too Many IF branches
    val distance    = Math.abs(requestFloor - currentFloor)

    if (isStationary) {
        MaxFloors + 4 - distance
    }
    else if (isMovingTowards(requestFloor)) {
      if(direction == requestDirection) {
        (MaxFloors + 3) - distance - requests.size
      }
      else {
        (MaxFloors + 1) - distance - requests.size
      }
    }
    else {
      1 - requests.size
    }
  }



  // Helper methods!

  def isStationary = (currentState == Stationary) && requests.isEmpty

  def whenStationary = requests.toList match { // TODO: Fix toList! Can't pattern match on set
    case Nil => this.copy(currentState = Stationary)
    case x :: xs  if x.floor == currentFloor => this.copy(currentState = Open, requests = (x::xs).toSet)
    case x :: xs  if x.floor > currentFloor  => this.copy(currentState = GoingUp, requests = (x::xs).toSet)
    case x :: xs  if x.floor < currentFloor  => this.copy(currentState = GoingDown, requests = (x::xs).toSet)
  }


  def isMovingTowards(requestedFloor: Int) = {
    if (requestedFloor - currentFloor > 0 ) {
      direction == Up
    }
    else {
      direction == Down
    }
  }


  def shouldOpen = requests.exists(_.hasStopAt(currentFloor))

  def reachedLimit = {
    (currentFloor == 1 && currentState == GoingDown) ||
    (currentFloor == MaxFloors && currentState == GoingUp)
  }

  def switchDirection = currentState match {
    case GoingUp   => this.copy(currentState = GoingDown, currentFloor = currentFloor - 1)
    case GoingDown => this.copy(currentState = GoingUp, currentFloor = currentFloor + 1)
    case _         => this
  }

  def updateFloor = currentState match {
    case GoingUp    => currentFloor + 1
    case GoingDown  => currentFloor - 1
    case _          => currentFloor
  }

  def noRequestInSameDirection = {
    if(direction == Up) {
      requests.filter(_.floor > currentFloor).size == 0
    } else {
      requests.filter(_.floor < currentFloor).size == 0
    }

  }

  def going(direction: Direction) = {
    // TODO: better way?
    if(requests.isEmpty) {
      this.copy(currentState = Stationary)
    }
    else if(shouldOpen) {
      this.copy(currentState = Open)
    }
    else if(reachedLimit) {
      switchDirection
    }
    else if(requests.map(_.floor).contains(currentFloor) && noRequestInSameDirection) {
      this.copy(currentState = Open)
    }
    else {
      this.copy(currentFloor = updateFloor)
    }
  }


}
