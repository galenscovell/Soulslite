package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.util.Constants


class BodyComponent(world: World, posX: Float, posY: Float, size: Int) extends Component {
  val body: Body = createBody
  val fixture: Fixture = createFixture


  private def createBody: Body = {
    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.DynamicBody
    // bodyDef.fixedRotation = true
    bodyDef.angularDamping = 1f
    bodyDef.position.set(posX, posY)

    world.createBody(bodyDef)
  }

  private def createFixture: Fixture = {
    //    val shape: PolygonShape = new PolygonShape()
    //    shape.setAsBox(Constants.ENTITY_SIZE / 3, Constants.ENTITY_SIZE / 3)
    val shape: CircleShape = new CircleShape()
    shape.setRadius(size / 3)

    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 1f
    fixtureDef.friction = 1f
    fixtureDef.filter.categoryBits = Constants.ENTITY_CATEGORY
    fixtureDef.filter.maskBits = Constants.ENTITY_MASK
    val fixture: Fixture = body.createFixture(fixtureDef)

    shape.dispose()
    fixture
  }

  private def updateCollision(): Unit = {
    val filter: Filter = fixture.getFilterData

    //    tileType match {
    //      case TileType.EMPTY =>
    //        filter.categoryBits = Constants.EMPTY_CATEGORY
    //        filter.maskBits = Constants.NO_MASK
    //      case TileType.FLOOR =>
    //        filter.categoryBits = Constants.EMPTY_CATEGORY
    //        filter.maskBits = Constants.NO_MASK
    //      case TileType.WALL =>
    //        filter.categoryBits = Constants.WALL_CATEGORY
    //        filter.maskBits = Constants.WALL_MASK
    //    }

    fixture.setFilterData(filter)
  }
}
