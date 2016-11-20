package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2


class VelocityComponent extends Component {
  val v: Vector2 = new Vector2(0, 0)
  var angle: Float = _
  var direction: Int = 3
  //    1
  // 2     0
  //    3

  var dashing: Boolean = false
  var dashframes: Int = 0


  def inMotion: Boolean = {
    !(v.x == 0 && v.y == 0)
  }

  def startDash(): Unit = {
    dashing = true
    dashframes = 16
  }

  def endDash(): Unit = {
    dashing = false
    v.x = 0
    v.y = 0
  }
}
