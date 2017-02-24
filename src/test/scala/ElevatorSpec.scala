import org.scalatest._

import com.elevator._

import ElevatorState._
import PickUpOrDrop._

class ElevatorSpec extends FunSpec with Matchers {
  describe("Elevator") {
    it("should create an elevator object") {
      val elevatorAlpha = Elevator("alpha", 1, GoingDown, List((PickUp, 3), (PickUp, 4)))
      val elevatorCharlie = Elevator("charlie", 2, GoingUp, List((PickUp,1)))

      elevatorAlpha.currentState should be (GoingDown)
      elevatorAlpha.requestedFloors should be (List((PickUp, 3), (PickUp, 4)))

      elevatorCharlie.currentState should be (GoingUp)
      elevatorCharlie.requestedFloors should be (List((PickUp, 1)))
    }

    describe("Going Up") {
      it("should move from floor 1 to floor 2") {
        val elevator = Elevator("alpha", 1, GoingUp, List((PickUp, 4)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(2)
        updatedElevator.currentState should be(GoingUp)
      }

      it("should stop if there are no more requested floors") {
        val elevator = Elevator("alpha", 1, GoingUp, List())
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(1)
        updatedElevator.currentState should be(Stationary)
      }

      it("should stop when it reaches one of the requested floors") {
        val elevator = Elevator("alpha", 2, GoingUp, List((PickUp, 2), (PickUp, 3)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(2)
        updatedElevator.currentState should be(Open)
      }

      it("should reverse direction when the elevator reaches the maximum floor") {
        val elevator = Elevator("alpha", 16, GoingUp, List((PickUp, 3)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(15)
        updatedElevator.currentState should be(GoingDown)
      }

      it("should stop when it reaches max floor and no more request") {
        val elevator = Elevator("alpha", 16, GoingUp, List())
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(16)
        updatedElevator.currentState should be(Stationary)
      }

    }

    describe("Going Down") {
      it("should move from floor 3 to floor 2") {
        val elevator = Elevator("alpha", 5, GoingDown, List((PickUp, 2)))

        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(4)
        updatedElevator.currentState should be(GoingDown)
      }


      it("should stop if there are no more requested floors") {
        val elevator = Elevator("alpha", 1, GoingDown, List())
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(1)
        updatedElevator.currentState should be(Stationary)
      }

      it("should stop when it reaches one of the requested floors") {
        val elevator = Elevator("alpha", 2, GoingDown, List((PickUp, 2)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(2)
        updatedElevator.currentState should be(Open)
      }

      it("should reverse direction if it reaches the bottom floor") {
        val elevator = Elevator("alpha", 1, GoingDown, List((PickUp, 2)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(2)
        updatedElevator.currentState should be(GoingUp)
      }

      it("should stop if it reaches the bottom floor and request is empty") {
        val elevator = Elevator("alpha", 1, GoingDown, List())
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(1)
        updatedElevator.currentState should be(Stationary)
      }
    }

    describe("Open") {
      it("should close the elevator if it is open in the previous state") {
        val elevator = Elevator("alpha", 5, Open, List((PickUp, 5), (Drop, 2)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(5)
        updatedElevator.requestedFloors should be(List((Drop, 2)))
        updatedElevator.currentState should be(Close)
      }
    }

    describe("Close") {
      it("should start moving up after the elevator is closed") {
        val elevator = Elevator("alpha", 1, Close, List((PickUp, 2),(PickUp, 3), (Drop, 1)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(1)
        updatedElevator.currentState should be(GoingUp)
      }

      it("should start moving down after the elevator is closed") {
        val elevator = Elevator("alpha", 4, Close, List((PickUp, 3)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(4)
        updatedElevator.currentState should be(GoingDown)
      }

      it("should remain Stationary when there is no more requested Floors") {
        val elevator = Elevator("alpha", 4, Close, List())
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(4)
        updatedElevator.currentState should be(Stationary)
      }
    }

    describe("Stationary") {
      it("should start moving down once a new floor has been requested") {
        val elevator = Elevator("alpha", 4, Stationary, List((PickUp, 3)))

        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(4)
        updatedElevator.currentState should be(GoingDown)
      }

      it("should start moving up once a new floor has been requested") {
        val elevator = Elevator("alpha", 4, Stationary, List((PickUp, 7)))

        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(4)
        updatedElevator.currentState should be(GoingUp)
      }
    }
  }
}
