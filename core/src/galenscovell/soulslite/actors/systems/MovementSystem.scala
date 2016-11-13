package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components._


class MovementSystem(family: Family) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] = ComponentMapper.getFor(classOf[BodyComponent])
  private val velocityMapper: ComponentMapper[VelocityComponent] = ComponentMapper.getFor(classOf[VelocityComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val body: Body = bodyMapper.get(entity).getBody
    val velocity: VelocityComponent = velocityMapper.get(entity)

    body.setLinearVelocity(velocity.vx, velocity.vy)
  }
}
