package galenscovell.soulslite.ui.screens

import com.badlogic.ashley.core.{Engine, Entity}
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math._
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

  private var world: World = _
  private val debugWorldRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  private var environment: Environment = _
  private var entityManager: EntityManager = _

  private val inputMultiplexer: InputMultiplexer = new InputMultiplexer
  private val controllerHandler: ControllerHandler = new ControllerHandler

  // Box2d has a limit on velocity of 2.0 units per step
  // The max speed is 120m/s at 60fps
  private val timestep: Float = 1 / 120.0f
  private var accumulator: Float = 0

  // For shader
  private var shader: ShaderProgram = _
  private var time: Float = 0f
  private var steps: Int = 0
  private var totalRunTimes: Double = 0f

  // For camera
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
    environment = new Environment(world)

    player = entityManager.makeEntity("player", Constants.MID_ENTITY_SIZE, 20, 20)
    playerBody = player.getComponent(classOf[BodyComponent]).body
    // Start camera immediately centered on player
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

    // Camera operations
    updateCamera()
    centerCameraOnPlayer()
    entityBatch.setProjectionMatrix(worldCamera.combined)
    environment.updateCamera(worldCamera)

    // Main render operations
    environment.renderBaseLayer()
    entityBatch.begin()
    entityManager.update(delta)
    entityBatch.end()
    environment.renderOverlapLayer()

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
