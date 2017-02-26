package com.elevator

import ElevatorControlSystem._
import ElevatorEnums.Direction._

case class Simulator(controlSystem: ElevatorControlSystem, shouldDisplay: Boolean, shouldDelay: Boolean) {
  var currentTime = 0
  var status = "Starting Simulation"

  def randomPickUpAndDrop = {
    val r = scala.util.Random
    var randomFloor = Math.abs(r.nextInt()) % 16 + 1
    val dirIndex = Math.abs(r.nextInt()) % 2
    val randomDirection = List(Up, Down)(dirIndex)
    val assignedElevator = controlSystem.pickUp(randomFloor, randomDirection)
    var randomDropLocation = 1


    //TODO clean up
    if (randomDirection == Up) {
      val range = randomFloor + 1 to 16
      randomDropLocation = range(r.nextInt(range.length))
    }
    else {
      val range = 1 to randomFloor - 1
      randomDropLocation = range(r.nextInt(range.length))
    }

    //TODO drop should be called after the pick up
    controlSystem.dropAt(randomDropLocation, assignedElevator.elevatorId)
    status = "Pick up at " + randomFloor + " and drop at " + randomDropLocation + " ---"
  }

  def display = {
    if(shouldDisplay) {
      PrettyPrinter.clearConsole
      PrettyPrinter.displayCurrentState(controlSystem, currentTime)
      println("Status Update:" + status)
      println("")
    }
  }

  def delay = {
    if(shouldDelay) {
        Thread.sleep(500)
    }
  }

  def runSimulation(steps: Int, requestInterval: Int) = {
    PrettyPrinter.displayCurrentState(controlSystem, currentTime)
    println("Status Update:" + status)
    println("")
    (1 to steps).foreach(_ => {
      display
      controlSystem.updateState
      currentTime += 1
      if(currentTime % requestInterval == 0) {
        randomPickUpAndDrop
      }
      delay
    })
  }
}
