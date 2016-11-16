package galenscovell.soulslite.ui.screens

import com.badlogic.ashley.core.{Engine, Entity}
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.{Vector2, Vector3}
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.{Gdx, _}
import galenscovell.soulslite.Main
import galenscovell.soulslite.actors.components.BodyComponent
import galenscovell.soulslite.environment.Environment
import galenscovell.soulslite.processing._
import galenscovell.soulslite.util.Constants


class GameScreen(root: Main) extends AbstractScreen(root) {
  private val entityBatch: SpriteBatch = new SpriteBatch()
  private val worldCamera: OrthographicCamera = new OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y)

  private val debugWorldRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  private var world: World = _
  private var tiledMapRenderer: OrthogonalTiledMapRenderer = _
  private val mapBaseLayers: Array[Int] = Array(0)
  private val mapOverLayers: Array[Int] = Array(1)
  private var entityManager: EntityManager = _
  private var shader: ShaderProgram = _

  private val inputMultiplexer: InputMultiplexer = new InputMultiplexer
  private val controllerHandler: ControllerHandler = new ControllerHandler

  // Box2d has a limit on velocity of 2.0 units per step
  // The max speed is 120m/s at 60fps
  private val timestep: Float = 1 / 120.0f
  private var accumulator: Float = 0

  // For shader timing
  private var time: Float = 0f
  private var steps: Int = 0
  private var totalRunTimes: Double = 0f

  // For camera smooth movement and bounds
  private val lerpPos: Vector3 = new Vector3(0, 0, 0)
  private var minCamX, minCamY, maxCamX, maxCamY: Float = 0f
  private var player: Entity = _
  private var playerBody: Body = _

  create()


  /********************
    *       Init      *
    ********************/
  override def create(): Unit = {
    stage = new Stage(viewport, root.spriteBatch)

    world = new World(new Vector2(0, 0), true)  // Gravity, whether to sleep or not
    entityManager = new EntityManager(new Engine, entityBatch, controllerHandler, world, this)

    val tileMap = new TmxMapLoader().load("maps/test.tmx")
    val prop: MapProperties = tileMap.getProperties
    val mapWidth: Int = prop.get("width", classOf[Integer])
    val mapHeight: Int = prop.get("height", classOf[Integer])
    val tileWidth: Int = prop.get("tilewidth", classOf[Integer])
    val tileHeight: Int = prop.get("tileheight", classOf[Integer])
    println(mapWidth, mapHeight, tileWidth, tileHeight)
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap, Constants.TILE_SIZE / Constants.PIXEL_PER_METER)

    player = entityManager.makeEntity("player", Constants.MID_ENTITY_SIZE, 20, 20)
    playerBody = player.getComponent(classOf[BodyComponent]).body
    // Start camera centered on player with no lerp
    worldCamera.position.set(playerBody.getPosition.x, playerBody.getPosition.y, 0)

    enableInput()
    setupShader()
  }

  private def setupShader(): Unit = {
    ShaderProgram.pedantic = false

    shader = new ShaderProgram(
      Gdx.files.internal("shaders/water_ripple.vert").readString(),
      Gdx.files.internal("shaders/water_ripple.frag").readString()
    )
    if (!shader.isCompiled) {
      println(shader.getLog)
    }
    // entityStage.getBatch.setShader(shader)
  }

  private def enableInput(): Unit = {
    inputMultiplexer.addProcessor(stage)
    Controllers.addListener(controllerHandler)
    Gdx.input.setInputProcessor(inputMultiplexer)
  }


/**********************
  *  Misc Operations  *
  **********************/
  def getRoot: Main = {
    root
  }

  def toMainMenu(): Unit = {
    root.setScreen(root.mainMenuScreen)
  }


  /**********************
    *      Camera       *
    **********************/
  private def updateCamera(): Unit = {
    // Find camera upper left coordinates
    minCamX = worldCamera.position.x - (worldCamera.viewportWidth / 2) * worldCamera.zoom
    minCamY = worldCamera.position.y - (worldCamera.viewportHeight / 2) * worldCamera.zoom
    // Find camera lower right coordinates
    maxCamX = minCamX + worldCamera.viewportWidth * worldCamera.zoom
    maxCamY = minCamY + worldCamera.viewportHeight * worldCamera.zoom
    worldCamera.update()
  }

  private def centerCameraOnPlayer(): Unit = {
    // Camera will center onto player unless they are within a certain distance of the map bounds
//    val environmentDimensions: Vector2 = environment.getDimensions
//
//    if (playerBody.getPosition.x > Constants.CAMERA_GIVE + (Constants.TILE_SIZE / 2) &&
//      playerBody.getPosition.x < environmentDimensions.x - Constants.CAMERA_GIVE - (Constants.TILE_SIZE / 2)) {
//      lerpPos.x = playerBody.getPosition.x
//    }
//    if (playerBody.getPosition.y > Constants.CAMERA_GIVE + (Constants.TILE_SIZE / 2) &&
//      playerBody.getPosition.y < environmentDimensions.y - Constants.CAMERA_GIVE - (Constants.TILE_SIZE / 2)) {
//      lerpPos.y = playerBody.getPosition.y
//    }

    lerpPos.x = playerBody.getPosition.x
    lerpPos.y = playerBody.getPosition.y

    worldCamera.position.lerp(lerpPos, 0.05f)
  }

  def inCamera(x: Float, y: Float): Boolean = {
    // Determines if a point falls within the camera (+/- some give to reduce chances of pop-in)
    (x + Constants.TILE_SIZE) >= minCamX &&
      (y + Constants.TILE_SIZE) >= minCamY &&
      (x - Constants.TILE_SIZE) <= maxCamX &&
      (y - Constants.TILE_SIZE) <= maxCamY
  }


  /**********************
    * Screen Operations *
    **********************/
  override def render(delta: Float): Unit = {
    val startTime: Double = System.nanoTime()

    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    // Shader
//    time += delta
//    if (time > 1200) {
//      time = 0f
//    }
//    shader.begin()
//    shader.setUniformf("u_time", time)
//    shader.end()

    val frameTime: Float = Math.min(delta, 0.25f)
    accumulator += frameTime
    while (accumulator > timestep) {
      world.step(timestep, 8, 3)
      accumulator -= timestep
    }

    updateCamera()
    centerCameraOnPlayer()
    entityBatch.setProjectionMatrix(worldCamera.combined)

    tiledMapRenderer.setView(worldCamera)
    tiledMapRenderer.render(mapBaseLayers)

    entityBatch.begin()
    entityManager.update(delta)
    entityBatch.end()

    tiledMapRenderer.render(mapOverLayers)

    debugWorldRenderer.render(world, worldCamera.combined)

    // stage.act()
    // stage.draw()

    if (steps == 60) {
      println("Average: " + (totalRunTimes / 60).toString + "ms")
      steps = 0
      totalRunTimes = 0f
    }
    totalRunTimes += (System.nanoTime() - startTime) / 1000000
    steps += 1
  }

  override def show(): Unit = {
    enableInput()
  }

  override def resize(width: Int, height: Int): Unit = {
    super.resize(width, height)
//    shader.begin()
//    shader.setUniformf("u_resolution", width, height)
//    shader.end()
  }

  override def dispose(): Unit = {
    super.dispose()
    shader.dispose()
  }
}
