package galenscovell.soulslite.environment

import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.environment.TileType.TileType
import galenscovell.soulslite.util.{Constants, Resources}

import scala.collection.mutable.ArrayBuffer


class Tile(val tx: Int, val ty: Int, world: World, columns: Int, rows: Int) {
  private var tileType: TileType = TileType.EMPTY
  private val body: Body = createBody
  private val fixture: Fixture = createFixture
  private val neighborTilePoints: Array[Point] = findNeighborPoints

  private var sprite: Sprite = Resources.spTest0
  private var bitmask: Int = 0
  var floorNeighbors: Int = 0


  /**********************
    *      Physics      *
    **********************/
  private def createBody: Body = {
    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.StaticBody
    bodyDef.fixedRotation = true
    bodyDef.angularDamping = 1f
    bodyDef.position.set(tx * Constants.TILE_SIZE, ty * Constants.TILE_SIZE)

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

  private def updateCollision(): Unit = {
    val filter: Filter = fixture.getFilterData

    tileType match {
      case TileType.EMPTY =>
        filter.categoryBits = Constants.EMPTY_CATEGORY
        filter.maskBits = Constants.NO_MASK
      case TileType.FLOOR =>
        filter.categoryBits = Constants.EMPTY_CATEGORY
        filter.maskBits = Constants.NO_MASK
      case TileType.WALL =>
        filter.categoryBits = Constants.WALL_CATEGORY
        filter.maskBits = Constants.WALL_MASK
    }

    fixture.setFilterData(filter)
  }


  /**********************
    *     Neighbors     *
    **********************/
  def getNeighborPoints: Array[Point] = {
    neighborTilePoints
  }

  private def findNeighborPoints: Array[Point] = {
    // Compute neighboring tiles only once at object construction
    val points: ArrayBuffer[Point] = new ArrayBuffer[Point]()
    var sumX, sumY: Int = 0

    for (x <- -1 to 1) {
      for (y <- -1 to 1) {
        sumX = tx + x
        sumY = ty + y

        if (!(sumX == tx && sumY == ty) && !isOutOfBounds(sumX, sumY)) {
          points.append(new Point(sumX, sumY))
        }
      }
    }

    points.toArray
  }

  private def isOutOfBounds(x: Int, y: Int): Boolean = {
    (x < 0 || y < 0) || (x >= columns || y >= rows)
  }


  /**********************
    *    Sprite/Skin    *
    **********************/
  def setBitmask(v: Int): Unit = {
    bitmask = v
  }

  def skin(): Unit = {
    if (isWall) {
      sprite = Resources.spTest1
      // sprite = new Sprite(Resources.atlas.createSprite("tiles/wall" + bitmask))
    } else if (isFloor) {
      sprite = Resources.spTest2
      // sprite = new Sprite(Resources.atlas.createSprite("tiles/floor" + bitmask))
    }
    sprite.flip(false, true)
  }


  /**********************
    *       State       *
    **********************/
  def isEmpty: Boolean = {
    tileType == TileType.EMPTY
  }

  def isFloor: Boolean = {
    tileType == TileType.FLOOR
  }

  def isWall: Boolean = {
    tileType == TileType.WALL
  }

  def makeEmpty(): Unit = {
    tileType = TileType.EMPTY
    updateCollision()
  }

  def makeFloor(): Unit = {
    tileType = TileType.FLOOR
    updateCollision()
  }

  def makeWall(): Unit = {
    tileType = TileType.WALL
    updateCollision()
  }


  /**********************
    *      Render       *
    **********************/
  def draw(spriteBatch: SpriteBatch): Unit = {
    spriteBatch.draw(
      sprite,
      body.getPosition.x - Constants.TILE_SIZE / 2,
      body.getPosition.y - Constants.TILE_SIZE / 2,
      Constants.TILE_SIZE, Constants.TILE_SIZE
    )
  }

  def debugDraw: String = {
    tileType match {
      case TileType.EMPTY => " "
      case TileType.FLOOR => "-"
      case TileType.WALL => "#"
    }
  }
}
