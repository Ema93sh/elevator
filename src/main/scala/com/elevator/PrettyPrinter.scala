package com.elevator

import com.elevator.ElevatorControlSystem._
import com.elevator.ElevatorEnums.ElevatorState._
import com.elevator.ElevatorEnums.PersonState._

object PrettyPrinter {

  def drawElevator(elevator: Elevator): String = {
    elevator.currentState match {
      case Open       => "[<  >]"
      case Close      => "[ >< ]"
      case GoingUp    => "[ E> ]"
      case GoingDown  => "[ <E ]"
      case Stationary => "[ == ]"

    }
  }

  def elevatorToString(elevator: Elevator): String = {
    val pickUpLocations = elevator.requests.filter(p => p.state == Waiting).map(_.floor)
    val dropLocations   = elevator.requests.filter(p => p.state == PickedUp).map(_.floor)

    val floorString      = (1 to elevator.MaxFloors).map(i =>
                            if (i == elevator.currentFloor)  drawElevator(elevator)
                            else if(pickUpLocations contains i) "[ PR ]"
                            else if(dropLocations contains i)   "[ DR ]"
                            else                                "|~~~~|"
                          ) mkString ""

    floorString + " : " + elevator.elevatorId + " (" +  elevator.currentState + ")                  \n\n"
  }


  def displayCurrentState(controlSystem: ElevatorControlSystem, currentTime: Int): Unit = {
    println("Time: " + currentTime)
    controlSystem.status
      .map(elevatorToString)
      .foreach(println)
  }


  def clearConsole() {
     print("\033[%dA".format(12));
     print("\033[K"*12)
  }
}
