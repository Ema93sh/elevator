package com.elevator


package object ElevatorEnums {

  object ElevatorState {
    sealed abstract class ElevatorState
    case object GoingUp extends ElevatorState
    case object GoingDown extends ElevatorState
    case object Open  extends ElevatorState
    case object Close extends ElevatorState
    case object Stationary extends ElevatorState
  }

  object Direction {
    sealed abstract class Direction
    case object Up extends Direction
    case object Down extends Direction
  }

  object RequestType {
    sealed abstract class RequestType
    case object PickUp extends RequestType
    case object Drop extends RequestType
  }

}
