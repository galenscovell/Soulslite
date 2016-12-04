package galenscovell.soulslite.util

import com.badlogic.gdx.ai.utils._
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._


class Box2DRaycastCollisionDetector(world: World) extends RaycastCollisionDetector[Vector2] {
  var callback: Box2DRaycastCallback = new Box2DRaycastCallback


  override def collides(ray: Ray[Vector2]): Boolean = {
    findCollision(null, ray)
  }

  override def findCollision(outputCollision: Collision[Vector2], inputRay: Ray[Vector2]): Boolean = {
    callback.collided = false

    if (!inputRay.start.epsilonEquals(inputRay.end, 0.1f)) {
      callback.outputCollision = outputCollision
      world.rayCast(callback, inputRay.start, inputRay.end)
    }

    callback.collided
  }



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
}
