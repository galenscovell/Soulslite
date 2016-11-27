package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.steer._
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components.{BodyComponent, SteeringComponent}
import galenscovell.soulslite.processing.steering.BaseSteerable


class SteeringSystem(family: Family) extends IteratingSystem(family) {
  private val steeringMapper: ComponentMapper[SteeringComponent] =
    ComponentMapper.getFor(classOf[SteeringComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])

  private val steerOutput: SteeringAcceleration[Vector2] = new SteeringAcceleration[Vector2](new Vector2())


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val steering: BaseSteerable = steeringMapper.get(entity).steering
    val body: Body = bodyMapper.get(entity).body

    if (steering.behavior != null) {
      steering.behavior.calculateSteering(steerOutput)
      applySteering(deltaTime, body, steering)
    }
  }

  def applySteering(delta: Float, body: Body, steering: BaseSteerable): Unit = {
    var anyAccelerations: Boolean = false

    // Apply steeroutput to linear velocity if over threshold
    if (!steerOutput.linear.isZero(steering.zeroLinearSpeedThreshold)) {
      body.applyForceToCenter(steerOutput.linear, true)
      anyAccelerations = true
    } else {
      body.setLinearVelocity(0, 0)
    }

    if (steerOutput.angular != 0) {
      body.applyTorque(steerOutput.angular, true)
      anyAccelerations = true
    } else {
      val linearVelocity: Vector2 = body.getLinearVelocity
      if (!linearVelocity.isZero(steering.zeroLinearSpeedThreshold)) {
        val newOrientation: Float = steering.vectorToAngle(linearVelocity)
        body.setAngularVelocity(newOrientation - body.getAngularVelocity)
        body.setTransform(body.getPosition, newOrientation)
        anyAccelerations = true
      }
    }

    if (anyAccelerations) {
      // Linear speed capping
      val velocity: Vector2 = body.getLinearVelocity
      val currentSpeedSquare: Float = velocity.len2()
      if (currentSpeedSquare > steering.maxLinearSpeed * steering.maxLinearSpeed) {
        body.setLinearVelocity(velocity.scl(steering.maxLinearSpeed / Math.sqrt(currentSpeedSquare).toFloat))
      }

      // Angular speed capping
      if (body.getAngularVelocity > steering.maxAngularSpeed) {
        body.setAngularVelocity(steering.maxAngularSpeed)
      }
    }
  }
}
