package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import galenscovell.soulslite.util.Resources

import scala.collection.mutable


class AnimationComponent(entityType: String) extends Component {
  private val animations: Map[String, Animation] = createAnimations()
  var stateTime: Float = 0.0f


  private def createAnimations(): Map[String, Animation] = {
    val map: mutable.Map[String, Animation] = mutable.Map[String, Animation]()

    val animationInfo: mutable.Map[String, Int] = mutable.Map[String, Int]()
    entityType match {
      case "player" =>
        animationInfo.put("default-up", 12)
        animationInfo.put("default-down", 12)
        animationInfo.put("default-left", 12)
        animationInfo.put("default-right", 12)
        animationInfo.put("dash-up", 1)
        animationInfo.put("dash-down", 1)
        animationInfo.put("dash-left", 1)
        animationInfo.put("dash-right", 1)
        animationInfo.put("idle-up", 1)
        animationInfo.put("idle-down", 1)
        animationInfo.put("idle-left", 1)
        animationInfo.put("idle-right", 1)
      case "charge" =>
        animationInfo.put("default-left", 8)
        animationInfo.put("default-right", 8)
        animationInfo.put("idle-left", 1)
        animationInfo.put("idle-right", 1)
      case _ =>
    }

    for ((key, value) <- animationInfo) {
      val textures: Array[TextureRegion] = new Array[TextureRegion](value)
      for (i: Int <- 0 until value) {
        textures(i) = Resources.atlas.findRegion(s"entity/$entityType/$key$i")
      }
      map.put(key, new Animation(0.1f, textures:_*))
    }

    map.toMap
  }

  def getCurrentAnimation(agentStateName: String, movementStateName: String, isIdle: Boolean): Animation = {
    if (isIdle) {
      animations(s"idle-$movementStateName")
    } else {
      animations(s"$agentStateName-$movementStateName")
    }
  }
}
