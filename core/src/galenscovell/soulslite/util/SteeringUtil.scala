package galenscovell.soulslite.util

import com.badlogic.gdx.math.Vector2


object SteeringUtil {
  def vectorToAngle(vector: Vector2): Float = {
    Math.atan2(-vector.x, vector.y).toFloat
  }

  def angleToVector(outVector: Vector2, angle: Float): Vector2 = {
    outVector.x = -Math.sin(angle).toFloat
    outVector.y = Math.cos(angle).toFloat
    outVector
  }
}
