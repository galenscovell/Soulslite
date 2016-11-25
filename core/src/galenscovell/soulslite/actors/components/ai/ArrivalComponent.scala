package galenscovell.soulslite.actors.components.ai

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.processing.ai.SteeringAI


class ArrivalComponent(body: Body, boundingRadius: Float, maxLinearSpeed: Float,
                       maxLinearAcceleration: Float, maxAngularSpeed: Float,
                       maxAngularAcceleration: Float) extends Component {
  val steeringAI: SteeringAI = new SteeringAI(
    body, boundingRadius, maxLinearSpeed, maxLinearAcceleration,
    maxAngularSpeed, maxAngularAcceleration
  )


  def setBehavior(behavior: SteeringBehavior[Vector2]): Unit = {
    steeringAI.setBehavior(behavior)
  }
}
