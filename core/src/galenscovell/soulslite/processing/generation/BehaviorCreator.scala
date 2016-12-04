package galenscovell.soulslite.processing.generation

import com.badlogic.gdx.ai.steer.behaviors._
import com.badlogic.gdx.ai.steer.proximities._
import com.badlogic.gdx.ai.steer.utils.RayConfiguration
import com.badlogic.gdx.ai.steer.utils.rays.{CentralRayWithWhiskersConfiguration, SingleRayConfiguration}
import com.badlogic.gdx.ai.steer.{Steerable, SteeringBehavior}
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector
import com.badlogic.gdx.math.{MathUtils, Vector2}
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import galenscovell.soulslite.processing.BaseSteerable
import galenscovell.soulslite.util.Box2DRaycastCollisionDetector


class BehaviorCreator(world: World) {

  // Arrive at a set distance from the target steerable, attempting to have zero velocity upon arrival
  def makeArriveBehavior(entitySteerable: BaseSteerable,
                         targetSteerable: BaseSteerable): Arrive[Vector2] = {
    new Arrive[Vector2](entitySteerable, targetSteerable)
      .setEnabled(true)
      .setTimeToTarget(0.05f)     // Time over which to achieve target speed
      .setArrivalTolerance(3f)    // Distance at which entity has 'arrived'
      .setDecelerationRadius(4f)  // Distance at which deceleration begins
  }

  // Pursue the target steerable using a processTime limit
  def makePursueBehavior(entitySteerable: BaseSteerable,
                         targetSteerable: BaseSteerable): Pursue[Vector2] = {
    new Pursue[Vector2](entitySteerable, targetSteerable, 2f)
      .setEnabled(true)
  }

  // Run from the target steerable using a processTime limit
  def makeEvadeBehavior(entitySteerable: BaseSteerable,
                        targetSteerable: BaseSteerable): Evade[Vector2] = {
    new Evade[Vector2](entitySteerable, targetSteerable, 2f)
      .setEnabled(true)
  }

  // Move towards the target steerable with no thought
  def makeSeekBehavior(entitySteerable: BaseSteerable,
                       targetSteerable: BaseSteerable): Seek[Vector2] = {
    new Seek[Vector2](entitySteerable, targetSteerable)
      .setEnabled(true)
  }

  // Move away from the target with no thought
  def makeFleeBehavior(entitySteerable: BaseSteerable,
                       targetSteerable: BaseSteerable): Flee[Vector2] = {
    new Flee[Vector2](entitySteerable, targetSteerable)
      .setEnabled(true)
  }

  // Hide behind the nearest 'obstacle'
  // Obstacles are other Steerables that don't move, ie props on the map such as pillars
  def makeHideBehavior(entitySteerable:BaseSteerable,
                       targetSteerable: BaseSteerable,
                       obstacles: Array[Steerable[Vector2]]): Hide[Vector2] = {
    val radiusProximity: RadiusProximity[Vector2] =
      new RadiusProximity[Vector2](entitySteerable, obstacles, entitySteerable.getBoundingRadius + 1)
    val infiniteProximity: InfiniteProximity[Vector2] =
      new InfiniteProximity[Vector2](entitySteerable, obstacles)
    new Hide[Vector2](entitySteerable, targetSteerable, radiusProximity)
      .setEnabled(true)
      .setDistanceFromBoundary(1)
      .setTimeToTarget(0.1f)
      .setArrivalTolerance(0.25f)
      .setDecelerationRadius(2)
  }

  // Wander aimlessly
  // This utilizes a 'wander circle' around the entity -- a random spot from the circumference of the circle
  //  is chosen for the new target location
  // If GdxAI.getTimepiece.getTime is not called on update, the wander orientation will not change
  def makeWanderBehavior(entitySteerable: BaseSteerable): Wander[Vector2] = {
    new Wander[Vector2](entitySteerable)
      .setEnabled(true)
      .setWanderOffset(3)               // Distance from entity for the wander circle to operate
      .setWanderOrientation(3)          //
      .setWanderRadius(2)               // Radius of wander circle
      .setWanderRate(MathUtils.PI2 * 4) // Rate at which new spot is chosen from the wander circle
  }

  // Collision avoidance using proximity, works well for obstacles that can be represented by a point and radius
  // Obstacles are other Steerables that don't move, ie props on the map such as pillars
  def makeCollisionAvoidBehavior(entitySteerable: BaseSteerable,
                                 obstacles: Array[Steerable[Vector2]]): CollisionAvoidance[Vector2] = {
    val radiusProximity: RadiusProximity[Vector2] =
      new RadiusProximity[Vector2](entitySteerable, obstacles, entitySteerable.getBoundingRadius + 1)
    new CollisionAvoidance[Vector2](entitySteerable, radiusProximity)
      .setEnabled(true)
  }

  // Collision avoidance using raycasting, works well for large collisions such as world boundaries
  def makeRaycastAvoidBehavior(entitySteerable: BaseSteerable): RaycastObstacleAvoidance[Vector2] = {
    val whiskerRays: RayConfiguration[Vector2] =
      new CentralRayWithWhiskersConfiguration[Vector2](entitySteerable, 4f, 2f, 35 * MathUtils.degreesToRadians)
    val collisionDetector: RaycastCollisionDetector[Vector2] =
      new Box2DRaycastCollisionDetector(world)
    new RaycastObstacleAvoidance[Vector2](entitySteerable, whiskerRays, collisionDetector, entitySteerable.getBoundingRadius + 2)
        .setEnabled(true)
  }

  // Combination of list of behaviors
  // These are operated on in order -- if the first behavior fires successfully to produce
  //  an impulse over the threshold, then no others will
  def makePrioritySteering(entitySteerable: BaseSteerable,
                           behaviors: List[SteeringBehavior[Vector2]]): PrioritySteering[Vector2] = {
    val prioritySteering: PrioritySteering[Vector2] = new PrioritySteering[Vector2](
      entitySteerable,
      0.01f  // Impulse threshold
    )
    for (behavior: SteeringBehavior[Vector2] <- behaviors) {
      prioritySteering.add(behavior)
    }
    prioritySteering
  }

  // Combination of list of behaviors (with accompanying list of weights)
  // The result of each behavior is summed to determine the next target
  def makeBlendedSteering(entitySteerable: BaseSteerable,
                          behaviors: List[SteeringBehavior[Vector2]],
                          weights: List[Float]): BlendedSteering[Vector2] = {
    assert(behaviors.length == weights.length)
    val blendedSteering: BlendedSteering[Vector2] = new BlendedSteering[Vector2](entitySteerable)
    for (i <- behaviors.indices) {
      blendedSteering.add(behaviors(i), weights(i))
    }
    blendedSteering
  }
}
