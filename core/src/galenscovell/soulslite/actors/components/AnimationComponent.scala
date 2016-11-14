package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import galenscovell.soulslite.util.Resources


class AnimationComponent(entityType: String) extends Component {
  private val up: Animation = createAnimation("up")
  private val down: Animation = createAnimation("down")
  private val left: Animation = createAnimation("left")
  private val right: Animation = createAnimation("right")

  var stateTime: Float = 0.0f


  private def createAnimation(t: String): Animation = {
    val count: Int = 12
    val textures: Array[TextureRegion] = new Array[TextureRegion](count)

    for (i: Int <- 0 until count) {
      textures(i) = Resources.atlas.findRegion("player/" + entityType + "-" + t + i.toString)
    }

    new Animation(0.1f, textures:_*)
  }

  def getCurrentAnimation(direction: Int): Animation = {
    direction match {
      case 0 => up
      case 1 => right
      case 2 => down
      case 3 => left
    }
  }
}
