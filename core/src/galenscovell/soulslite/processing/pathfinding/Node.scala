package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.utils.Array


class Node(val x: Int, val y: Int, index: Int) {
  var isWall: Boolean = false
  var connections: Array[Connection[Node]] = new Array[Connection[Node]]()


  def makeWall(): Unit = {
    isWall = true
  }

  def makeFloor(): Unit = {
    isWall = false
  }

  def debugPrint: String = {
    if (isWall) {
      "W"
    } else {
      "."
    }
  }

  def getIndex: Int = index
  def getConnections: Array[Connection[Node]] = connections
  override def toString: String = s"Node $index: ($x, $y)"
}
