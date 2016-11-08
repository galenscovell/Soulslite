package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components.{PositionComponent, VelocityComponent}


class MovementSystem(family: Family) extends IteratingSystem(family) {
  private val pm: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])
  private val vm: ComponentMapper[VelocityComponent] = ComponentMapper.getFor(classOf[VelocityComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val position: PositionComponent = pm.get(entity)
    val velocity: VelocityComponent = vm.get(entity)

    position.x += velocity.velocity * deltaTime
    position.y += velocity.velocity * deltaTime
  }
}
