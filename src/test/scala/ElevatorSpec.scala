import org.scalatest._

import com.elevator._

import ElevatorEnums.Direction._
import ElevatorEnums.ElevatorState._
import ElevatorEnums.PersonState._


class ElevatorSpec extends FunSpec with Matchers {
  describe("Elevator") {
    it("should create an elevator object") {
      val elevatorAlpha = Elevator("alpha", 1, GoingDown, Set(Person(3, 6, Waiting), Person(4, 8, Waiting)))
      val elevatorCharlie = Elevator("charlie", 2, GoingUp, Set(Person(4, 1, PickedUp)))

      elevatorAlpha.currentState should be (GoingDown)
      elevatorAlpha.requests should be (Set(Person(3, 6, Waiting), Person(4, 8, Waiting)))

      elevatorCharlie.currentState should be (GoingUp)
      elevatorCharlie.requests should be (Set(Person(4, 1, PickedUp)))
    }

    describe("Going Up") {
      it("should move from floor 1 to floor 2") {
        val elevator = Elevator("alpha", 1, GoingUp, Set(Person(4, 8, Waiting)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(2)
        updatedElevator.currentState should be(GoingUp)
      }

      it("should stop if there are no more requested floors") {
        val elevator = Elevator("alpha", 1, GoingUp, Set())
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(1)
        updatedElevator.currentState should be(Stationary)
      }

      it("should stop when it reaches one of the requested floors") {
        val elevator = Elevator("alpha", 2, GoingUp, Set(Person(2, 4, Waiting), Person(1, 2, PickedUp)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(2)
        updatedElevator.currentState should be(Open)
      }

      it("should reverse direction when the elevator reaches the maximum floor") {
        val elevator = Elevator("alpha", 16, GoingUp, Set(Person(3, 6, Waiting)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(15)
        updatedElevator.currentState should be(GoingDown)
      }

      it("should stop when it reaches max floor and no more request") {
        val elevator = Elevator("alpha", 16, GoingUp, Set())
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(16)
        updatedElevator.currentState should be(Stationary)
      }

      it("should change direction if  there are no more requests going up") {
        val elevator = Elevator("alpha", 5, GoingUp, Set(Person(6, 3, Waiting)))
        val updatedElevator = elevator.updateState.updateState

        updatedElevator.currentFloor should be(6)
        updatedElevator.currentState should be(Open)
      }
    }

    describe("Going Down") {
      it("should move from floor 3 to floor 2") {
        val elevator = Elevator("alpha", 5, GoingDown, Set(Person(2, 4, Waiting)))

        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(4)
        updatedElevator.currentState should be(GoingDown)
      }


      it("should stop if there are no more requested floors") {
        val elevator = Elevator("alpha", 1, GoingDown, Set())
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(1)
        updatedElevator.currentState should be(Stationary)
      }

      it("should stop when it reaches one of the requested floors") {
        val elevator = Elevator("alpha", 2, GoingDown, Set(Person(2, 1, Waiting)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(2)
        updatedElevator.currentState should be(Open)
      }

      it("should reverse direction if it reaches the bottom floor") {
        val elevator = Elevator("alpha", 1, GoingDown, Set(Person(2, 4, Waiting)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(2)
        updatedElevator.currentState should be(GoingUp)
      }

      it("should stop if it reaches the bottom floor and request is empty") {
        val elevator = Elevator("alpha", 1, GoingDown, Set())
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(1)
        updatedElevator.currentState should be(Stationary)
      }

      it("should change direction if reaches there are no more requests going down") {
        val elevator = Elevator("alpha", 5, GoingDown, Set(Person(4, 8, Waiting)))
        val updatedElevator = elevator.updateState.updateState

        updatedElevator.currentFloor should be(4)
        updatedElevator.currentState should be(Open)
      }
    }

    describe("Open") {
      it("should close the elevator if it is open in the previous state") {
        val elevator = Elevator("alpha", 5, Open, Set(Person(7, 5, PickedUp), Person(4, 2, PickedUp)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(5)
        updatedElevator.requests     should be(Set(Person(4, 2, PickedUp)))
        updatedElevator.currentState should be(Close)
      }

      it("should remove the request for that floor") {
        val elevator = Elevator("alpha", 5, Open, Set(Person(7, 5, PickedUp), Person(8, 2, Waiting)))
        val updatedElevator = elevator.updateState

        println("p1", Person(7, 5, PickedUp).updateState(5))
        println("p2", Person(8, 2, Waiting).updateState(5))

        updatedElevator.currentFloor should be(5)
        updatedElevator.requests     should be(Set(Person(8, 2, Waiting)))
        updatedElevator.currentState should be(Close)
      }
    }

    describe("Close") {
      it("should start moving up after the elevator is closed") {
        val elevator = Elevator("alpha", 1, Close, Set(Person(2, 4, Waiting),Person(3, 6, Waiting), Person(4, 1, PickedUp)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(1)
        updatedElevator.currentState should be(GoingUp)
      }

      it("should start moving down after the elevator is closed") {
        val elevator = Elevator("alpha", 4, Close, Set(Person(3, 6, Waiting)))
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(4)
        updatedElevator.currentState should be(GoingDown)
      }

      it("should remain Stationary when there is no more requested Floors") {
        val elevator = Elevator("alpha", 4, Close, Set())
        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(4)
        updatedElevator.currentState should be(Stationary)
      }
    }

    describe("Stationary") {
      it("should start moving down once a new floor has been requested") {
        val elevator = Elevator("alpha", 4, Stationary, Set(Person(3, 6, Waiting)))

        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(4)
        updatedElevator.currentState should be(GoingDown)
      }

      it("should start moving up once a new floor has been requested") {
        val elevator = Elevator("alpha", 4, Stationary, Set(Person(8, 7, Waiting)))

        val updatedElevator = elevator.updateState

        updatedElevator.currentFloor should be(4)
        updatedElevator.currentState should be(GoingUp)
      }
    }
  }
}
