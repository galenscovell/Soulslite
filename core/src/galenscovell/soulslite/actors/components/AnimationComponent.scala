package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.{JsonReader, JsonValue}
import galenscovell.soulslite.graphics.EntityAnimation

import scala.collection.mutable


class AnimationComponent(entityType: String) extends Component {
  private val animations: Map[String, EntityAnimation] = fromJsonForEntity(entityType)
  private var currentAnimation: EntityAnimation = _
  var stateTime: Float = 0.0f


  def fromJsonForEntity(entityName: String): Map[String, EntityAnimation] = {
    val map: mutable.Map[String, EntityAnimation] = mutable.Map[String, EntityAnimation]()

    val fullJson: JsonValue = new JsonReader().parse(Gdx.files.internal("data/animations.json"))
    val entityJson: JsonValue = fullJson.get(entityName)

    // Iterate across animations (default, dash, idle, etc.)
    for (a: Int <- 0 until entityJson.size) {
      val animation: JsonValue = entityJson.get(a)
      val animationName: String = animation.name
      val frames: Int = animation.getInt("frames")
      val speed: Float = animation.getFloat("speed")

      // Create animations for each available direction
      for (dir: String <- List("up", "down", "left", "right")) {

        // If entity has animation for a given direction, construct new animation object for it
        if (animation.has(dir)) {
          val fullName: String = s"$animationName-$dir"
          println(fullName)
          val offsetArray: JsonValue = animation.get(dir)
          val offsetVector: Vector2 = new Vector2(offsetArray.getInt(0), offsetArray.getInt(1))
          val entityAnimation: EntityAnimation = new EntityAnimation(entityName, fullName, frames, speed, offsetVector)

          // Add new animation to entity animation map under its name ("anim-dir")
          map.put(fullName, entityAnimation)
        }
      }
    }

    map.toMap
  }

  def currentAnimationComplete: Boolean = {
    currentAnimation.getAnimation.isAnimationFinished(stateTime)
  }

  def getCurrentAnimation(agentStateName: String, movementStateName: String, isIdle: Boolean): EntityAnimation = {
    val newAnimation: EntityAnimation = if (isIdle) {
      animations(s"idle-$movementStateName")
    } else {
      animations(s"$agentStateName-$movementStateName")
    }

    // If new animation is different, reset stateframe and set new animation as current
    if (newAnimation != currentAnimation) {
      stateTime = 0f
      currentAnimation = newAnimation
    }

    currentAnimation
  }
}
