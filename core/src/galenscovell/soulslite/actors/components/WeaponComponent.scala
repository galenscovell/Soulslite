package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class WeaponComponent(world: World, entityBody: Body) extends Component {
  val weaponFixture: Fixture = createFixture


  private def createFixture: Fixture = {
    // Entities will have differently shaped weapon fixtures
    //  to accommodate different attack styles
//    val shape: CircleShape = new CircleShape()
//    shape.setRadius(0.2f)
    val shape: PolygonShape = new PolygonShape()
    // The shape of the fixture is defined here relative to the entity body
    val vertices = new Array[Vector2](4)
    vertices(0) = new Vector2(0.75f, 0.75f)
    vertices(1) = new Vector2(0.65f, 0.5f)
    vertices(2) = new Vector2(0.5f, 0.65f)
    vertices(3) = new Vector2(1.25f, 1.25f)
    shape.set(vertices)

    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 0f
    fixtureDef.friction = 0f
    fixtureDef.isSensor = true
    fixtureDef.filter.categoryBits = Constants.NO_CATEGORY
    fixtureDef.filter.maskBits = Constants.ATTACK_OFF_MASK
    val fixture: Fixture = entityBody.createFixture(fixtureDef)
    fixture.setUserData(this)

    shape.dispose()
    fixture
  }

  def enable(): Unit = {
    val filter: Filter = weaponFixture.getFilterData
    filter.categoryBits = Constants.ATTACK_CATEGORY
    filter.maskBits = Constants.ATTACK_ON_MASK
    weaponFixture.setFilterData(filter)
  }

  def disable(): Unit = {
    val filter: Filter = weaponFixture.getFilterData
    filter.categoryBits = Constants.NO_CATEGORY
    filter.maskBits = Constants.ATTACK_OFF_MASK
    weaponFixture.setFilterData(filter)
  }

  def startAttack(direction: Int): Unit = {
    // Direction is ordinal - will need to be played with to change where
    //  attack angle starts and ends
    enable()
    // This angle will be 45deg in relation to ordinal direction
    val angle: Float = Math.toRadians(direction * 90).toFloat
    entityBody.setTransform(entityBody.getPosition, angle)
    entityBody.applyAngularImpulse(-5f, true)
  }

  def endAttack(): Unit = {
    disable()
    entityBody.setAngularVelocity(0)
  }
}
