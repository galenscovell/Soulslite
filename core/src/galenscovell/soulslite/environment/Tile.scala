package galenscovell.soulslite.environment

import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.environment.TileType.TileType
import galenscovell.soulslite.util.{Constants, Resources}

import scala.collection.mutable.ArrayBuffer


class Tile(tx: Int, ty: Int, world: World, columns: Int, rows: Int) {
  private var tileType: TileType = TileType.EMPTY
  private val body: Body = createBody
  private val fixture: Fixture = createFixture
  private val neighborTilePoints: Array[Point] = findNeighborPoints
  private var sprite: Sprite = Resources.spTest0

  var floorNeighbors: Int = 0


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

  private def findNeighborPoints: Array[Point] = {
    // Compute neighboring tiles only once at object construction
    val points: ArrayBuffer[Point] = new ArrayBuffer[Point]()
    var sumX, sumY: Int = 0

    for (i <- -1 to 1) {
      for (j <- -1 to 1) {
        sumX = tx + i
        sumY = ty + j

        if (!((sumX == tx && sumY == ty) || isOutOfBounds(sumX, sumY))) {
          points.append(new Point(sumX, sumY))
        }
      }
    }

    points.toArray
  }

  private def isOutOfBounds(i: Int, j: Int): Boolean = {
    if (i < 0 || j < 0){
      true
    } else if (i >= columns || j >= rows){
      true
    } else {
      false
    }
  }

  def getNeighborPoints: Array[Point] = {
    neighborTilePoints
  }


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
    sprite = Resources.spTest0

    val filter: Filter = fixture.getFilterData
    filter.categoryBits = Constants.EMPTY_CATEGORY
    filter.maskBits = Constants.NO_MASK
    fixture.setFilterData(filter)
  }

  def makeFloor(): Unit = {
    tileType = TileType.FLOOR
    sprite = Resources.spTest1

    val filter: Filter = fixture.getFilterData
    filter.categoryBits = Constants.EMPTY_CATEGORY
    filter.maskBits = Constants.NO_MASK
    fixture.setFilterData(filter)
  }

  def makeWall(): Unit = {
    tileType = TileType.WALL
    sprite = Resources.spTest2

    val filter: Filter = fixture.getFilterData
    filter.categoryBits = Constants.WALL_CATEGORY
    filter.maskBits = Constants.WALL_MASK
    fixture.setFilterData(filter)
  }


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
