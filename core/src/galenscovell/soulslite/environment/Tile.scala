package galenscovell.soulslite.environment

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.environment.TileType.TileType
import galenscovell.soulslite.util.Constants


class Tile(tx: Int, ty: Int, world: World) {
  private var tileType: TileType = TileType.EMPTY
  private val body: Body = createBody
  private val fixture: Fixture = createFixture


  private def createBody: Body = {
    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.StaticBody
    bodyDef.fixedRotation = true
    bodyDef.angularDamping = 1f
    bodyDef.position.set(tx, ty)

    world.createBody(bodyDef)
  }

  private def createFixture: Fixture = {
    val shape: PolygonShape = new PolygonShape()
    shape.setAsBox(Constants.TILE_SIZE / 2, Constants.TILE_SIZE / 2)

    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 1f
    fixtureDef.friction = 1f
    fixtureDef.filter.categoryBits = Constants.EMPTY_CATEGORY
    fixtureDef.filter.maskBits = Constants.NO_MASK

    val fixture: Fixture = body.createFixture(fixtureDef)
    shape.dispose()
    fixture
  }


  def isEmpty: Boolean = {
    tileType ==  TileType.EMPTY
  }

  def isFloor: Boolean = {
    tileType ==  TileType.FLOOR
  }

  def isWall: Boolean = {
    tileType ==  TileType.WALL
  }


  def makeEmpty(): Unit = {
    tileType =  TileType.EMPTY
  }

  def makeFloor(): Unit = {
    tileType =  TileType.FLOOR
  }

  def makeWall(): Unit = {
    tileType =  TileType.WALL
  }


  def draw(spriteBatch: SpriteBatch): Unit = {
    // spriteBatch.draw(null, tx, ty, Constants.TILE_SIZE, Constants.TILE_SIZE)
  }
}
