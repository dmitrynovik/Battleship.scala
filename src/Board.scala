package battleship

import scala.collection.mutable

object SquareState extends Enumeration {
  type SquareState = Value
  val MISS, OCCUPIED, HIT = Value
}

object BoardDimensions {
  def getSize(): Int = 10
}

class Board {
    private val ships = mutable.HashMap[Point, Ship]()

    // Squares internal data structure (assume 0,0 is top left). Fill as empty:
    private val sqs = (for (i <- 0 until BoardDimensions.getSize();
                            j <- 0 until BoardDimensions.getSize() )
                       yield new Point(i, j) ) map (a => a -> SquareState.MISS) toMap
    private val squares = collection.mutable.Map[Point, SquareState.Value]() ++= sqs // (mutable map for performance)

    def getShips(): collection.Map[Point, Ship] = ships

    def placeShip(ship: Ship, pos: Point) : Boolean = {
      // generate ship coordinates vector:
      val shipSquare = ship.Direction match {
        case Direction.HORIZONTAL => for (i <- 0 until ship.Length) yield new Point(pos.x + i, pos.y)
        case Direction.VERTICAL => for (i <- 0 until ship.Length) yield new Point(pos.x, pos.y + i)
      }

      // bound check:
      if (shipSquare.exists(pt => pt.x < 0 || pt.y < 0 || pt.x >= BoardDimensions.getSize() || pt.y >= BoardDimensions.getSize())) false
      // one of ship squares might be already taken by another ship:
      else if (shipSquare.exists(isHit)) false
      else {
        ships.put(pos, ship)
        shipSquare.foreach(squares.update(_, SquareState.OCCUPIED))
        true
      }
    }

  private def isHit: (Point) => Boolean = squares.getOrElse(_, SquareState.MISS) != SquareState.MISS

  def hit(pos: Point): Boolean = {
    val hit = isHit(pos)
    if (hit) {
      squares.update(pos, SquareState.HIT)
    }
    hit
  }

  def isGameOver(): Boolean = squares.exists(_._2 == SquareState.HIT) && !squares.exists(_._2 == SquareState.OCCUPIED)
}

