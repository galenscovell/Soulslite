package galenscovell.soulslite.processing

import com.badlogic.gdx.ai.steer._
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.util.{Box2DLocation, Box2DSteeringUtils}


class BaseSteerable(body: Body,
                    boundingRadius: Float,
                    var maxLinearSpeed: Float,
                    var maxLinearAcceleration: Float,
                    var maxAngularSpeed: Float,
                    var maxAngularAcceleration: Float) extends Steerable[Vector2] {
  var zeroLinearSpeedThreshold: Float = 0.005f
  var tagged = false
  var behavior: SteeringBehavior[Vector2] = _


  /********************
    *       Get       *
    ********************/
  override def isTagged: Boolean = tagged

  override def getPosition: Vector2 = body.getPosition
  override def getOrientation: Float = body.getAngle
  override def getBoundingRadius: Float = boundingRadius

  override def getLinearVelocity: Vector2 = body.getLinearVelocity
  override def getMaxLinearSpeed: Float = maxLinearSpeed
  override def getMaxLinearAcceleration: Float = maxLinearAcceleration

  override def getAngularVelocity: Float = body.getAngularVelocity
  override def getMaxAngularSpeed: Float = maxAngularSpeed
  override def getMaxAngularAcceleration: Float = maxAngularAcceleration


  /********************
    *       Set       *
    ********************/
  override def setTagged(tagged: Boolean): Unit = {
    this.tagged = tagged
  }

  override def vectorToAngle(vector: Vector2): Float = {
    Box2DSteeringUtils.vectorToAngle(vector)
  }

  override def angleToVector(outVector: Vector2, angle: Float): Vector2 = {
    Box2DSteeringUtils.angleToVector(outVector, angle)
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
