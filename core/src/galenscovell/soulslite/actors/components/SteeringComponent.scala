package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.behaviors.Arrive
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.processing.BaseSteerable


class SteeringComponent(body: Body,
                        boundingRadius: Float,
                        maxLinearSpeed: Float,
                        maxLinearAcceleration: Float) extends Component {
  val steerable: BaseSteerable = new BaseSteerable(
    body, boundingRadius, maxLinearSpeed, maxLinearAcceleration, 0, 0
  )
  val nodeArriveBehavior: Arrive[Vector2] = new Arrive[Vector2](steerable)
    .setEnabled(true)
    .setTimeToTarget(0.005f)       // Time over which to achieve target speed
    .setArrivalTolerance(0.05f)    // Distance at which entity has 'arrived'
    .setDecelerationRadius(0.005f) // Distance at which deceleration begins

  steerable.behavior = nodeArriveBehavior
}
