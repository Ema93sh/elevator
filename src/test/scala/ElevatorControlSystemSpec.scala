import org.scalatest._

import com.elevator._

import ElevatorEnums.Direction._
import ElevatorEnums.RequestType._
import ElevatorEnums.ElevatorState._
import ElevatorControlSystem._

class ElevatorControlSystemSpec extends FunSpec with Matchers {

  describe("ElevatorControlSystem") {
    describe("NearestElevator") {
      describe("Pick up request") {
        it("should send the pick up request to the nearest elevator") {
          val elevatorAlpha   = Elevator("alpha", 1, Stationary, List())
          val elevatorBeta    = Elevator("beta", 6, Stationary, List())
          val elevatorCharlie = Elevator("charlie", 4, Stationary, List())
          val elevators       = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(2, Up)

          elevatorControlSystem.getElevators(0).requestQueueStatus should be(List(Request(PickUp, 2)))
          elevatorControlSystem.getElevators(1).requestQueueStatus should be(List())
          elevatorControlSystem.getElevators(2).requestQueueStatus should be(List())
        }

        it("should select the elevator with the same direction") {
          val elevatorAlpha   = Elevator("alpha", 3, GoingDown, List())
          val elevatorBeta    = Elevator("beta", 3, GoingUp, List())

          val elevators       = List(elevatorAlpha, elevatorBeta)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(4, Up)
          elevatorControlSystem.getElevators(0).requestQueueStatus should be(List())
          elevatorControlSystem.getElevators(1).requestQueueStatus should be(List(Request(PickUp, 4)))
        }

        it("should select the elevator which is Stationary") {
          val elevatorAlpha    = Elevator("alpha", 2, GoingDown, List())
          val elevatorBeta     = Elevator("beta", 3, GoingDown, List())
          val elevatorCharlie  = Elevator("charlie", 3, Stationary, List(Request(PickUp, 1)))

          val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(4, Up)

          elevatorControlSystem.getElevators(0).requestQueueStatus should be(List())
          elevatorControlSystem.getElevators(1).requestQueueStatus should be(List())
          elevatorControlSystem.getElevators(2).requestQueueStatus should be(List(Request(PickUp, 1), Request(PickUp, 4)))
        }

        it("should select the closest possible elevator if no elevator is going in the same direction") {
          val elevatorAlpha    = Elevator("alpha", 2, GoingUp, List(Request(PickUp, 3), Request(PickUp, 4), Request(PickUp, 5)))
          val elevatorBeta     = Elevator("beta", 3, GoingUp, List(Request(PickUp, 4), Request(PickUp, 5)))
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, List(Request(PickUp, 6), Request(Drop, 7)))
          val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(4, Down)

          elevatorControlSystem.getElevators(0).requestQueueStatus should be(List(Request(PickUp, 3), Request(PickUp, 4), Request(PickUp, 5)))
          elevatorControlSystem.getElevators(1).requestQueueStatus should be(List(Request(PickUp, 4), Request(PickUp, 5), Request(PickUp, 4)))
          elevatorControlSystem.getElevators(2).requestQueueStatus should be(List(Request(PickUp, 6), Request(Drop, 7)))
        }

        it("should select elevator that is stationary") {
          val elevatorAlpha    = Elevator("alpha", 3, Stationary, List(Request(PickUp, 1)))
          val elevatorBeta     = Elevator("beta", 2, Stationary, List())
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, List(Request(PickUp, 6), Request(Drop, 7)))
          val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(2, Down)

          elevatorControlSystem.getElevators(0).requestQueueStatus should be(List(Request(PickUp, 1)))
          elevatorControlSystem.getElevators(1).requestQueueStatus should be(List(Request(PickUp, 2)))
          elevatorControlSystem.getElevators(2).requestQueueStatus should be(List(Request(PickUp, 6), Request(Drop, 7)))
        }

        it("should select the elevator on the same floor") {
          val elevatorAlpha    = Elevator("alpha", 3, Stationary, List(Request(PickUp, 1)))
          val elevatorBeta     = Elevator("beta", 2, Open, List())
          val elevators = List(elevatorAlpha, elevatorBeta)
          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(2, Down)

          elevatorControlSystem.getElevators(0).requestQueueStatus should be(List(Request(PickUp, 1)))
          elevatorControlSystem.getElevators(1).requestQueueStatus should be(List(Request(PickUp, 2)))
        }

      }

      describe("Drop off request") {
        it("should update the elevator request with the drop off request") {
          val elevatorAlpha    = Elevator("alpha", 2, GoingUp, List(Request(PickUp, 3), Request(PickUp, 4), Request(PickUp, 5)))
          val elevatorBeta     = Elevator("beta", 3, GoingUp, List(Request(PickUp, 4), Request(PickUp, 5)))
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, List(Request(PickUp, 6), Request(Drop, 7)))
          val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.dropAt(1, "alpha")

          elevatorControlSystem.getElevators(0).requestQueueStatus should be(List(Request(PickUp, 3), Request(PickUp, 4), Request(PickUp, 5), Request(Drop, 1)))
          elevatorControlSystem.getElevators(1).requestQueueStatus should be(List(Request(PickUp, 4), Request(PickUp, 5)))
          elevatorControlSystem.getElevators(2).requestQueueStatus should be(List(Request(PickUp, 6), Request(Drop, 7)))
        }
      }

    }
  }
}
