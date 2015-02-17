package battleship

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameTestSuite extends FunSuite {

  test("create empty user board") {
    Game.createEmptyUserBoard()
  }

  test("create empty attacks board") {
    Game.createEmptyAttacksBoard()
  }

  test("ships cannot be out of board bounds") {
    val board = new UserBoard
    val ship = new Cruiser(Direction.HORIZONTAL)

    assert(Game.placeShip(board, ship, new Point(-1, 0)) === false)
    assert(Game.placeShip(board, ship, new Point(0, -1)) === false)
    assert(Game.placeShip(board, ship, new Point(10, 0)) === false)
    assert(Game.placeShip(board, ship, new Point(0, 10)) === false)
  }

  test("ships cannot overlap") {
    val board = new UserBoard
    val carrier = new Carrier(Direction.VERTICAL)
    val cruiser = new Cruiser(Direction.HORIZONTAL)

    assert(Game.placeShip(board, cruiser, new Point(0, 0)) === true)
    assert(Game.placeShip(board, carrier, new Point(0, 0)) === false)
    assert(Game.placeShip(board, carrier, new Point(1, 1)) === true)
  }

  test("random board creates one ship of each type") {
    val board = Game.createRandomBoard()

    val shipsWithCoords = board.getShips()
    shipsWithCoords.foreach(ship => println(ship._1.toString + ", ", ship._2.getClass().getSimpleName() + ", " + ship._2.direction))

    val ships: List[Ship] = shipsWithCoords.values.toList
    assert(ships.filter(s => s.isInstanceOf[Carrier]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Battleship]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Submarine]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Cruiser]).length === 1)
    assert(ships.filter(s => s.isInstanceOf[Patrol]).length === 1)
  }

  test("an attack - hitting the ship") {
    val board = Game.createEmptyUserBoard()
    // Place the carrier occupying (2,1) to (6,1)
    board.placeShip(new Carrier(Direction.HORIZONTAL), new Point(2, 1))

    assert(board.hit(new Point(2, 1)))
    assert(board.hit(new Point(6, 1)))
    assert(!board.hit(new Point(2, 2)))
  }

  test("The new game's state is 'in progress'") {
    val player1 = new Player(Game.createRandomBoard())
    val player2 = new Player(Game.createRandomBoard())

    assert(Game.computeState(player1, player2) === GameState.IN_PROGRESS)
  }

  test("Game over") {
    val player1 = new Player(Game.createEmptyUserBoard())
    val player2 = new Player(Game.createEmptyUserBoard())

    Game.placeShip(player1.userBoard, new Submarine(Direction.HORIZONTAL), new Point(4, 9))
    Game.hit(player2, player1, new Point(4, 9))
    Game.hit(player2, player1, new Point(5, 9))
    Game.hit(player2, player1, new Point(6, 9))

    Game.placeShip(player1.userBoard, new Cruiser(Direction.HORIZONTAL), new Point(3, 7))
    Game.hit(player2, player1, new Point(3, 7))
    Game.hit(player2, player1, new Point(4, 7))

    Game.placeShip(player1.userBoard, new Battleship(Direction.VERTICAL), new Point(6, 2))
    Game.hit(player2, player1, new Point(6, 2))
    Game.hit(player2, player1, new Point(6, 3))
    Game.hit(player2, player1, new Point(6, 4))
    Game.hit(player2, player1, new Point(6, 5))

    Game.placeShip(player1.userBoard, new Carrier(Direction.HORIZONTAL), new Point(3, 0))
    Game.hit(player2, player1, new Point(3, 0))
    Game.hit(player2, player1, new Point(4, 0))
    Game.hit(player2, player1, new Point(5, 0))
    Game.hit(player2, player1, new Point(6, 0))
    Game.hit(player2, player1, new Point(7, 0))

    Game.placeShip(player1.userBoard, new Patrol(Direction.VERTICAL), new Point(3, 9))
    Game.hit(player2, player1, new Point(3, 9))

    assert(Game.computeState(player1, player2) === GameState.SECOND_WON)
  }
}
