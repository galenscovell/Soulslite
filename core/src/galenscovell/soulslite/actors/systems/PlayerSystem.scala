package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.GameController
import galenscovell.soulslite.processing.fsm.PlayerAgent
import galenscovell.soulslite.util.Constants


class PlayerSystem(family: Family, controllerHandler: GameController) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val directionMapper: ComponentMapper[DirectionComponent] =
    ComponentMapper.getFor(classOf[DirectionComponent])
  private val stateMapper: ComponentMapper[StateComponent] =
    ComponentMapper.getFor(classOf[StateComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] =
    ComponentMapper.getFor(classOf[WeaponComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val directionComponent: DirectionComponent = directionMapper.get(entity)
    val stateComponent: StateComponent = stateMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    val startVelocity: Vector2 = bodyComponent.body.getLinearVelocity
    val stateFrameRatio: Float = stateComponent.getCurrentState.getFrameRatio


    /********************
      *     Normal      *
      ********************/
    if (stateComponent.isInState(PlayerAgent.DEFAULT)) {
      // Regular movement if not attacking or dashing
      bodyComponent.body.setLinearVelocity(
        controllerHandler.leftAxis.x * 5,
        controllerHandler.leftAxis.y * 5
      )

      // Dash input handling
      if (controllerHandler.dashPressed) {
        stateComponent.setState(PlayerAgent.DASH)
      }

      // Attack input handling
      if (controllerHandler.attackPressed) {
        stateComponent.setState(PlayerAgent.ATTACK)
        weaponComponent.startAttack(directionComponent.direction)
      }

    /********************
      *      Dash       *
      ********************/
    } else if (stateComponent.isInState(PlayerAgent.DASH)) {
      controllerHandler.dashPressed = false

      // If player is stationary, start dash in facing direction
      if (!bodyComponent.inMotion) {
        directionComponent.direction match {
          case 0 => startVelocity.x += 0.5f
          case 1 => startVelocity.y += 0.5f
          case 2 => startVelocity.x -= 0.5f
          case 3 => startVelocity.y -= 0.5f
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
        controllerHandler.leftAxis.x * 500, controllerHandler.leftAxis.y * 500, true
      )

    /********************
      *     Attack      *
      ********************/
    } else if (stateComponent.isInState(PlayerAgent.ATTACK)) {
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
