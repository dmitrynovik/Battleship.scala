package battleship

class Player(u: UserBoard) {
  val userBoard = u
  val attacksBoard = Game.createEmptyAttacksBoard()
}
