package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.GameController
import galenscovell.soulslite.util.Constants


class PlayerSystem(family: Family, controllerHandler: GameController) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val directionMapper: ComponentMapper[DirectionComponent] =
    ComponentMapper.getFor(classOf[DirectionComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] =
    ComponentMapper.getFor(classOf[WeaponComponent])


  val totalDashFrames: Float = 14f
  var dashing: Boolean = false
  var currentDashFrame: Float = 0f


  def dashFrameRatio: Float = {
    currentDashFrame / totalDashFrames
  }


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val directionComponent: DirectionComponent = directionMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    val startVelocity: Vector2 = bodyComponent.body.getLinearVelocity


    // Attack handling
    if (!weaponComponent.attacking && weaponComponent.frames == 0) {
      if (controllerHandler.attackPressed) {
        weaponComponent.startAttack(directionComponent.direction)
      }
    } else {
      controllerHandler.attackPressed = false
      weaponComponent.frames -= 1
      if (weaponComponent.frames == 8) {
        weaponComponent.endAttack()
      }
    }


    // Dash handling
    if (!dashing) {
      if (controllerHandler.dashPressed) {
        dashing = true
        currentDashFrame = totalDashFrames
      }
    } else {
      controllerHandler.dashPressed = false
      currentDashFrame -= 1

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

      if (currentDashFrame > (totalDashFrames / 5)) {
        bodyComponent.body.applyForceToCenter(
          normalizedVelocity.x * (7500 * dashFrameRatio),
          normalizedVelocity.y * (7500 * dashFrameRatio),
          true
        )
      } else if (currentDashFrame > 0) {
        bodyComponent.body.applyForceToCenter(
          normalizedVelocity.x * (-1000 * dashFrameRatio),
          normalizedVelocity.y * (-1000 * dashFrameRatio),
          true
        )
      } else {
        dashing = false
      }
    }


    // Regular movement if not attacking or dashing
    if (!weaponComponent.attacking && !dashing) {
      bodyComponent.body.setLinearVelocity(
        controllerHandler.leftAxis.x * 5,
        controllerHandler.leftAxis.y * 5
      )
    }

    // Allow player slight control during dash
    if (dashing) {
      bodyComponent.body.applyForceToCenter(
        controllerHandler.leftAxis.x * 500,
        controllerHandler.leftAxis.y * 500,
        true
      )
    }

    // Normalize diagonal movement
    val endVelocity: Vector2 = bodyComponent.body.getLinearVelocity
    if (endVelocity.len() > Constants.MAX_NORMAL_VELOCITY) {
      bodyComponent.body.setLinearVelocity(endVelocity.nor().scl(Constants.MAX_NORMAL_VELOCITY))
    }
  }
}
