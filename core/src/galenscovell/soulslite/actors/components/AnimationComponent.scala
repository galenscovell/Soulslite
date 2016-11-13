package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import galenscovell.soulslite.util.Resources


class AnimationComponent(entityType: String, idleFrames: Int, motionFrames: Int) extends Component {
  private val idle: Animation = createAnimation("idle")
  private val up: Animation = createAnimation("up")
  private val down: Animation = createAnimation("down")
  private val left: Animation = createAnimation("left")
  private val right: Animation = createAnimation("right")

  //    0
  // 3     1
  //    2
  var direction: Int = 1
  var inMotion: Boolean = false
  var stateTime: Float = 0.0f


  private def createAnimation(t: String): Animation = {
    val count: Int = t match {
      case "idle" => idleFrames
      case _ => motionFrames
    }
    val textures: Array[TextureRegion] = new Array[TextureRegion](count)

    for (i: Int <- 0 until count) {
      textures(i) = Resources.atlas.findRegion("player/" + entityType + "-" + t + i.toString)
    }

    new Animation(0.1f, textures:_*)
  }

  def getCurrentAnimation: Animation = {
    if (inMotion) {
      direction match {
        case 0 => up
        case 1 => right
        case 2 => down
        case 3 => left
      }
    } else {
      idle
    }
  }
}
