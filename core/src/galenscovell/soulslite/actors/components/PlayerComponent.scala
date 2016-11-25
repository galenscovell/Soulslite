package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.processing.ai.BaseSteerable


class PlayerComponent(body: Body) extends Component {
  val steering: BaseSteerable = new BaseSteerable(body, 1f, 5f, 30f, 5f, 30f)
}
