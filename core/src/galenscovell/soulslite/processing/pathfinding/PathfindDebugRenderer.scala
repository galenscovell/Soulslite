package galenscovell.soulslite.processing.pathfinding

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import galenscovell.soulslite.util.{Constants, Resources}


class PathfindDebugRenderer(aStarGraph: AStarGraph) {
  private val graph: AStarGraph = aStarGraph


  def render(batch: SpriteBatch): Unit = {
    for (row: Array[Node] <- graph.getGraph) {
      for (node: Node <- row) {
        if (node.isMarked) {
          batch.draw(Resources.spTest3, node.position.x, node.position.y, Constants.TILE_SIZE, Constants.TILE_SIZE)
        }
      }
    }
  }
}
