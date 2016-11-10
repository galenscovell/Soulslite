package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import galenscovell.soulslite.util.Resources


class AnimationComponent(entityType: String) extends Component {
  private val idle: Animation = createAnimation("idle")
  private val moving: Animation = createAnimation("right")

  var inMotion: Boolean = false
  var direction: Int = 1 // -1 is left, 1 is right
  var stateTime: Float = 0.0f


  private def createAnimation(t: String): Animation = {
    val count: Int = t match {
      case "idle" => 9
      case _ => 6
    }
    val textures: Array[TextureRegion] = new Array[TextureRegion](count)

    for (i: Int <- 0 until count) {
      textures(i) = Resources.atlas.findRegion(entityType + "-" + t + i.toString)
    }

    new Animation(1.0f / count, textures:_*)
  }

  def getCurrentAnimation: Animation = {
    if (inMotion) {
      moving
    } else {
      idle
    }
  }
}
