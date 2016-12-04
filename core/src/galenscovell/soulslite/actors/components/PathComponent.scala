package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.utils.paths.LinePath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import galenscovell.soulslite.processing.pathfinding.Node


class PathComponent extends Component {
  private var linePath: LinePath[Vector2] = _
  private var ticks: Int = 0


  def tick(): Boolean = {
    ticks -= 1
    if (ticks <= 0) {
      ticks = 30
      true
    } else {
      false
    }
  }

  def setLinePath(nodes: scala.Array[Node]): Unit = {
    val newLinePath: Array[Vector2] = new Array[Vector2]()
    for (node: Node <- nodes) {
      newLinePath.add(node.getPosition)
    }
    linePath = new LinePath[Vector2](newLinePath, true)
  }

  def getLinePath: LinePath[Vector2] = linePath
}
