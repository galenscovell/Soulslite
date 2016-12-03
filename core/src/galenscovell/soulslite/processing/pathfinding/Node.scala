package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import galenscovell.soulslite.util.{Box2DLocation, Box2DSteeringUtils}

import scala.collection.mutable.ArrayBuffer


class Node(val x: Int, val y: Int, index: Int) extends Location[Vector2] {
  private var parent: Node = _
  private var costFromStart, totalCost: Double = _
  private val connections: ArrayBuffer[Node] = new ArrayBuffer[Node]()
  var isWall: Boolean = false
  var isMarked: Boolean = false
  var orientation: Float = 0f
  val position: Vector2 = new Vector2(x, y)


  /********************
    *     Getters     *
    ********************/
  def getCostFromStart: Double = costFromStart
  def getTotalCost: Double = totalCost
  def getParent: Node = parent
  def getIndex: Int = index
  def getConnections: ArrayBuffer[Node] = connections


  /********************
    *     Setters     *
    ********************/
  def setCostFromStart(cost: Double): Unit = {
    costFromStart = cost
  }

  def setTotalCost(cost: Double): Unit = {
    totalCost = cost
  }

  def setParent(node: Node): Unit = {
    parent = node
  }


  def makeWall(): Unit = {
    isWall = true
  }

  def makeFloor(): Unit = {
    isWall = false
    isMarked = false
  }

  def makeMarked(): Unit = {
    isMarked = true
  }

  def debugPrint: String = {
    if (isWall) {
      "W"
    } else if (isMarked) {
      "+"
    } else {
      "."
    }
  }

  override def toString: String = s"Node $index: ($x, $y)"


  /********************
    *    Location     *
    ********************/
  override def getPosition: Vector2 = position

  override def getOrientation: Float = orientation
  override def setOrientation(orientation: Float): Unit = {
    this.orientation = orientation
  }

  override def vectorToAngle(vector: Vector2): Float = {
    Box2DSteeringUtils.vectorToAngle(vector)
  }
  override def angleToVector(outVector: Vector2, angle: Float): Vector2 = {
    Box2DSteeringUtils.angleToVector(outVector, angle)
  }

  override def newLocation(): Location[Vector2] = {
    new Box2DLocation
  }
}
