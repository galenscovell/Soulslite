package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import galenscovell.soulslite.processing.pathfinding.Node

import scala.collection.mutable


class PathComponent extends Component {
  var ticks: Int = 30
  val path: mutable.Stack[Node] = new mutable.Stack[Node]()


  def tick(): Boolean = {
    ticks -= 1
    if (ticks <= 0) {
      ticks = 20
      true
    } else {
      false
    }
  }

  def nodeReached(targetPosition: Vector2, currentPosition: Vector2): Boolean = {
    targetPosition.epsilonEquals(currentPosition, 0.05f)
  }
}
