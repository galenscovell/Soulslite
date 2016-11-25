package galenscovell.soulslite.processing.ai

import com.badlogic.gdx.ai.steer._
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body


class AISteerable(body: Body, boundingRadius: Float, maxLinearSpeed: Float, maxLinearAcceleration: Float,
                  maxAngularSpeed: Float, maxAngularAcceleration: Float) extends BaseSteerable(
  body, boundingRadius, maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration) {


  def getBehavior: SteeringBehavior[Vector2] = behavior
  def getSteerOutput: SteeringAcceleration[Vector2] = steerOutput
  def getBody: Body = body

  def setBehavior(b: SteeringBehavior[Vector2]): Unit = {
    behavior = b
  }

  /********************
    *      Update     *
    ********************/
  def update(delta: Float): Unit = {
    if (behavior != null) {
      behavior.calculateSteering(steerOutput)
      applySteering(delta)
    }
  }

  def applySteering(delta: Float): Unit = {
    var anyAccelerations: Boolean = false

    // Apply steeroutput to linear velocity if over threshold
    if (!steerOutput.linear.isZero(zeroLinearSpeedThreshold)) {
      body.applyForceToCenter(steerOutput.linear, true)
      anyAccelerations = true
    } else {
      body.setLinearVelocity(0, 0)
    }

    if (steerOutput.angular != 0) {
      println(steerOutput.angular)
      body.applyTorque(steerOutput.angular, true)
      anyAccelerations = true
    } else {
      val linearVelocity: Vector2 = getLinearVelocity
      if (!linearVelocity.isZero(zeroLinearSpeedThreshold)) {
        val newOrientation: Float = vectorToAngle(linearVelocity)
        body.setAngularVelocity(newOrientation - getAngularVelocity)
        body.setTransform(body.getPosition, newOrientation)
        anyAccelerations = true
      }
    }

    if (anyAccelerations) {
      // Linear speed capping
      val velocity: Vector2 = body.getLinearVelocity
      val currentSpeedSquare: Float = velocity.len2()
      if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
        body.setLinearVelocity(velocity.scl(maxLinearSpeed / Math.sqrt(currentSpeedSquare).toFloat))
      }

      // Angular speed capping
      if (body.getAngularVelocity > maxAngularSpeed) {
        body.setAngularVelocity(maxAngularSpeed)
      }
    }
  }
}
