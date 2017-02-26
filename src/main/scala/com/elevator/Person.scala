package com.elevator


import ElevatorEnums.PersonState._
import ElevatorEnums.Direction._

case class Person(pickUp:Int, dropAt: Int, state: PersonState) {
  def direction: Direction = {
    if( pickUp > dropAt ) Down
    else Up
  }

  def hasStopAt(floor: Int): Boolean = state match {
    case Waiting  => pickUp == floor
    case PickedUp => dropAt == floor
    case _        => false
  }

  def updateState(floor: Int): Option[Person] = state match {
    case Waiting  if floor == pickUp => Some(this.copy(state = PickedUp))
    case Waiting  if floor != pickUp => Some(this)
    case PickedUp if floor != dropAt => Some(this)
    case _                           => None
  }

  def floor: Int = state match {
    case Waiting  => pickUp
    case PickedUp => dropAt
    case _        => println(" :O This shouldnt happen!"); -1
  }


}
