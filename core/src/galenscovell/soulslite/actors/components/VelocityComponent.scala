package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2


class VelocityComponent extends Component {
  val v: Vector2 = new Vector2(0, 0)
  var inMotion: Boolean = false
  var direction: Int = 0
  //    0
  // 3     1
  //    2
}
