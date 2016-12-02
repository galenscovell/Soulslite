package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class AStarGraph(world: World, tileMap: TiledMap, var width: Int, var height: Int) {
  // Times the width and height by 2 to make the graph twice as large as the actual
  // tiles which compose it -- this is done to quarter the tiles
  width *= 2
  height *= 2
  private val graph: Array[Array[Node]] = Array.ofDim[Node](width, height)
  private var wall: Boolean = _

  private val queryCallback: QueryCallback = (fixture: Fixture) => {
    wall = fixture.getFilterData.categoryBits == Constants.WALL_CATEGORY
    false
  }

  constructGraph()
  debugPrint()


  /**
    * Iterate across the width and height of our tiles multiplied by two, then for each
    * x and y divide by two in order to work with the tiles quartered (to make pathfinding
    * more precise). We also utilize padding of 0.05f to leave the quarters borders out of
    * the equation. The world.QueryAABB checks the quartered tile's bounding box for any
    * colliding fixtures (with the categoryBit of WALL).
    */
  private def constructGraph(): Unit = {
    for (y <- height - 1 to 0 by -1) {
      for (x <- 0 until width) {
        graph(y)(x) = new Node(x, y, x * height + y)
        wall = false
        world.QueryAABB(
          queryCallback,
          x / 2f + 0.05f, y / 2f + 0.05f,
          x / 2f + Constants.TILE_SIZE / 2f - 0.05f,
          y / 2f + Constants.TILE_SIZE / 2f - 0.05f
        )
        if (wall) {
          graph(y)(x).makeWall()
        }
      }
    }
  }

  private def debugPrint(): Unit = {
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
  def getGraph: Array[Array[Node]] = graph
}
