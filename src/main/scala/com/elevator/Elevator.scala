package com.elevator

import ElevatorEnums.ElevatorState._
import ElevatorEnums.Direction._
import ElevatorEnums.RequestType._


case class Request(requestType: RequestType, floor: Int)


case class Elevator(elevatorId: String,
                    floor: Int,
                    currentState: ElevatorState,
                    private var requestQueue: List[Request]) {
  val MaxFloors = 16

  def isSameDirection(direction: Direction) = {
     (currentState == GoingUp && direction == Up) ||
     (currentState == GoingDown && direction == Down) ||
     (currentState == Stationary)
  }

  def isMovingTowards(requestedFloor: Int) = {
    if (requestedFloor - floor > 0 ) {
      isSameDirection(Up)
    }
    else {
      isSameDirection(Down)
    }
  }

  def addRequests(requests: List[Request]) = {
    requests.foreach(addRequest)
  }

  def addRequest(request: Request) = {
    requestQueue = requestQueue :+ request
  }

  def requestQueueStatus = {
    requestQueue
  }

  def isStationary = currentState match {
    case Stationary                    => true
    case Open if requestQueue.isEmpty  => true
    case Close if requestQueue.isEmpty => true
    case _                             => false
  }

  def feasibilityFunction(requestFloor: Int, requestDirection: Direction): Int = {
    val distance = Math.abs(requestFloor - floor)
    if (isStationary) {
        (MaxFloors + 10) - distance
    }
    else if (isMovingTowards(requestFloor)) {
      if(isSameDirection(requestDirection)) {
        (MaxFloors + 3) - distance - requestQueue.length
      }
      else {
        (MaxFloors + 1) - distance - requestQueue.length
      }
    }
    else {
      1
    }
  }

  def selectDirection = requestQueue match {
    case Nil =>  Stationary
    case _   => {
      val nextFloor = requestQueue.head.floor
      if(nextFloor - floor > 0) GoingUp
      else GoingDown
    }
  }

  def shouldStop =  {
    !requestQueue.isEmpty && requestQueue.head.floor == floor
  }

  def nextFloorAndState(direction: Direction) =  {
    if(shouldStop) {
      (floor, Open)
    }
    else if(requestQueue.isEmpty) {
      (floor, Stationary)
    }
    else {
      val floor_above = floor + 1
      val floor_below = floor - 1
      direction match {
        case Up   if floor_above <= MaxFloors  => (floor_above, GoingUp)
        case Up   if floor_above > MaxFloors   => (floor_below, GoingDown)
        case Down if floor_below >= 1          => (floor_below, GoingDown)
        case Down if floor_below < 1           => (floor_above, GoingUp)
      }
    }
  }

  def updateState = currentState match {
    case _ if requestQueue.isEmpty => {
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

    case Open =>
      this.copy(currentState = Close, requestQueue = requestQueue.filterNot(_.floor == floor))

    case Close => this.copy(currentState = selectDirection)

    case Stationary => this.copy(currentState = selectDirection)
  }

}
