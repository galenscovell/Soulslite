package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.steer._
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.BaseSteerable
import galenscovell.soulslite.processing.pathfinding._


class AISystem(family: Family, aStarGraph: AStarGraph) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val pathMapper: ComponentMapper[PathComponent] =
    ComponentMapper.getFor(classOf[PathComponent])
  private val stateMapper: ComponentMapper[StateComponent] =
    ComponentMapper.getFor(classOf[StateComponent])
  private val steeringMapper: ComponentMapper[SteeringComponent] =
    ComponentMapper.getFor(classOf[SteeringComponent])

  private val steerOutput: SteeringAcceleration[Vector2] =
    new SteeringAcceleration[Vector2](new Vector2())
  private val pathfinder: Pathfinder = new Pathfinder(aStarGraph)


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val body: Body = bodyMapper.get(entity).body
    val pathComponent: PathComponent = pathMapper.get(entity)
    val stateComponent: StateComponent = stateMapper.get(entity)
    val steeringComponent: SteeringComponent = steeringMapper.get(entity)

    val entityPosition: Vector2 = body.getPosition

    // Pathfinding
    if (pathComponent.tick()) {
      val playerPosition: Vector2 = stateComponent.getPlayerPosition
      pathComponent.path.clear()

      val foundPath: Array[Node] = pathfinder.findPath(entityPosition, playerPosition)
      if (foundPath != null && foundPath.nonEmpty) {
        for (node: Node <- foundPath) {
          pathComponent.path.push(node)
        }
        steeringComponent.nodeArriveBehavior.setTarget(pathComponent.path.pop())
      }
    }

    if (pathComponent.path.nonEmpty) {
      val currentTargetPosition: Vector2 = steeringComponent.nodeArriveBehavior.getTarget.getPosition
      if (pathComponent.nodeReached(currentTargetPosition, entityPosition)) {
        steeringComponent.nodeArriveBehavior.setTarget(pathComponent.path.pop())
      }
    }

    // AI is using steering behavior
    if (steeringComponent.steerable.behavior != null && steeringComponent.nodeArriveBehavior.getTarget != null) {
      steeringComponent.steerable.behavior.calculateSteering(steerOutput)
      applySteering(deltaTime, body, steeringComponent.steerable)
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
