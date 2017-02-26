import org.scalatest._

import com.elevator._

import ElevatorEnums.Direction._
import ElevatorEnums.ElevatorState._
import ElevatorEnums.PersonState._
import ElevatorControlSystem._

class ElevatorControlSystemSpec extends FunSpec with Matchers {

  describe("ElevatorControlSystem") {
    describe("NearestElevator") {
      describe("Pick up request") {
        it("should send the pick up request to the nearest elevator") {
          val elevatorAlpha   = Elevator("alpha", 1, Stationary, Set())
          val elevatorBeta    = Elevator("beta", 6, Stationary, Set())
          val elevatorCharlie = Elevator("charlie", 4, Stationary, Set())
          val elevators       = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(Person(2, 5, Waiting))

          elevatorControlSystem.status(0).requests should be(Set(Person(2, 5, Waiting)))
          elevatorControlSystem.status(1).requests should be(Set())
          elevatorControlSystem.status(2).requests should be(Set())
        }

        it("should select the elevator with the same direction") {
          val elevatorAlpha   = Elevator("alpha", 3, GoingDown, Set())
          val elevatorBeta    = Elevator("beta", 3, GoingUp, Set())

          val elevators       = List(elevatorAlpha, elevatorBeta)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(Person(4, 7, Waiting))

          elevatorControlSystem.status(0).requests should be(Set())
          elevatorControlSystem.status(1).requests should be(Set(Person(4, 7, Waiting)))
        }

        it("should select the elevator which is Stationary") {
          val elevatorAlpha    = Elevator("alpha", 2, GoingDown, Set(Person(6, 7, Waiting)))
          val elevatorBeta     = Elevator("beta", 3, GoingDown, Set(Person(7, 5, PickedUp)))
          val elevatorCharlie  = Elevator("charlie", 3, Stationary, Set())

          val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(Person(4, 7, Waiting))

          elevatorControlSystem.status(0).requests should be(Set(Person(6, 7, Waiting)))
          elevatorControlSystem.status(1).requests should be(Set(Person(7, 5, PickedUp)))
          elevatorControlSystem.status(2).requests should be(Set(Person(4, 7, Waiting)))
        }

        it("should select the closest possible elevator if no elevator is going in the same direction") {
          val elevatorAlpha    = Elevator("alpha", 3, GoingUp, Set(Person(3, 8, Waiting), Person(4, 7, Waiting), Person(5, 6, Waiting)))
          val elevatorBeta     = Elevator("beta", 3, GoingUp, Set(Person(4, 7, Waiting), Person(5, 6, Waiting)))
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, Set(Person(6, 7, Waiting), Person(4, 7, PickedUp)))
          val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(Person(2, 1, Waiting))

          elevatorControlSystem.status(0).requests should be(Set(Person(3, 8, Waiting), Person(4, 7, Waiting), Person(5, 6, Waiting)))
          elevatorControlSystem.status(1).requests should be(Set(Person(4, 7, Waiting), Person(5, 6, Waiting), Person(2, 1, Waiting)))
          elevatorControlSystem.status(2).requests should be(Set(Person(6, 7, Waiting), Person(4, 7, PickedUp)))
        }

        it("should select elevator that is stationary") {
          val elevatorAlpha    = Elevator("alpha", 3, Stationary, Set(Person(1, 5, Waiting)))
          val elevatorBeta     = Elevator("beta", 2, Stationary, Set())
          val elevatorCharlie  = Elevator("charlie", 4, GoingUp, Set(Person(6, 7, Waiting), Person(4, 7, PickedUp)))
          val elevators = List(elevatorAlpha, elevatorBeta, elevatorCharlie)

          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(Person(2, 1, Waiting))

          elevatorControlSystem.status(0).requests should be(Set(Person(1, 5, Waiting)))
          elevatorControlSystem.status(1).requests should be(Set(Person(2, 1, Waiting)))
          elevatorControlSystem.status(2).requests should be(Set(Person(6, 7, Waiting), Person(4, 7, PickedUp)))
        }

        it("should select the elevator on the same floor") {
          val elevatorAlpha    = Elevator("alpha", 3, Stationary, Set(Person(1, 5, Waiting)))
          val elevatorBeta     = Elevator("beta", 2, Open, Set())
          val elevators        = List(elevatorAlpha, elevatorBeta)
          val elevatorControlSystem = NearestElevatorControlSystem(elevators)

          elevatorControlSystem.pickUp(Person(2, 1, Waiting))

          elevatorControlSystem.status(0).requests should be(Set(Person(1, 5, Waiting)))
          elevatorControlSystem.status(1).requests should be(Set(Person(2, 1, Waiting)))
        }

      }

      // ignore("Drop off request") {
      //   it("should update the elevator request with the drop off request") {
      //     val elevatorAlpha    = Elevator("alpha", 2, GoingUp, Set(Person(3, 8, Waiting), Person(4, 7, Waiting), Person(5, 6, Waiting)))
      //     val elevatorBeta     = Elevator("beta", 3, GoingUp, Set(Person(4, 7, Waiting), Person(5, 6, Waiting)))
      //     val elevatorCharlie  = Elevator("charlie", 4, GoingUp, Set(Person(6, 7, Waiting), Person(4, 7, PickedUp)))
      //     val elevators        = List(elevatorAlpha, elevatorBeta, elevatorCharlie)
      //
      //     val elevatorControlSystem = NearestElevatorControlSystem(elevators)
      //
      //     elevatorControlSystem.dropAt(1, "alpha")
      //
      //     elevatorControlSystem.status(0).requests should be(Set(Person(3, 8, Waiting), Person(4, 7, Waiting), Person(5, 6, Waiting), Person(4, 1, PickedUp)))
      //     elevatorControlSystem.status(1).requests should be(Set(Person(4, 7, Waiting), Person(5, 6, Waiting)))
      //     elevatorControlSystem.status(2).requests should be(Set(Person(6, 7, Waiting), Person(4, 7, PickedUp)))
      //   }
      // }

    }
  }
}
