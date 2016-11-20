package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.ControllerHandler
import galenscovell.soulslite.util.Constants


class InputSystem(family: Family, controllerHandler: ControllerHandler) extends IteratingSystem(family) {
  private val velocityMapper: ComponentMapper[VelocityComponent] = ComponentMapper.getFor(classOf[VelocityComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] = ComponentMapper.getFor(classOf[BodyComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] = ComponentMapper.getFor(classOf[WeaponComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val velocity: VelocityComponent = velocityMapper.get(entity)
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    // Attack handling - attack is 8 frames with 8 waitframes (16 frames total)
    if (!weaponComponent.attacking && weaponComponent.frames == 0) {
      if (controllerHandler.attackPressed) {
        weaponComponent.startAttack(velocity.direction)
      }
    } else {
      controllerHandler.attackPressed = false
      weaponComponent.frames -= 1
      if (weaponComponent.frames == 8) {
        weaponComponent.endAttack()
      }
    }

    // Dash handling - dash is 10 frames with 6 waitframes (16 frames total)
    if (!velocity.dashing && velocity.dashframes == 0) {
      if (controllerHandler.dashPressed) {
        velocity.startDash()
      }
    } else {
      controllerHandler.dashPressed = false
      velocity.dashframes -= 1
      if (velocity.dashframes > 6) {
        if (!velocity.inMotion) {
          velocity.direction match {
            case 0 => velocity.v.x += 0.5f
            case 1 => velocity.v.y += 0.5f
            case 2 => velocity.v.x -= 0.5f
            case 3 => velocity.v.y -= 0.5f
          }
        }

        val normalizedVelocity: Vector2 = velocity.v.nor()
        bodyComponent.body.applyForceToCenter(normalizedVelocity.x * 4600, normalizedVelocity.y * 4600, true)
      } else if (velocity.dashframes == 6) {
        velocity.endDash()
      }
    }

    // Regular movement
    if (!weaponComponent.attacking && !velocity.dashing) {
      velocity.v.x = controllerHandler.leftAxis.x * 5
      velocity.v.y = controllerHandler.leftAxis.y * 5

      // Normalize diagonal movement
      if (velocity.v.len() > Constants.MAX_NORMAL_VELOCITY) {
        velocity.v.nor().scl(Constants.MAX_NORMAL_VELOCITY)
      }
    }

    velocity.angle = velocity.v.angle()
  }
}
