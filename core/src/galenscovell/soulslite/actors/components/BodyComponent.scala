package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d._


class BodyComponent(body: Body, fixture: Fixture) extends Component {


  def getBody: Body = {
    body
  }

  def getFixture: Fixture = {
    fixture
  }
}
