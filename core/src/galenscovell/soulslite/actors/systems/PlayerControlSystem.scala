package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.ControllerHandler
import galenscovell.soulslite.util.Constants


class PlayerControlSystem(family: Family, controllerHandler: ControllerHandler) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val velocityMapper: ComponentMapper[VelocityComponent] =
    ComponentMapper.getFor(classOf[VelocityComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] =
    ComponentMapper.getFor(classOf[WeaponComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val velocityComponent: VelocityComponent = velocityMapper.get(entity)
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    // Attack handling - attack is 8 frames with 8 waitframes (16 frames total)
    if (!weaponComponent.attacking && weaponComponent.frames == 0) {
      if (controllerHandler.attackPressed) {
        weaponComponent.startAttack(velocityComponent.direction)
      }
    } else {
      controllerHandler.attackPressed = false
      weaponComponent.frames -= 1
      if (weaponComponent.frames == 8) {
        weaponComponent.endAttack()
      }
    }

    // Dash handling - dash is 10 frames with 6 waitframes (16 frames total)
    if (!velocityComponent.dashing && velocityComponent.dashframes == 0) {
      if (controllerHandler.dashPressed) {
        velocityComponent.startDash()
      }
    } else {
      controllerHandler.dashPressed = false
      velocityComponent.dashframes -= 1
      if (velocityComponent.dashframes > 6) {
        if (!velocityComponent.inMotion) {
          velocityComponent.direction match {
            case 0 => velocityComponent.v.x += 0.5f
            case 1 => velocityComponent.v.y += 0.5f
            case 2 => velocityComponent.v.x -= 0.5f
            case 3 => velocityComponent.v.y -= 0.5f
          }
        }

        val normalizedVelocity: Vector2 = velocityComponent.v.nor()
        bodyComponent.body.applyForceToCenter(normalizedVelocity.x * 4600, normalizedVelocity.y * 4600, true)
      } else if (velocityComponent.dashframes == 6) {
        velocityComponent.endDash()
      }
    }

    // Regular movement
    if (!weaponComponent.attacking && !velocityComponent.dashing) {
      velocityComponent.v.x = controllerHandler.leftAxis.x * 5
      velocityComponent.v.y = controllerHandler.leftAxis.y * 5

      // Normalize diagonal movement
      if (velocityComponent.v.len() > Constants.MAX_NORMAL_VELOCITY) {
        velocityComponent.v.nor().scl(Constants.MAX_NORMAL_VELOCITY)
      }
    }

    velocityComponent.angle = velocityComponent.v.angle()
  }
}
