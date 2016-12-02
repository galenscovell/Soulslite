package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.ai.pfa._
import com.badlogic.gdx.ai.pfa.indexed.{IndexedAStarPathFinder, IndexedGraph}
import com.badlogic.gdx.math.{MathUtils, Vector2}
import com.badlogic.gdx.utils.Array


class Pathfinder(aStarGraph: AStarGraph) {
  private val pathfinder: IndexedAStarPathFinder[Node] =
    new IndexedAStarPathFinder[Node](createConnections())
  private val connectionPath: GraphPath[Connection[Node]] =
    new DefaultGraphPath[Connection[Node]]()
  private val heuristic: Heuristic[Node] =
    (node: Node, endNode: Node) => {
      // Manhattan distance
      Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y)
    }
  private val neighborhood: scala.Array[Vector2] = Array(
    new Vector2(-1, 0),
    new Vector2(0, -1),
    new Vector2(0, 1),
    new Vector2(1 ,0)
  )


  def findNextNode(source: Vector2, target: Vector2): Node = {
    val sourceX: Int = MathUtils.floor(source.x)
    val sourceY: Int = MathUtils.floor(source.y)
    val targetX: Int = MathUtils.floor(target.x)
    val targetY: Int = MathUtils.floor(target.y)

    if (aStarGraph == null
      || sourceX < 0 || sourceX >= aStarGraph.getWidth
      || sourceY < 0 || sourceY >= aStarGraph.getHeight
      || targetX < 0 || targetX >= aStarGraph.getWidth
      || targetY < 0 || targetY >= aStarGraph.getHeight) {
      return null
    }

    val sourceNode: Node = aStarGraph.getNodeAt(sourceX, sourceY)
    val targetNode: Node = aStarGraph.getNodeAt(targetX, targetY)
    connectionPath.clear()
    pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath)

    if (connectionPath.getCount == 0) {
      null
    } else {
      connectionPath.get(0).getToNode
    }
  }


  private def createConnections(): MyGraph = {
    val height: Int = aStarGraph.getHeight
    val width: Int = aStarGraph.getWidth
    val myGraph: MyGraph = new MyGraph(aStarGraph)

    for (y <- height - 1 to 0 by -1) {
      for (x <- 0 until width) {
        val node: Node = aStarGraph.getNodeAt(x, y)
        if (!node.isWall) {
          // Add Connection for each valid neighbor
          for (offset <- neighborhood.indices) {
            val neighborX: Int = node.x + neighborhood(offset).x.toInt
            val neighborY: Int = node.y + neighborhood(offset).y.toInt

            if (neighborX >= 0 && neighborY < width && neighborY >= 0 && neighborY < height) {
              val neighbor: Node = aStarGraph.getNodeAt(neighborX, neighborY)
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


  private class MyGraph(aStarGraph: AStarGraph) extends IndexedGraph[Node] {
    override def getIndex(node: Node) = {
      node.getIndex
    }

    override def getConnections(fromNode: Node): Array[Connection[Node]] = {
      fromNode.getConnections
    }

    override def getNodeCount: Int = {
      aStarGraph.getWidth * aStarGraph.getHeight
    }
  }
}
