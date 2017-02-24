package com.elevator


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

object PickUpOrDrop {
  sealed abstract class PickUpOrDrop
  case object PickUp extends PickUpOrDrop
  case object Drop extends PickUpOrDrop
}
