package galenscovell.soulslite.ui.screens

import com.badlogic.ashley.core.{Engine, Entity}
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
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

  private val debugWorldRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  private var world: World = _
  private var environment: Environment = _
  private var entityManager: EntityManager = _
  private var shader: ShaderProgram = _

  private val inputMultiplexer: InputMultiplexer = new InputMultiplexer
  private val inputHandler: InputHandler = new InputHandler

  // Box2d has a limit on velocity of 2.0 units per step
  // The max speed is 120m/s at 60fps
  private val timestep: Float = 1 / 120.0f
  private var accumulator: Float = 0

  // For shader timing
  private var time: Float = 0f

  // For camera smooth movement and bounds
  private var lerpPos: Vector3 = new Vector3(0, 0, 0)
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
    entityManager = new EntityManager(new Engine, entityBatch, inputHandler, world, this)
    environment = new Environment(60, 60, world, entityBatch)

    player = entityManager.makeEntity("player", Constants.MID_ENTITY_SIZE, 800, 800, 1, 12)
    playerBody = player.getComponent(classOf[BodyComponent]).body

//    entityManager.makeEntity("rat", 400, 400, 4, 4)
//    entityManager.makeEntity("rat", 600, 600, 4, 4)
//    entityManager.makeEntity("rat", 800, 800, 4, 4)

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
    inputMultiplexer.addProcessor(inputHandler)
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
    minCamX = camera.position.x - (camera.viewportWidth / 2) * camera.zoom
    minCamY = camera.position.y - (camera.viewportHeight / 2) * camera.zoom
    // Find camera lower right coordinates
    maxCamX = minCamX + camera.viewportWidth * camera.zoom
    maxCamY = minCamY + camera.viewportHeight * camera.zoom
    camera.update()
  }

  private def centerCameraOnPlayer(): Unit = {
    // Camera will center onto player unless they are within a certain distance of the map bounds
    val environmentDimensions: Vector2 = environment.getDimensions

    if (playerBody.getPosition.x > Constants.CAMERA_GIVE &&
      playerBody.getPosition.x < environmentDimensions.x - Constants.CAMERA_GIVE) {
      lerpPos.x = playerBody.getPosition.x
    }
    if (playerBody.getPosition.y > Constants.CAMERA_GIVE &&
      playerBody.getPosition.y < environmentDimensions.y - Constants.CAMERA_GIVE) {
      lerpPos.y = playerBody.getPosition.y
    }

    camera.position.lerp(lerpPos, 0.05f)
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
      world.step(timestep, 6, 2)
      accumulator -= timestep
    }

    updateCamera()
    centerCameraOnPlayer()
    entityBatch.setProjectionMatrix(camera.combined)
    entityBatch.begin()
    environment.render()
    entityManager.update(delta)
    entityBatch.end()

     debugWorldRenderer.render(world, camera.combined)

    // stage.act()
    // stage.draw()
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
