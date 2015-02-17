package battleship

import scala.collection.mutable

object SquareState extends Enumeration {
  type SquareState = Value
  val UNKNOWN, MISS, OCCUPIED, HIT = Value
}

abstract class Board {

    // Squares internal data structure (assume 0,0 is top left). Fill as empty:
    private val sqs = (for (i <- 0 until Game.getBoardSize();
                            j <- 0 until Game.getBoardSize() )
                       yield new Point(i, j) ) map (a => a -> defaultSquareState()) toMap

    protected val squares = collection.mutable.Map[Point, SquareState.Value]() ++= sqs // (mutable map for performance)

    def updateState(pos: Point, state: SquareState.Value) = squares.update(pos, state)
    def defaultSquareState(): SquareState.Value
    def hits: Int = squares.values.count(v => v == SquareState.HIT)
}

class UserBoard extends Board {
  override def defaultSquareState = SquareState.MISS
  private val ships = mutable.HashMap[Point, Ship]()

  def getShips(): collection.Map[Point, Ship] = ships

  def placeShip(ship: Ship, pos: Point) : Boolean = {
    // generate ship coordinates vector:
    val shipSquare = ship.direction match {
      case Direction.HORIZONTAL => for (i <- 0 until ship.length) yield new Point(pos.x + i, pos.y)
      case Direction.VERTICAL => for (i <- 0 until ship.length) yield new Point(pos.x, pos.y + i)
    }

    // bound check:
    if (shipSquare.exists(pt => pt.x < 0 || pt.y < 0 || pt.x >= Game.getBoardSize() || pt.y >= Game.getBoardSize())) false
    // overlap check:
    else if (shipSquare.exists(pt => squares.get(pt) != Some(SquareState.MISS))) false
    else {
      // all good, place ship:
      ships.put(pos, ship)
      shipSquare.foreach(squares.update(_, SquareState.OCCUPIED))
      true
    }
  }

  def hit(pos: Point): Boolean = {
    val square = squares.getOrElse(pos, SquareState.MISS)
    if (square == SquareState.OCCUPIED || square == SquareState.HIT) {
      squares.update(pos, SquareState.HIT)
      true
    } else {
      squares.update(pos, SquareState.MISS)
      false
    }
  }
}

class AttacksBoard extends Board {

  override def defaultSquareState = SquareState.UNKNOWN
}

