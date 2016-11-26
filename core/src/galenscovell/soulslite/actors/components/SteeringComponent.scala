package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.processing.BaseSteerable


class SteeringComponent(body: Body,
                        boundingRadius: Float,
                        maxLinearSpeed: Float,
                        maxLinearAcceleration: Float,
                        maxAngularSpeed: Float,
                        maxAngularAcceleration: Float) extends Component {
  val steering: BaseSteerable = new BaseSteerable(
    body, boundingRadius, maxLinearSpeed, maxLinearAcceleration, maxAngularSpeed, maxAngularAcceleration
  )
}
