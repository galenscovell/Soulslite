package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import galenscovell.soulslite.util.Resources


class AnimationComponent(entityType: String) extends Component {
  private val up: Animation = createAnimation("up", 12)
  private val down: Animation = createAnimation("down", 12)
  private val left: Animation = createAnimation("left", 12)
  private val right: Animation = createAnimation("right", 12)
  private val dash: Animation = createAnimation("dash", 1)

  var stateTime: Float = 0.0f


  private def createAnimation(t: String, n: Int): Animation = {
    val textures: Array[TextureRegion] = new Array[TextureRegion](n)

    for (i: Int <- 0 until n) {
      textures(i) = Resources.atlas.findRegion("entity/" + entityType + "-" + t + i.toString)
    }

    new Animation(0.1f, textures:_*)
  }

  def getCurrentAnimation(direction: Int): Animation = {
    direction match {
      case 0 => up
      case 1 => right
      case 2 => down
      case 3 => left
      case 4 => dash
    }
  }
}
