package battleship

import scala.collection.mutable

class Board {
    private val Size = 10
    private val ships = mutable.HashMap[Point, Ship]()

    // Squares internal data structure (each square is taken by ship(hit) or empty(miss))
    private val sqs = (for (i <- 0 until Size; j <- 0 until Size) yield new Point(i, j) ) map (a => a -> false) toMap
    private val squares = collection.mutable.Map[Point, Boolean]() ++= sqs // (mutable map for performance)

    def getSize(): Int = Size
    def getShips(): collection.Map[Point, Ship] = ships
    def isHit(pos: Point): Boolean = squares.getOrElse(pos, false)

    def placeShip(ship: Ship, pos: Point) : Boolean = {
      // generate ship coordinates vector (assume 0,0 is top left):
      val shipSquare = ship.Direction match {
        case Direction.HORIZONTAL => for (i <- 0 until ship.Length) yield new Point(pos.x + i, pos.y)
        case Direction.VERTICAL => for (i <- 0 until ship.Length) yield new Point(pos.x, pos.y + i)
      }

      // bound check:
      if (shipSquare.exists(pt => pt.x < 0 || pt.y < 0 || pt.x >= Size || pt.y >= Size)) false
      // one of ship squares might be already taken by another ship:
      else if (shipSquare.exists(isHit(_))) false
      else {
        ships.put(pos, ship)
        shipSquare.foreach(squares.update(_, true))
        true
      }
    }
  }

class Point(xc: Int, yc: Int) {
  val x: Int = xc
  val y: Int = yc

  override def toString: String = "(" + x + ", " + y + ")"

  override def equals(o: Any) = {
    o != null && o.isInstanceOf[Point] && o.asInstanceOf[Point].x == this.x && o.asInstanceOf[Point].y == this.y
  }

  override def hashCode() = (x.hashCode() * 10) * y.hashCode()
}

