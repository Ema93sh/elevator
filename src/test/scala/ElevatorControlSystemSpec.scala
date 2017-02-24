import org.scalatest._

import com.elevator._

import ElevatorState._
import ElevatorControlSystem._
import Direction._
import PickUpOrDrop._

class ElevatorControlSystemSpec extends FunSpec with Matchers {
  describe("ElevatorControlSystem") {
    describe("NearestElevator") {
      describe("Pick up request") {
        it("should send the pick up request to the nearest elevator") {
          val elevatorAlpha   = Elevator("alpha", 1, Stationary, List())
          val elevatorBeta    = Elevator("beta", 6, Stationary, List())
          val elevatorCharlie = Elevator("charlie", 4, Stationary, List())
          val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(2, Up)

          elevatorControlSystem.elevators(0).requestedFloors should be(List((PickUp, 2)))
          elevatorControlSystem.elevators(1).requestedFloors should be(List())
          elevatorControlSystem.elevators(2).requestedFloors should be(List())
        }

        it("should select the elevator with the same direction") {
          val elevatorAlpha   = Elevator("alpha", 3, GoingDown, List())
          val elevatorBeta    = Elevator("beta", 3, GoingUp, List())

          val elevators = List(elevatorAlpha, elevatorBeta)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(4, Up)
          elevatorControlSystem.elevators(0).requestedFloors should be(List())
          elevatorControlSystem.elevators(1).requestedFloors should be(List((PickUp, 4)))
        }

        it("should select the elevator which is Stationary") {
          val elevatorAlpha    = Elevator("alpha", 2, GoingDown, List())
          val elevatorBeta     = Elevator("beta", 3, GoingDown, List())
          val elevatorCharlie  = Elevator("charlie", 3, Stationary, List((PickUp, 1)))

          val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(4, Up)

          elevatorControlSystem.elevators(0).requestedFloors should be(List())
          elevatorControlSystem.elevators(1).requestedFloors should be(List())
          elevatorControlSystem.elevators(2).requestedFloors should be(List((PickUp, 1), (PickUp, 4)))
        }

        it("should select the closest possible elevator if no elevator is going in the same direction") {
          val elevatorAlpha    = Elevator("alpha", 2, GoingUp, List((PickUp, 3), (PickUp, 4), (PickUp, 5)))
          val elevatorBeta     = Elevator("beta", 3, GoingUp, List((PickUp, 4), (PickUp, 5)))
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, List((PickUp, 6), (Drop, 7)))
          val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(4, Down)

          elevatorControlSystem.elevators(0).requestedFloors should be(List((PickUp, 3), (PickUp, 4), (PickUp, 5)))
          elevatorControlSystem.elevators(1).requestedFloors should be(List((PickUp, 4), (PickUp, 5), (PickUp, 4)))
          elevatorControlSystem.elevators(2).requestedFloors should be(List((PickUp, 6), (Drop, 7)))
        }

        it("test Stationary") {
          val elevatorAlpha    = Elevator("alpha", 3, Stationary, List((PickUp, 1)))
          val elevatorBeta     = Elevator("beta", 2, Stationary, List())
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, List((PickUp, 6), (Drop, 7)))
          val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(2, Down)

          elevatorControlSystem.elevators(0).requestedFloors should be(List((PickUp, 1)))
          elevatorControlSystem.elevators(1).requestedFloors should be(List((PickUp, 2)))
          elevatorControlSystem.elevators(2).requestedFloors should be(List((PickUp, 6), (Drop, 7)))
        }
      }

      describe("Drop off request") {
        it("should update the elevator request with the drop off request") {
          val elevatorAlpha    = Elevator("alpha", 2, GoingUp, List((PickUp, 3), (PickUp, 4), (PickUp, 5)))
          val elevatorBeta     = Elevator("beta", 3, GoingUp, List((PickUp, 4), (PickUp, 5)))
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, List((PickUp, 6), (Drop, 7)))
          val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.dropAt(1, "alpha")

          elevatorControlSystem.elevators(0).requestedFloors should be(List((PickUp, 3), (PickUp, 4), (PickUp, 5), (Drop, 1)))
          elevatorControlSystem.elevators(1).requestedFloors should be(List((PickUp, 4), (PickUp, 5)))
          elevatorControlSystem.elevators(2).requestedFloors should be(List((PickUp, 6), (Drop, 7)))
        }
      }

    }
  }
}
