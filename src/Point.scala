package battleship

class Point(xc: Int, yc: Int) {
  val x: Int = xc
  val y: Int = yc

  override def toString: String = "(" + x + ", " + y + ")"

  override def equals(o: Any) = {
    o != null && o.isInstanceOf[Point] && o.asInstanceOf[Point].x == this.x && o.asInstanceOf[Point].y == this.y
  }

  override def hashCode() = (x.hashCode() * BoardDimensions.getSize()) * y.hashCode()
}
