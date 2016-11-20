package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class WeaponComponent(world: World, entityBody: Body) extends Component {
  val weaponFixture: Fixture = createFixture
  var attacking: Boolean = false
  var frames: Int = 0


  private def createFixture: Fixture = {
//    val shape: CircleShape = new CircleShape()
//    shape.setRadius(0.2f)
    val shape: PolygonShape = new PolygonShape()
    val vertices = new Array[Vector2](4)
    vertices(0) = new Vector2(0, 0)
    vertices(1) = new Vector2(0, 0.5f)
    vertices(2) = new Vector2(0.5f, 0)
    vertices(3) = new Vector2(1.25f, 1.25f)
    shape.set(vertices)

    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 0.01f
    fixtureDef.friction = 0f
    fixtureDef.isSensor = false
    fixtureDef.filter.categoryBits = Constants.ENTITY_CATEGORY
    fixtureDef.filter.maskBits = Constants.ENTITY_MASK
    val fixture: Fixture = entityBody.createFixture(fixtureDef)

    shape.dispose()
    fixture
  }

  def startAttack(direction: Int): Unit = {
    attacking = true
    frames = 16

    val angle: Float = Math.toRadians(direction * 90).toFloat
    entityBody.setTransform(entityBody.getPosition, angle)
    // entityBody.setAngularVelocity(-12f)
    entityBody.applyAngularImpulse(-4f, true)
  }

  def endAttack(): Unit = {
    attacking = false

    entityBody.setAngularVelocity(0)
  }
}
