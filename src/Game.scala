package battleship

import scala.util.Random

object GameState extends Enumeration {
  type GameState = Value
  val IN_PROGRESS, FIRST_WON, SECOND_WON, DRAW = Value
}

object Game {
    def getBoardSize(): Int = 10

    /** API - empty board */
    def createEmptyUserBoard(): UserBoard = new UserBoard
    def createEmptyAttacksBoard(): AttacksBoard = new AttacksBoard

    /** API - random board */
    def createRandomBoard(): UserBoard = {
       val now = java.util.Calendar.getInstance().getTime().getTime()
       val rand = new Random(now)
       val board = new UserBoard

       val fleet = List(new Carrier(generateRandomOrientation(rand)),
         new Battleship(generateRandomOrientation(rand)),
         new Cruiser(generateRandomOrientation(rand)),
         new Submarine(generateRandomOrientation(rand)),
         new Patrol(generateRandomOrientation(rand)))

       fleet.foreach(randomPlaceShip(rand, board, _))
       board
    }

    /** API - places ship */
    def placeShip(board: UserBoard, ship: Ship, pos: Point) : Boolean = board.placeShip(ship, pos)

    /** API - attacks a position, returns TRUE if hit */
    def hit(attacker: Player, defender: Player, pos: Point): Boolean =  {
      val hit = defender.userBoard.hit(pos)
      if (hit) {
        attacker.attacksBoard.updateState(pos, SquareState.HIT)
        true
      }
      false
    }

    /** API - game state */
    def computeState(player1: Player, player2: Player): GameState.Value = {
      if (player1.attacksBoard.hits == gameOverHits && player2.attacksBoard.hits == gameOverHits) GameState.DRAW
      else if (player1.attacksBoard.hits == gameOverHits) GameState.FIRST_WON
      else if (player2.attacksBoard.hits == gameOverHits) GameState.SECOND_WON
      else GameState.IN_PROGRESS
    }

    private def generateRandomOrientation(rand: Random): Direction.Value = {
        rand.nextBoolean() match {
          case true => Direction.VERTICAL
          case _ => Direction.HORIZONTAL
        }
    }

    private def gameOverHits: Int = 5 + 4 + 3 + 2 + 1

    private def generateRandomPoint(rand: Random, board: Board) : Point = {
      val x = (rand.nextDouble() * getBoardSize()).toInt
      val y = (rand.nextDouble() * getBoardSize()).toInt
      new Point(x, y)
    }

    private def randomPlaceShip(rand: Random, board: UserBoard, ship: Ship): Unit = {
      val pt = generateRandomPoint(rand, board)
      if (!board.placeShip(ship, pt)) {
        // try again (safe to use recursion on a sparse battleship board):
        randomPlaceShip(rand, board, ship)
      }
    }
  }

