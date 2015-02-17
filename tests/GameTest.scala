package battleship

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameTestSuite extends FunSuite {

  test("create empty board") {
    val game = new Game
    val board = game.createEmptyBoard()
    assert(board.getSize() == 10)
  }

  test("ships cannot be out of board bounds") {
    val board = new Board
    val patrol = new Cruiser(Direction.HORIZONTAL)

    assert(board.placeShip(patrol, new Point(-1, 0)) === false)
    assert(board.placeShip(patrol, new Point(0, -1)) === false)
    assert(board.placeShip(patrol, new Point(10, 0)) === false)
    assert(board.placeShip(patrol, new Point(0, 10)) === false)
  }

  test("ships cannot overlap") {
    val board = new Board
    val carrier = new Carrier(Direction.VERTICAL)
    val patrol = new Cruiser(Direction.HORIZONTAL)

    assert(board.placeShip(patrol, new Point(0, 0)) === true)
    assert(board.placeShip(carrier, new Point(0, 0)) === false)
    assert(board.placeShip(carrier, new Point(1, 1)) === true)
  }

  test("random board creates one ship of each type") {
    val game = new Game
    val board = game.createRandomBoard()
    val shipsWithCoords = board.getShips()
    val ships = shipsWithCoords.values.toList
    shipsWithCoords.foreach(ship => println(ship._1.toString + ", ", ship._2.getClass().getSimpleName() + ", " + ship._2.Direction))

    assert(ships.filter(s => s.isInstanceOf[Carrier]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Battleship]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Submarine]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Cruiser]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Patrol]).length === 1)
  }
}
