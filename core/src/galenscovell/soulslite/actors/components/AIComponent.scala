package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.processing.ai.AISteerable


class AIComponent(body: Body, boundingRadius: Float, maxLinearSpeed: Float,
                  maxLinearAcceleration: Float, maxAngularSpeed: Float,
                  maxAngularAcceleration: Float) extends Component {
  val steering: AISteerable = new AISteerable(
    body, boundingRadius, maxLinearSpeed, maxLinearAcceleration,
    maxAngularSpeed, maxAngularAcceleration
  )


  def setBehavior(behavior: SteeringBehavior[Vector2]): Unit = {
    steering.setBehavior(behavior)
  }
}
