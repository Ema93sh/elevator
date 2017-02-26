import org.scalatest._

import com.elevator._

import ElevatorEnums.RequestType._
import ElevatorEnums.Direction._
import ElevatorEnums.ElevatorState._


class ElevatorSpec extends FunSpec with Matchers {

  describe("Elevator") {
    it("should create an elevator object") {
      val elevatorAlpha = Elevator("alpha", 1, GoingDown, List(Request(PickUp, 3), Request(PickUp, 4)))
      val elevatorCharlie = Elevator("charlie", 2, GoingUp, List(Request(PickUp, 1)))

      elevatorAlpha.currentState should be (GoingDown)
      elevatorAlpha.requestQueueStatus should be (List(Request(PickUp, 3), Request(PickUp, 4)))

      elevatorCharlie.currentState should be (GoingUp)
      elevatorCharlie.requestQueueStatus should be (List(Request(PickUp, 1)))
    }

    describe("Going Up") {
      it("should move from floor 1 to floor 2") {
        val elevator = Elevator("alpha", 1, GoingUp, List(Request(PickUp, 4)))
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
        val elevator = Elevator("alpha", 2, GoingUp, List(Request(PickUp, 2), Request(PickUp, 3)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(2)
        updatedElevator.currentState should be(Open)
      }

      it("should reverse direction when the elevator reaches the maximum floor") {
        val elevator = Elevator("alpha", 16, GoingUp, List(Request(PickUp, 3)))
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
        val elevator = Elevator("alpha", 5, GoingDown, List(Request(PickUp, 2)))

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
        val elevator = Elevator("alpha", 2, GoingDown, List(Request(PickUp, 2)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(2)
        updatedElevator.currentState should be(Open)
      }

      it("should reverse direction if it reaches the bottom floor") {
        val elevator = Elevator("alpha", 1, GoingDown, List(Request(PickUp, 2)))
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
        val elevator = Elevator("alpha", 5, Open, List(Request(PickUp, 5), Request(Drop, 2)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(5)
        updatedElevator.requestQueueStatus should be(List(Request(Drop, 2)))
        updatedElevator.currentState should be(Close)
      }
    }

    describe("Close") {
      it("should start moving up after the elevator is closed") {
        val elevator = Elevator("alpha", 1, Close, List(Request(PickUp, 2),Request(PickUp, 3), Request(Drop, 1)))
        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(1)
        updatedElevator.currentState should be(GoingUp)
      }

      it("should start moving down after the elevator is closed") {
        val elevator = Elevator("alpha", 4, Close, List(Request(PickUp, 3)))
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
        val elevator = Elevator("alpha", 4, Stationary, List(Request(PickUp, 3)))

        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(4)
        updatedElevator.currentState should be(GoingDown)
      }

      it("should start moving up once a new floor has been requested") {
        val elevator = Elevator("alpha", 4, Stationary, List(Request(PickUp, 7)))

        val updatedElevator = elevator.updateState

        updatedElevator.floor should be(4)
        updatedElevator.currentState should be(GoingUp)
      }
    }
  }
}
