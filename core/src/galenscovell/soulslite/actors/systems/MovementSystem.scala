package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components.{PositionComponent, VelocityComponent}


class MovementSystem(family: Family) extends IteratingSystem(family) {
  private val positionMapper: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])
  private val velocityMapper: ComponentMapper[VelocityComponent] = ComponentMapper.getFor(classOf[VelocityComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val position: PositionComponent = positionMapper.get(entity)
    val velocity: VelocityComponent = velocityMapper.get(entity)

    position.x += velocity.vx * deltaTime
    position.y += velocity.vy * deltaTime
  }
}
