package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.InputHandler


class InputSystem(family: Family, inputHandler: InputHandler) extends IteratingSystem(family) {
  private val velocityMapper: ComponentMapper[VelocityComponent] = ComponentMapper.getFor(classOf[VelocityComponent])
  private val animationMapper: ComponentMapper[AnimationComponent] = ComponentMapper.getFor(classOf[AnimationComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val velocity: VelocityComponent = velocityMapper.get(entity)
    val animationComponent: AnimationComponent = animationMapper.get(entity)

    if (inputHandler.leftPressed && inputHandler.rightPressed) {
      velocity.vx = 0
    } else if (inputHandler.leftPressed) {
      velocity.vx = -150
    } else if (inputHandler.rightPressed) {
      velocity.vx  = 150
    } else {
      velocity.vx = 0
    }

    if (inputHandler.upPressed && inputHandler.downPressed) {
      velocity.vy = 0
    } else if (inputHandler.upPressed) {
      velocity.vy = 150
    } else if (inputHandler.downPressed) {
      velocity.vy = -150
    } else {
      velocity.vy = 0
    }

    //    0
    // 3     1
    //    2
    if (Math.abs(velocity.vx) > Math.abs(velocity.vy)) {
      if (velocity.vx > 0) {
        animationComponent.direction = 1
      } else {
        animationComponent.direction = 3
      }
    } else if (Math.abs(velocity.vy) > Math.abs(velocity.vx)) {
      if (velocity.vy > 0) {
        animationComponent.direction = 0
      } else {
        animationComponent.direction = 2
      }
    }

    if (velocity.vx == 0 && velocity.vy == 0) {
      animationComponent.inMotion = false
    } else {
      animationComponent.inMotion = true
    }
  }
}
