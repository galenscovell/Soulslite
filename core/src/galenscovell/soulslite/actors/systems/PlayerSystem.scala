package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.ControllerHandler
import galenscovell.soulslite.processing.fsm.PlayerAgent
import galenscovell.soulslite.util.Constants


class PlayerSystem(family: Family, controllerHandler: ControllerHandler) extends IteratingSystem(family) {
  private val agentStateMapper: ComponentMapper[AgentStateComponent] =
    ComponentMapper.getFor(classOf[AgentStateComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val movementStateMapper: ComponentMapper[MovementStateComponent] =
    ComponentMapper.getFor(classOf[MovementStateComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] =
    ComponentMapper.getFor(classOf[WeaponComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val agentStateComponent: AgentStateComponent = agentStateMapper.get(entity)
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val movementStateComponent: MovementStateComponent = movementStateMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    val startVelocity: Vector2 = bodyComponent.body.getLinearVelocity
    val stateFrameRatio: Float = agentStateComponent.getCurrentState.getFrameRatio


    /********************
      *     Normal      *
      ********************/
    if (agentStateComponent.isInState(PlayerAgent.DEFAULT)) {
      // Regular movement if not attacking or dashing
      bodyComponent.body.setLinearVelocity(
        controllerHandler.leftAxis.x * 5,
        controllerHandler.leftAxis.y * 5
      )

      // Dash input handling
      if (controllerHandler.dashPressed) {
        agentStateComponent.setState(PlayerAgent.DASH)
      }

      // Attack input handling
      if (controllerHandler.attackPressed) {
        agentStateComponent.setState(PlayerAgent.ATTACK)
        weaponComponent.startAttack(movementStateComponent.getCurrentState)
      }

    /********************
      *      Dash       *
      ********************/
    } else if (agentStateComponent.isInState(PlayerAgent.DASH)) {
      controllerHandler.dashPressed = false

      // If player is stationary, start dash in facing direction
      if (!bodyComponent.inMotion) {
        movementStateComponent.getCurrentState match {
          case "right" => startVelocity.x += 0.5f
          case "up" => startVelocity.y += 0.5f
          case "left" => startVelocity.x -= 0.5f
          case "down" => startVelocity.y -= 0.5f
        }
      }

      val normalizedVelocity: Vector2 = startVelocity.nor()
      if (stateFrameRatio > 0.35f) {
        bodyComponent.body.applyForceToCenter(
          normalizedVelocity.x * (7000 * stateFrameRatio), normalizedVelocity.y * (7000 * stateFrameRatio), true
        )
      } else if (stateFrameRatio > 0) {
        bodyComponent.body.applyForceToCenter(
          normalizedVelocity.x * (-1000 * stateFrameRatio), normalizedVelocity.y * (-1000 * stateFrameRatio), true
        )
      }

      // Allow player slight control during dash
      bodyComponent.body.applyForceToCenter(
        controllerHandler.leftAxis.x * 300, controllerHandler.leftAxis.y * 300, true
      )

    /********************
      *     Attack      *
      ********************/
    } else if (agentStateComponent.isInState(PlayerAgent.ATTACK)) {
      controllerHandler.attackPressed = false
      bodyComponent.body.setLinearVelocity(0, 0)

      if (stateFrameRatio <= 0.5f && stateFrameRatio > 0.4f) {
        weaponComponent.endAttack()
      }
    }


    // Normalize diagonal movement
    val endVelocity: Vector2 = bodyComponent.body.getLinearVelocity
    if (endVelocity.len() > Constants.MAX_NORMAL_VELOCITY) {
      bodyComponent.body.setLinearVelocity(endVelocity.nor().scl(Constants.MAX_NORMAL_VELOCITY))
    }
  }
}
