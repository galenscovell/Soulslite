package galenscovell.soulslite.util

import com.badlogic.gdx.ai.utils._
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World;


class Box2DRaycastCollisionDetector(world: World) extends RaycastCollisionDetector[Vector2] {
  var callback: Box2DRaycastCallback = new Box2DRaycastCallback


  override def collides(ray: Ray[Vector2]): Boolean = {
    findCollision(null, ray)
  }

  override def findCollision(outputCollision: Collision[Vector2], inputRay: Ray[Vector2]): Boolean = {
    callback.collided = false

    if (!inputRay.start.epsilonEquals(inputRay.end, 0.00001f)) {
      callback.outputCollision = outputCollision
      world.rayCast(callback, inputRay.start, inputRay.end)
    }

    callback.collided
  }
}
