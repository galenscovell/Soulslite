package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.steer.behaviors.FollowPath
import com.badlogic.gdx.ai.steer.utils.paths.LinePath
import com.badlogic.gdx.ai.steer.utils.paths.LinePath.LinePathParam
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.processing.BaseSteerable


class SteeringComponent(body: Body,
                        boundingRadius: Float,
                        maxLinearSpeed: Float,
                        maxLinearAcceleration: Float) extends Component {
  private val steerable: BaseSteerable = new BaseSteerable(
    body, boundingRadius, maxLinearSpeed, maxLinearAcceleration, 0, 0
  )


  def hasBehavior: Boolean = {
    steerable.behavior != null
  }

  def setNewFollowPath(path: LinePath[Vector2]): Unit = {
    steerable.behavior = new FollowPath[Vector2, LinePathParam](steerable, path, 1)
      .setEnabled(true)
      .setTimeToTarget(0.01f)
      .setArrivalTolerance(0.5f)
      .setDecelerationRadius(0.03f)
  }

  def getSteerable: BaseSteerable = steerable
  def getBehavior: SteeringBehavior[Vector2] = steerable.behavior
}
