package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import galenscovell.soulslite.actors.components._


class StateSystem(family: Family) extends IteratingSystem(family) {
  private val agentStateMapper: ComponentMapper[AgentStateComponent] =
    ComponentMapper.getFor(classOf[AgentStateComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val movementStateMapper: ComponentMapper[MovementStateComponent] =
    ComponentMapper.getFor(classOf[MovementStateComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val agentStateComponent: AgentStateComponent = agentStateMapper.get(entity)
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val bodyVelocity: Vector2 = bodyComponent.body.getLinearVelocity
    val movementStateComponent: MovementStateComponent = movementStateMapper.get(entity)

    // Update Agent state
    agentStateComponent.update()

    // Update Movement state
    if (bodyComponent.inMotion) {
      movementStateComponent.setIdle(false)
    } else {
      movementStateComponent.setIdle(true)
    }

    if (movementStateComponent.isFourWay) {
      if (Math.abs(bodyVelocity.x) > Math.abs(bodyVelocity.y)) {
        if (bodyVelocity.x > 0) {
          movementStateComponent.setState("right")
        } else {
          movementStateComponent.setState("left")
        }
      } else if (Math.abs(bodyVelocity.y) > Math.abs(bodyVelocity.x)) {
        if (bodyVelocity.y > 0) {
          movementStateComponent.setState("up")
        } else {
          movementStateComponent.setState("down")
        }
      }
    } else {
      if (bodyVelocity.x > 0) {
        movementStateComponent.setState("right")
      } else {
        movementStateComponent.setState("left")
      }
    }
  }
}
