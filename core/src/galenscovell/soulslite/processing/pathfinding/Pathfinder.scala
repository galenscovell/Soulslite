package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer


class Pathfinder(graph: AStarGraph) {
  private val openList: ArrayBuffer[Node] = new ArrayBuffer()
  private val closedList: ArrayBuffer[Node] = new ArrayBuffer()
  private val maxSearchDistance: Float = 45f


  def resetParents(): Unit = {
    for (row: Array[Node] <- graph.getGraph) {
      for (node: Node <- row) {
        node.setParent(null)
      }
    }
  }

  def findPath(startNodeVector: Vector2, endNodeVector: Vector2): Array[Node] = {
    resetParents()
    val startNode: Node = graph.getNodeAt(startNodeVector.x, startNodeVector.y)
    val endNode: Node = graph.getNodeAt(endNodeVector.x, endNodeVector.y)
    findPath(startNode, endNode)
  }

  def findPath(startNode: Node, endNode: Node): Array[Node] = {
    openList.clear()
    closedList.clear()

    startNode.setCostFromStart(0)
    startNode.setTotalCost(startNode.getCostFromStart + heuristic(startNode, endNode))
    val startNodePosition: Vector2 = startNode.position
    openList.append(startNode)

    while (openList.nonEmpty) {
      val current: Node = getBestNode(endNode)
      val distanceFromStart: Float = current.getPosition.dst2(startNodePosition)

      if (current == endNode || distanceFromStart > maxSearchDistance) {
        return tracePath(current)
      }

      openList.remove(openList.indexOf(current))
      closedList.append(current)

      for (neighborNode: Node <- current.getConnections) {
        if (!closedList.contains(neighborNode)) {
          neighborNode.setTotalCost(current.getCostFromStart + heuristic(neighborNode, endNode))

          if (!openList.contains(neighborNode)) {
            neighborNode.setParent(current)
            openList.append(neighborNode)
          } else {
            if (neighborNode.getCostFromStart < current.getCostFromStart) {
              neighborNode.setCostFromStart(neighborNode.getCostFromStart)
              neighborNode.setParent(neighborNode.getParent)
            }
          }
        }
      }
    }
    null
  }

  private def heuristic(node: Node, endNode: Node): Float = {
    val dx: Float = endNode.x - node.x
    val dy: Float = endNode.y - node.y
    // Math.abs(dx) + Math.abs(dy).toFloat          // Manhattan distance
    Math.sqrt(dx * dx + dy * dy).toFloat            // Euclidean distance
    // Math.max(Math.abs(dx), Math.abs(dy)).toFloat // Chebyshev distance
  }

  private def getBestNode(endNode: Node): Node = {
    var minCost: Double = Double.PositiveInfinity
    var bestNode: Node = null

    for (n: Node <- openList) {
      val totalCost: Double = n.getCostFromStart + heuristic(n, endNode)
      if (minCost > totalCost) {
        minCost = totalCost
        bestNode = n
      }
    }

    bestNode
  }

  private def tracePath(n: Node): Array[Node] = {
    val path: ArrayBuffer[Node] = ArrayBuffer[Node]()
    var node: Node = n

    while (node.getParent != null) {
      path.append(node)
      node = node.getParent
    }

    path.drop(1)      // Drop first node added (targetNode)
    path.dropRight(1) // Drop final node added (startNode)
    path.toArray
  }
}
