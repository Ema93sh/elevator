package com.elevator

import ElevatorControlSystem._
import ElevatorEnums.RequestType._
import ElevatorEnums.Direction._
import ElevatorEnums.ElevatorState._

object PrettyPrinter {

  def requestlocationFor(elevator: Elevator, requestType: RequestType) = {
    elevator.requestQueueStatus.filter(_.requestType == requestType).map(_.floor)
  }

  def drawElevator(elevator: Elevator) = {
    elevator.currentState match {
      case Open       => "[<  >]"
      case Close      => "[ >< ]"
      case GoingUp    => "[ E> ]"
      case GoingDown  => "[ <E ]"
      case Stationary => "[ == ]"

    }
  }

  def elevatorToString(elevator: Elevator) = {
    val pickUpLocations = requestlocationFor(elevator, PickUp)
    val dropLocations   = requestlocationFor(elevator, Drop)

    val floorString      = (1 to elevator.MaxFloors).map(i =>
                            if (i == elevator.floor)  drawElevator(elevator)
                            else if(pickUpLocations contains i) "[ PR ]"
                            else if(dropLocations contains i)   "[ DR ]"
                            else                                "|~~~~|"
                          ) mkString ""

    floorString + " : " + elevator.elevatorId + " (" +  elevator.currentState + ")                   \n\n"
  }


  def displayCurrentState(controlSystem: ElevatorControlSystem, currentTime: Int) = {
    println("Time: " + currentTime)
    controlSystem.getElevators
      .map(elevatorToString(_))
      .foreach(println)
  }


  def clearConsole {
     print("\033[%dA".format(12));
     print("\033[K"*12)
  }
}
