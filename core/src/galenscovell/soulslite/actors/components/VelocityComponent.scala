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

  val totalDashFrames: Float = 16f
  var dashing: Boolean = false
  var currentDashFrame: Float = 0f


  def inMotion: Boolean = {
    !(v.x == 0 && v.y == 0)
  }


  def startDash(): Unit = {
    dashing = true
    currentDashFrame = totalDashFrames
  }

  def dashDecrement(): Unit = {
    currentDashFrame -= 1
  }

  def isSpeeding: Boolean = {
    currentDashFrame > (totalDashFrames / 5)
  }

  def isSlowing: Boolean = {
    currentDashFrame > 0
  }

  def dashFrameRatio: Float = {
    currentDashFrame / totalDashFrames
  }

  def endDash(): Unit = {
    dashing = false
  }
}
