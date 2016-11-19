package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class WeaponComponent(world: World, entityBody: Body) extends Component {
  val weaponBody: Body = createBody
  val weaponFixture: Fixture = createFixture


  private def createBody: Body = {
    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.DynamicBody
    //    bodyDef.fixedRotation = true
    bodyDef.angularDamping = 1f
    bodyDef.linearDamping = 0.1f
//    bodyDef.position.set(
//      entityBody.getPosition.x / Constants.PIXEL_PER_METER,
//      entityBody.getPosition.y / Constants.PIXEL_PER_METER
//    )

    world.createBody(bodyDef)
  }

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
    fixtureDef.density = 0.05f
    fixtureDef.friction = 0f
    fixtureDef.isSensor = false
    fixtureDef.filter.categoryBits = Constants.ENTITY_CATEGORY
    fixtureDef.filter.maskBits = Constants.ENTITY_MASK
    val fixture: Fixture = entityBody.createFixture(fixtureDef)

    shape.dispose()
    fixture
  }

  def swing(angle: Float): Unit = {
    entityBody.setTransform(entityBody.getPosition, 10f)
    entityBody.applyAngularImpulse(2f, true)
  }

  def endSwing(): Unit = {
    entityBody.setAngularVelocity(0)
  }
}
