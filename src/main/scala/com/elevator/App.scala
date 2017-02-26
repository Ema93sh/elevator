package com.elevator

import com.elevator.ElevatorControlSystem._
import com.elevator.ElevatorEnums.ElevatorState._

object Application extends App {

   override def main(args: Array[String]) {
     val elevatorAlpha    = Elevator("alpha", 1, Stationary, Set())
     val elevatorBeta     = Elevator("beta", 1, Stationary, Set())
     val elevatorCharlie  = Elevator("charlie", 1, Stationary, Set())
     val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

     val elevatorControlSystem = NearestElevatorControlSystem(elevators)

     val simulator = Simulator(elevatorControlSystem, shouldDisplay = true, shouldDelay = true)

     simulator.runSimulation(1000, 6)

   }
}
