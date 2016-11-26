package galenscovell.soulslite.util

import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2


class Box2DLocation(position: Vector2) extends Location[Vector2] {
  var orientation: Float = 0f

  def this() {
    this(new Vector2())
  }


  override def getPosition: Vector2 = {
    position
  }

  override def getOrientation: Float = {
    orientation
  }

  override def setOrientation(orientation: Float): Unit = {
    this.orientation = orientation
  }

  override def vectorToAngle(vector: Vector2): Float = {
    Box2DSteeringUtils.vectorToAngle(vector)
  }
  override def angleToVector(outVector: Vector2, angle: Float): Vector2 = {
    Box2DSteeringUtils.angleToVector(outVector, angle)
  }

  override def newLocation(): Location[Vector2] = {
    new Box2DLocation
  }
}
