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
  private val agentStateMapper: ComponentMapper[AgentStateComponent] =
    ComponentMapper.getFor(classOf[AgentStateComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val pathMapper: ComponentMapper[PathComponent] =
    ComponentMapper.getFor(classOf[PathComponent])
  private val steeringMapper: ComponentMapper[SteeringComponent] =
    ComponentMapper.getFor(classOf[SteeringComponent])

  private val steerOutput: SteeringAcceleration[Vector2] =
    new SteeringAcceleration[Vector2](new Vector2())
  private val pathfinder: Pathfinder = new Pathfinder(aStarGraph)


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val agentStateComponent: AgentStateComponent = agentStateMapper.get(entity)
    val body: Body = bodyMapper.get(entity).body
    val pathComponent: PathComponent = pathMapper.get(entity)
    val steeringComponent: SteeringComponent = steeringMapper.get(entity)

    pathfind(body, pathComponent, agentStateComponent, steeringComponent)

    if (steeringComponent.hasBehavior) {
      steeringComponent.getBehavior.calculateSteering(steerOutput)
      applySteering(deltaTime, body, steeringComponent.getSteerable)
    } else {
      body.setLinearVelocity(0, 0)
    }
  }

  def pathfind(body: Body, pathComponent: PathComponent, stateComponent: AgentStateComponent,
               steeringComponent: SteeringComponent): Unit = {
    val entityPosition: Vector2 = body.getPosition

    // Pathfinding happens at set intervals (depending on entity type?)
    if (pathComponent.tick()) {
      // Locate player and clear old path
      val playerPosition: Vector2 = stateComponent.getPlayerPosition

      // Find path to player
      val foundPath: Array[Node] = pathfinder.findPath(entityPosition, playerPosition)

      // Add path nodes to path and set first node as next target location
      if (foundPath != null && foundPath.length > 1) {
        pathComponent.setLinePath(foundPath)
        steeringComponent.setNewFollowPath(pathComponent.getLinePath)
      }
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
