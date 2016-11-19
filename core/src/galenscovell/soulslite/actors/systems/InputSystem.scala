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

  private var dashFrames: Int = 0
  private var waitFrames: Int = 0
  private var dashing: Boolean = false


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val velocity: VelocityComponent = velocityMapper.get(entity)
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    if (controllerHandler.attackPressed) {
      weaponComponent.swing(0)
    } else {
      // weaponComponent.endSwing()
    }

    if (!dashing) {
      if (controllerHandler.dashPressed) {
        if (waitFrames == 0) {
          dashing = true
          waitFrames = 6
        }
      } else {
        if (waitFrames > 0) {
          waitFrames -= 1
        }
        velocity.v.x = controllerHandler.leftAxis.x * 5
        velocity.v.y = controllerHandler.leftAxis.y * 5

        // Normalize diagonal movement
        if (velocity.v.len() > Constants.MAX_NORMAL_VELOCITY) {
          velocity.v.nor().scl(Constants.MAX_NORMAL_VELOCITY)
        }
      }
    } else {
      velocity.dashing = true
      dashFrames += 1
      if (dashFrames < 10) {
        val normalizedVelocity: Vector2 = velocity.v.nor()
        bodyComponent.body.applyForceToCenter(normalizedVelocity.x * 4000, normalizedVelocity.y * 4000, true)
      } else if (dashFrames == 10) {
        velocity.v.x = 0
        velocity.v.y = 0
      } else if (dashFrames == 14) {
        velocity.dashing = false
        dashFrames = 0
        dashing = false
      }
    }

    velocity.angle = velocity.v.angle()
  }
}
