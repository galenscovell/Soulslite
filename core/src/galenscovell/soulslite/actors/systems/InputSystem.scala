package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.InputHandler


class InputSystem(family: Family, inputHandler: InputHandler) extends IteratingSystem(family) {
  private val velocityMapper: ComponentMapper[VelocityComponent] = ComponentMapper.getFor(classOf[VelocityComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val velocity: VelocityComponent = velocityMapper.get(entity)

    if (inputHandler.leftPressed && inputHandler.rightPressed) {
      velocity.vx = 0
    } else if (inputHandler.leftPressed) {
      velocity.vx = -240
    } else if (inputHandler.rightPressed) {
      velocity.vx = 240
    } else {
      velocity.vx = 0
    }

    if (inputHandler.upPressed && inputHandler.downPressed) {
      velocity.vy = 0
    } else if (inputHandler.upPressed) {
      velocity.vy = 240
    } else if (inputHandler.downPressed) {
      velocity.vy = -240
    } else {
      velocity.vy = 0
    }
  }
}
