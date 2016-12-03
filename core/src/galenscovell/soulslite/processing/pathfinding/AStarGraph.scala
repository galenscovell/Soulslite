package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class AStarGraph(world: World, tileMap: TiledMap, var width: Int, var height: Int) {
  private val graph: Array[Array[Node]] = Array.ofDim[Node](width, height)

  private var wall: Boolean = _
  private val queryCallback: QueryCallback = (fixture: Fixture) => {
    wall = fixture.getFilterData.categoryBits == Constants.WALL_CATEGORY
    false
  }

  private val neighborhood: scala.Array[Vector2] = scala.Array(
    new Vector2(-1, 0),
    new Vector2(1 ,0),
    new Vector2(0, -1),
    new Vector2(0, 1),
    new Vector2(1, 1),
    new Vector2(-1, -1),
    new Vector2(1, -1),
    new Vector2(-1, 1)
  )

  constructGraph()
  createConnections()


  private def constructGraph(): Unit = {
    for (y <- height - 1 to 0 by -1) {
      for (x <- 0 until width) {
        graph(y)(x) = new Node(x, y, x * height + y)
        wall = false
        world.QueryAABB(
          queryCallback,
          x + 0.05f, y + 0.05f,
          x + Constants.TILE_SIZE - 0.05f,
          y + Constants.TILE_SIZE - 0.05f
        )
        if (wall) {
          graph(y)(x).makeWall()
        }
      }
    }
  }

  private def createConnections(): Unit = {
    for (y <- height - 1 to 0 by -1) {
      for (x <- 0 until width) {
        val node: Node = graph(y)(x)
        if (!node.isWall) {
          // Add Connection for each valid neighbor
          for (offset <- neighborhood.indices) {
            val neighborX: Int = node.x + neighborhood(offset).x.toInt
            val neighborY: Int = node.y + neighborhood(offset).y.toInt

            if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
              val neighbor: Node = graph(neighborY)(neighborX)
              if (!neighbor.isWall) {
                node.getConnections.append(neighbor)
              }
            }
          }
        }
      }
    }
  }

  def debugPrint(): Unit = {
    println()
    for (y <- height - 1 to 0 by -1) {
      println()
      for (x <- 0 until width) {
        print(graph(y)(x).debugPrint)
      }
    }
    println()
  }

  def getWidth: Int = width
  def getHeight: Int = height
  def getNodeAt(x: Int, y: Int): Node = graph(y)(x)
  def getNodeAt(x: Float, y: Float): Node = graph(Math.round(y))(Math.round(x))
  def getGraph: Array[Array[Node]] = graph
}
