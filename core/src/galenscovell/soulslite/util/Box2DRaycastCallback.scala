package galenscovell.soulslite.util

import com.badlogic.gdx.ai.utils.Collision
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{Fixture, RayCastCallback}


class Box2DRaycastCallback extends RayCastCallback {
  var outputCollision: Collision[Vector2] = _
  var collided: Boolean = false


  override def reportRayFixture(fixture: Fixture, point: Vector2, normal: Vector2, fraction: Float): Float = {
    if (outputCollision != null) {
      outputCollision.set(point, normal)
    }
    collided = true
    fraction
  }
}
