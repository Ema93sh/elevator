package com.elevator

import scala.io._

import ElevatorControlSystem._
import ElevatorEnums.Direction._
import ElevatorEnums.ElevatorState._

object Application extends App {

   override def main(args: Array[String]) {
     val elevatorAlpha    = Elevator("alpha", 1, Stationary, Set())
     val elevatorBeta     = Elevator("beta", 1, Stationary, Set())
     val elevatorCharlie  = Elevator("charlie", 1, Stationary, Set())
     val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

     val elevatorControlSystem = NearestElevatorControlSystem(elevators)

     val simulator = Simulator(elevatorControlSystem, true, true)

     simulator.runSimulation(1000, 6)

   }
}
