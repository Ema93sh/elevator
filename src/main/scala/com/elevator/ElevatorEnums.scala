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

  object PersonState {
    sealed abstract class PersonState
    case object Waiting extends PersonState
    case object PickedUp extends PersonState
    case object DropedOff  extends PersonState
  }

  object Direction {
    sealed abstract class Direction
    case object Up extends Direction
    case object Down extends Direction
    case object NoDirection extends Direction
  }

}
