package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.{Component, Entity}
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util._


class BodyComponent(entity: Entity, world: World, posX: Float, posY: Float,
                    size: Float) extends Component{
  val body: Body = createBody
  val fixture: Fixture = createFixture
  body.setUserData(entity)
  fixture.setUserData(this)


  private def createBody: Body = {
    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.DynamicBody
//    bodyDef.fixedRotation = true
    bodyDef.angularDamping = 1f
    bodyDef.linearDamping = 0.1f
    bodyDef.position.set(posX, posY)

    world.createBody(bodyDef)
  }

  private def createFixture: Fixture = {
//    val shape: PolygonShape = new PolygonShape()
//    shape.setAsBox(Constants.MID_ENTITY_SIZE / 3, Constants.MID_ENTITY_SIZE / 3)
    val shape: CircleShape = new CircleShape()
    shape.setRadius(size / 3)

    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 1f
    fixtureDef.friction = 0.1f
    fixtureDef.filter.categoryBits = Constants.ENTITY_CATEGORY
    fixtureDef.filter.maskBits = Constants.ENTITY_MASK
    val fixture: Fixture = body.createFixture(fixtureDef)

    shape.dispose()
    fixture
  }

  def updateCollision(newCategory: Short, newMask: Short): Unit = {
    val filter: Filter = fixture.getFilterData
    filter.categoryBits = newCategory
    filter.maskBits = newMask
    fixture.setFilterData(filter)
  }
}
