package galenscovell.soulslite.processing.ai

import com.badlogic.gdx.ai.steer._
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.util.{Box2DLocation, SteeringUtil}


class SteeringAI(body: Body, boundingRadius: Float, var maxLinearSpeed: Float, var maxLinearAcceleration: Float,
                 var maxAngularSpeed: Float, var maxAngularAcceleration: Float) extends Steerable[Vector2] {
  private var zeroLinearSpeedThreshold: Float = 0f
  private var tagged = false
  private var behavior: SteeringBehavior[Vector2] = _
  private val steerOutput: SteeringAcceleration[Vector2] =
    new SteeringAcceleration[Vector2](new Vector2())



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

    if (!steerOutput.linear.isZero) {
      body.applyForceToCenter(steerOutput.linear, true)
      anyAccelerations = true
    }

    if (steerOutput.angular != 0) {
      body.applyTorque(steerOutput.angular, true)
      anyAccelerations = true
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


  /********************
    *       Get       *
    ********************/
  def getBehavior: SteeringBehavior[Vector2] = behavior
  def getSteerOutput: SteeringAcceleration[Vector2] = steerOutput
  def getBody: Body = body

  override def isTagged: Boolean = tagged

  override def getPosition: Vector2 = body.getPosition
  override def getOrientation: Float = body.getAngle

  override def getLinearVelocity: Vector2 = body.getLinearVelocity
  override def getMaxLinearSpeed: Float = maxLinearSpeed
  override def getMaxLinearAcceleration: Float = maxLinearAcceleration

  override def getAngularVelocity: Float = body.getAngularVelocity
  override def getMaxAngularSpeed: Float = maxAngularSpeed
  override def getMaxAngularAcceleration: Float = maxAngularAcceleration

  override def getBoundingRadius: Float = boundingRadius


  /********************
    *       Set       *
    ********************/
  def setBehavior(b: SteeringBehavior[Vector2]): Unit = {
    behavior = b
  }

  override def setTagged(tagged: Boolean): Unit = {
    this.tagged = tagged
  }

  override def vectorToAngle(vector: Vector2): Float = {
    SteeringUtil.vectorToAngle(vector)
  }

  override def angleToVector(outVector: Vector2, angle: Float): Vector2 = {
    SteeringUtil.angleToVector(outVector, angle)
  }

  override def setMaxLinearSpeed(maxLinearSpeed: Float): Unit = {
    this.maxLinearSpeed = maxLinearSpeed
  }

  override def setMaxLinearAcceleration(maxLinearAcceleration: Float): Unit = {
    this.maxLinearAcceleration = maxLinearAcceleration
  }

  override def setMaxAngularSpeed(maxAngularSpeed: Float): Unit = {
    this.maxAngularSpeed = maxAngularSpeed
  }

  override def setMaxAngularAcceleration(maxAngularAcceleration: Float): Unit = {
    this.maxAngularAcceleration = maxAngularAcceleration
  }

  override def setOrientation(orientation: Float): Unit = {
    body.setTransform(body.getPosition, orientation)
  }



  override def newLocation(): Location[Vector2] = {
    new Box2DLocation
  }

  override def getZeroLinearSpeedThreshold: Float = {
    zeroLinearSpeedThreshold
  }

  override def setZeroLinearSpeedThreshold(value: Float): Unit = {
    this.zeroLinearSpeedThreshold = value
  }
}
