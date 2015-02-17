package battleship

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameTestSuite extends FunSuite {

  test("create empty board") {
    val game = new Game
    val board = game.createEmptyBoard()
  }

  test("ships cannot be out of board bounds") {
    val game = new Game
    val board = new Board
    val ship = new Cruiser(Direction.HORIZONTAL)

    assert(game.placeShip(board, ship, new Point(-1, 0)) === false)
    assert(game.placeShip(board, ship, new Point(0, -1)) === false)
    assert(game.placeShip(board, ship, new Point(10, 0)) === false)
    assert(game.placeShip(board, ship, new Point(0, 10)) === false)
  }

  test("ships cannot overlap") {
    val game = new Game
    val board = new Board
    val carrier = new Carrier(Direction.VERTICAL)
    val patrol = new Cruiser(Direction.HORIZONTAL)

    assert(game.placeShip(board, patrol, new Point(0, 0)) === true)
    assert(game.placeShip(board, carrier, new Point(0, 0)) === false)
    assert(game.placeShip(board, carrier, new Point(1, 1)) === true)
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

  test("an attack - hitting the ship") {
    val game = new Game
    val board = game.createEmptyBoard()
    // Place the carrier occupying (2,1) to (6,1)
    board.placeShip(new Carrier(Direction.HORIZONTAL), new Point(2, 1))

    assert(board.hit(new Point(2, 1)))
    assert(board.hit(new Point(6, 1)))
    assert(!board.hit(new Point(2, 2)))
  }

  test("The new game's state is 'in progress'") {
    val game = new Game
    val board1 = game.createEmptyBoard()
    val board2 = game.createEmptyBoard()

    assert(game.computeState(board1, board2) === GameState.IN_PROGRESS)
  }

  test("Game over") {
    val game = new Game
    val board1 = game.createEmptyBoard()
    val board2 = game.createEmptyBoard()

    game.placeShip(board1, new Cruiser(Direction.HORIZONTAL), new Point(1, 1))
    game.hit(board1, new Point(1, 1))
    game.hit(board1, new Point(2, 1))

    assert(game.computeState(board1, board2) === GameState.SECOND_WON)
  }
}
