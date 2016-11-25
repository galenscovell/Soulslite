package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import galenscovell.soulslite.actors.components._


class MovementSystem(family: Family) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val directionMapper: ComponentMapper[DirectionComponent] =
    ComponentMapper.getFor(classOf[DirectionComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val bodyVelocity: Vector2 = bodyComponent.body.getLinearVelocity
    val directionComponent: DirectionComponent = directionMapper.get(entity)

    if (Math.abs(bodyVelocity.x) > Math.abs(bodyVelocity.y)) {
      if (bodyVelocity.x > 0) {
        directionComponent.direction = 0
      } else {
        directionComponent.direction = 2
      }
    } else if (Math.abs(bodyVelocity.y) > Math.abs(bodyVelocity.x)) {
      if (bodyVelocity.y > 0) {
        directionComponent.direction = 1
      } else {
        directionComponent.direction = 3
      }
    }
  }
}
