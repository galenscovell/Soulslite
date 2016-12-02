package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.pfa._
import galenscovell.soulslite.processing.pathfinding.Node


class PathComponent extends Component {
  val path: GraphPath[Connection[Node]] = new DefaultGraphPath[Connection[Node]]()
}
