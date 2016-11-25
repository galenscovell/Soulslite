package galenscovell.soulslite.environment

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.{MapLayer, MapProperties}
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.utils.Array
import galenscovell.soulslite.util.Constants


class TileMap(world: World) {
  private var tiledMapRenderer: OrthogonalTiledMapRenderer = _
  private val baseLayers: scala.Array[Int] = scala.Array(0, 1)
  private val overlapLayers: scala.Array[Int] = scala.Array(2)

  create()


  private def create(): Unit = {
    val tileMap = new TmxMapLoader().load("maps/test.tmx")
    val prop: MapProperties = tileMap.getProperties
    val mapWidth: Int = prop.get("width", classOf[Integer])
    val mapHeight: Int = prop.get("height", classOf[Integer])
    val tileWidth: Int = prop.get("tilewidth", classOf[Integer])
    val tileHeight: Int = prop.get("tileheight", classOf[Integer])
    println(mapWidth, mapHeight, tileWidth, tileHeight)

    // Find collision rectangle objects
    val objectLayer: MapLayer = tileMap.getLayers.get("Collision")
    val objects: Array[RectangleMapObject] = objectLayer.getObjects.getByType(classOf[RectangleMapObject])
    for (rmo: RectangleMapObject <- objects.toArray) {
      val rect: Rectangle = rmo.getRectangle
      createCollisionBody(
        rect.x / Constants.PIXEL_PER_METER, rect.y / Constants.PIXEL_PER_METER,
        rect.width / Constants.PIXEL_PER_METER, rect.height / Constants.PIXEL_PER_METER)
    }

    tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap, Constants.TILE_SIZE / Constants.PIXEL_PER_METER)
  }


  private def createCollisionBody(rx: Float, ry: Float, width: Float, height: Float): Unit = {
    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.StaticBody
    bodyDef.fixedRotation = true
    bodyDef.angularDamping = 1f
    bodyDef.position.set(rx + width / 2, ry + height / 2)

    val body: Body = world.createBody(bodyDef)

    val shape: PolygonShape = new PolygonShape()
    shape.setAsBox(width / 2, height / 2)

    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 1f
    fixtureDef.friction = 0.1f
    fixtureDef.filter.categoryBits = Constants.WALL_CATEGORY
    fixtureDef.filter.maskBits = Constants.WALL_MASK

    val fixture: Fixture = body.createFixture(fixtureDef)
    shape.dispose()
  }

  def updateShader(shaderProgram: ShaderProgram): Unit = {
    tiledMapRenderer.getBatch.setShader(shaderProgram)
  }

  def updateCamera(camera: OrthographicCamera): Unit = {
    tiledMapRenderer.setView(camera)
  }

  def renderBaseLayer(): Unit = {
    tiledMapRenderer.render(baseLayers)
  }

  def renderOverlapLayer(): Unit = {
    tiledMapRenderer.render(overlapLayers)
  }

}
