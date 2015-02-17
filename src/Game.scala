package battleship

import scala.util.Random

class Game {

    def createEmptyBoard(): Board = new Board

    def createRandomBoard(): Board = {
       val now = java.util.Calendar.getInstance().getTime().getTime()
       val rand = new Random(now)
       val board = new Board

       val fleet = List(new Carrier(generateDirection(rand)),
         new Battleship(generateDirection(rand)),
         new Cruiser(generateDirection(rand)),
         new Submarine(generateDirection(rand)),
         new Patrol(generateDirection(rand)))

       fleet.foreach(randomPlaceShip(rand, board, _))
       board
    }

    private def generateDirection(rand: Random): Direction.Value = {
        rand.nextBoolean() match {
          case true => Direction.VERTICAL
          case _ => Direction.HORIZONTAL
        }
    }

    private def generatePoint(rand: Random, board: Board) : Point = {
      val x = (rand.nextDouble() * board.getSize()).toInt
      val y = (rand.nextDouble() * board.getSize()).toInt
      new Point(x, y)
    }

    private def randomPlaceShip(rand: Random, board: Board, ship: Ship): Unit = {
      val pt = generatePoint(rand, board)
      if (!board.placeShip(ship, pt))
        randomPlaceShip(rand, board, ship)
    }
  }

