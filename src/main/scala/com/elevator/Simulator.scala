package com.elevator

import ElevatorControlSystem._
import ElevatorEnums.Direction._
import ElevatorEnums.PersonState._

case class Simulator(controlSystem: ElevatorControlSystem, shouldDisplay: Boolean, shouldDelay: Boolean) {
  var currentTime = 0
  var status = "Starting Simulation -- PR[Pick Up Request] DR[Drop Request]"

  def randomPickUpAndDrop = {
    val r = scala.util.Random
    val range = 1 to 16
    val randomPickUp = range(r.nextInt(range.length))
    val randomDrop   = range(r.nextInt(range.length))
    controlSystem.pickUp(Person(randomPickUp, randomDrop, Waiting))
    status = "Pick requested at " + randomPickUp + " with drop at " + randomDrop + " -------------"
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
