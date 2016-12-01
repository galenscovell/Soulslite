package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class WeaponComponent(world: World, body: Body, attackShape: Array[Vector2],
                      startAngle: Float, endAngle: Float, attackSpeed: Float) extends Component {
  // attackShape is the vertices defining the entity weapon shape
  // startAngle is where the attack will being from relative to the entity direction
  // endAngle is where the attack stops
  // attackSpeed is the velocity applied
  val weaponFixture: Fixture = createFixture


  private def createFixture: Fixture = {
//    val shape: CircleShape = new CircleShape()
//    shape.setRadius(0.2f)
    val shape: PolygonShape = new PolygonShape()
    shape.set(attackShape)

    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 0f
    fixtureDef.friction = 0f
    fixtureDef.isSensor = true
    fixtureDef.filter.categoryBits = Constants.NO_CATEGORY
    fixtureDef.filter.maskBits = Constants.ATTACK_OFF_MASK
    val fixture: Fixture = body.createFixture(fixtureDef)
    fixture.setUserData(this)

    shape.dispose()
    fixture
  }

  def startAttack(direction: Int): Unit = {
    val filter: Filter = weaponFixture.getFilterData
    filter.categoryBits = Constants.ATTACK_CATEGORY
    filter.maskBits = Constants.ATTACK_ON_MASK
    weaponFixture.setFilterData(filter)

    // Direction is ordinal - will need to be played with to change where
    //  attack angle starts and ends
    // This start angle will be 45deg in relation to ordinal direction
    val angle: Float = Math.toRadians(direction * startAngle).toFloat
    body.setTransform(body.getPosition, angle)
    body.applyAngularImpulse(-attackSpeed, true)
  }

  def endAttack(): Unit = {
    val filter: Filter = weaponFixture.getFilterData
    filter.categoryBits = Constants.NO_CATEGORY
    filter.maskBits = Constants.ATTACK_OFF_MASK
    weaponFixture.setFilterData(filter)

    body.setAngularVelocity(0)
  }
}
