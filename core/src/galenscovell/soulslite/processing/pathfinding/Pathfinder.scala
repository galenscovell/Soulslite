package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.ai.pfa._
import com.badlogic.gdx.ai.pfa.indexed.{IndexedAStarPathFinder, IndexedGraph}
import com.badlogic.gdx.math.{MathUtils, Vector2}
import com.badlogic.gdx.utils.Array


class Pathfinder(graph: AStarGraph) {
  private val heuristic: Heuristic[Node] = (node: Node, endNode: Node) => {
      val dx: Float = endNode.x - node.x
      val dy: Float = endNode.y - node.y
      // Math.abs(dx) + Math.abs(dy).toFloat          // Manhattan distance
      Math.sqrt(dx * dx + dy * dy).toFloat            // Euclidean distance
      // Math.max(Math.abs(dx), Math.abs(dy)).toFloat // Chebyshev distance
    }
  private val neighborhood: scala.Array[Vector2] = scala.Array(
    new Vector2(-1, 0),
    new Vector2(0, -1),
    new Vector2(0, 1),
    new Vector2(1 ,0)
  )
  private val pathfinder: IndexedAStarPathFinder[Node] = new IndexedAStarPathFinder[Node](createConnections())


  def findPath(source: Vector2, target: Vector2, connectionPath: GraphPath[Connection[Node]]): Unit = {
    val sourceX: Int = MathUtils.floor(source.x)
    val sourceY: Int = MathUtils.floor(source.y)
    val targetX: Int = MathUtils.floor(target.x)
    val targetY: Int = MathUtils.floor(target.y)

    if (!(sourceX < 0 || sourceX >= graph.getWidth || sourceY < 0 || sourceY >= graph.getHeight
       || targetX < 0 || targetX >= graph.getWidth || targetY < 0 || targetY >= graph.getHeight)) {
      val sourceNode: Node = graph.getNodeAt(sourceX, sourceY)
      val targetNode: Node = graph.getNodeAt(targetX, targetY)
      connectionPath.clear()
      pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath)
    }
  }


  private def createConnections(): MyGraph = {
    val height: Int = graph.getHeight
    val width: Int = graph.getWidth
    val myGraph: MyGraph = new MyGraph(graph)

    for (y <- height - 1 to 0 by -1) {
      for (x <- 0 until width) {
        val node: Node = graph.getNodeAt(x, y)
        if (!node.isWall) {
          // Add Connection for each valid neighbor
          for (offset <- neighborhood.indices) {
            val neighborX: Int = node.x + neighborhood(offset).x.toInt
            val neighborY: Int = node.y + neighborhood(offset).y.toInt

            if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
              val neighbor: Node = graph.getNodeAt(neighborX, neighborY)
              if (!neighbor.isWall) {
                node.getConnections.add(new DefaultConnection[Node](node, neighbor))
              }
            }
          }
        }
        node.getConnections.shuffle()
      }
    }

    myGraph
  }


  private class MyGraph(grid: AStarGraph) extends IndexedGraph[Node] {
    override def getIndex(node: Node) = node.getIndex
    override def getConnections(fromNode: Node): Array[Connection[Node]] = fromNode.getConnections
    override def getNodeCount: Int = grid.getWidth * grid.getHeight
  }
}
