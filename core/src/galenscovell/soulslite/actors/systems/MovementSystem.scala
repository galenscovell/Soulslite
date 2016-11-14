package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components._


class MovementSystem(family: Family) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] = ComponentMapper.getFor(classOf[BodyComponent])
  private val velocityMapper: ComponentMapper[VelocityComponent] = ComponentMapper.getFor(classOf[VelocityComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val body: Body = bodyMapper.get(entity).body
    val velocity: VelocityComponent = velocityMapper.get(entity)

    //    0
    // 3     1
    //    2
    if (Math.abs(velocity.v.x) > Math.abs(velocity.v.y)) {
      if (velocity.v.x > 0) {
        velocity.direction = 1
      } else {
        velocity.direction = 3
      }
    } else if (Math.abs(velocity.v.y) > Math.abs(velocity.v.x)) {
      if (velocity.v.y > 0) {
        velocity.direction = 0
      } else {
        velocity.direction = 2
      }
    }

    if (velocity.v.x == 0 && velocity.v.y == 0) {
      velocity.inMotion = false
    } else {
      velocity.inMotion = true
    }

    body.setLinearVelocity(velocity.v.x, velocity.v.y)
  }
}
