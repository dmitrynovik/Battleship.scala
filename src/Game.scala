package battleship

import scala.util.Random

object GameState extends Enumeration {
  type GameState = Value
  val IN_PROGRESS, FIRST_WON, SECOND_WON = Value
}

class Game {

    /** API - empty board */
    def createEmptyBoard(): Board = new Board

    /** API - random board */
    def createRandomBoard(): Board = {
       val now = java.util.Calendar.getInstance().getTime().getTime()
       val rand = new Random(now)
       val board = new Board

       val fleet = List(new Carrier(generateRandomOrientation(rand)),
         new Battleship(generateRandomOrientation(rand)),
         new Cruiser(generateRandomOrientation(rand)),
         new Submarine(generateRandomOrientation(rand)),
         new Patrol(generateRandomOrientation(rand)))

       fleet.foreach(randomPlaceShip(rand, board, _))
       board
    }

    /** API - place ship */
    def placeShip(board: Board, ship: Ship, pos: Point) : Boolean = board.placeShip(ship, pos)

    /** API - attack a position */
    def hit(board: Board, pos: Point): Boolean = board.hit(pos)

    /** API - game state */
    def computeState(board1: Board, board2: Board): GameState.Value = {
      if (board1.isGameOver) GameState.SECOND_WON
      else if (board2.isGameOver) GameState.FIRST_WON
      else GameState.IN_PROGRESS
    }

    private def generateRandomOrientation(rand: Random): Direction.Value = {
        rand.nextBoolean() match {
          case true => Direction.VERTICAL
          case _ => Direction.HORIZONTAL
        }
    }

    private def generateRandomPoint(rand: Random, board: Board) : Point = {
      val x = (rand.nextDouble() * BoardDimensions.getSize()).toInt
      val y = (rand.nextDouble() * BoardDimensions.getSize()).toInt
      new Point(x, y)
    }

    private def randomPlaceShip(rand: Random, board: Board, ship: Ship): Unit = {
      val pt = generateRandomPoint(rand, board)
      if (!board.placeShip(ship, pt)) {
        // try again (safe to use recursion on a sparse battleship board):
        randomPlaceShip(rand, board, ship)
      }
    }
  }

