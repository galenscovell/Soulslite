package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class AStarGraph(world: World, tileMap: TiledMap, width: Int, height: Int) {
  private val graph: Array[Array[Node]] = Array.ofDim[Node](width, height)
  private var wall: Boolean = _

  private val queryCallback: QueryCallback = new QueryCallback {
    override def reportFixture(fixture: Fixture): Boolean = {
      wall = fixture.getFilterData.categoryBits == Constants.WALL_CATEGORY
      false
    }
  }

  constructGraph()
  debugPrint()


  private def constructGraph(): Unit = {
    for (y <- height - 1 to 0 by -1) {
      for (x <- 0 until width) {
        graph(y)(x) = new Node(x, y, x * height + y)
        wall = false
        world.QueryAABB(
          queryCallback,
          x + 0.1f, y + 0.1f,
          x + Constants.TILE_SIZE - 0.1f, y + Constants.TILE_SIZE - 0.1f
        )
        if (wall) {
          graph(y)(x).makeWall()
        }
      }
    }
  }

  def getWidth: Int = {
    width
  }

  def getHeight: Int = {
    height
  }

  def getNodeAt(x: Int, y: Int): Node = {
    graph(y)(x)
  }

  def getGraph: Array[Array[Node]] = {
    graph
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
}
