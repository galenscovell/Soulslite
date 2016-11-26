package galenscovell.soulslite.ui.screens

import com.badlogic.ashley.core.{Engine, Entity}
import com.badlogic.gdx._
import com.badlogic.gdx.ai.steer.behaviors._
import com.badlogic.gdx.ai.steer.utils.RayConfiguration
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math._
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.scenes.scene2d.Stage
import galenscovell.soulslite.Main
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.environment.{PhysicsWorld, TileMap}
import galenscovell.soulslite.processing._
import galenscovell.soulslite.util.Constants


class GameScreen(root: Main) extends AbstractScreen(root) {
  private val entityBatch: SpriteBatch = new SpriteBatch()
  private val worldCamera: OrthographicCamera = new OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y)
  private val gameController: GameController = new GameController
  private val physicsWorld: PhysicsWorld = new PhysicsWorld
  private val entityManager: EntityManager = new EntityManager(new Engine, entityBatch, gameController, physicsWorld.getWorld, this)
  private val tileMap: TileMap = new TileMap(physicsWorld.getWorld)

  // Box2d has a limit on velocity of 2.0 units per step
  // The max speed is 120m/s at 60fps
  private val timestep: Float = 1 / 120.0f
  private var accumulator: Float = 0

  // For shader
  private var environmentShader: ShaderProgram = _
  private var environmentShaderTime: Float = 0f
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

    // Establish player entity
    player = entityManager.makeEntity("player", Constants.MID_ENTITY_SIZE, 20, 20)
    playerBody = player.getComponent(classOf[BodyComponent]).body
    val playerComponent: PlayerComponent = new PlayerComponent
    player.add(new PlayerComponent)
    val playerSteering: SteeringComponent = new SteeringComponent(playerBody, 1, 5, 20, 5, 20)


    // Temp entities for testing purposes with same graphics as player
    val testDummy = entityManager.makeEntity("player", Constants.MID_ENTITY_SIZE, 20, 24)
    val steeringComponent: SteeringComponent = new SteeringComponent(
      testDummy.getComponent(classOf[BodyComponent]).body, 1, 4, 20, 4, 20
    )
    testDummy.add(steeringComponent)

    val arriveBehavior: Arrive[Vector2] = new Arrive[Vector2](
      steeringComponent.steering, playerSteering.steering).setEnabled(true)
      .setTimeToTarget(0.05f)       // Time over which to achieve target speed
      .setArrivalTolerance(3f)      // Distance at which entity has 'arrived'
      .setDecelerationRadius(4f)    // Distance at which deceleration begins

    val pursueBehavior: Pursue[Vector2] = new Pursue[Vector2](
      steeringComponent.steering, playerSteering.steering, 4f).setEnabled(true)

    val evadeBehavior: Evade[Vector2] = new Evade[Vector2](
      steeringComponent.steering, playerSteering.steering, 4f).setEnabled(true)

    val raycastObstacleAvoidance: RaycastObstacleAvoidance[Vector2] =
      new RaycastObstacleAvoidance[Vector2](steeringComponent.steering)

    steeringComponent.steering.behavior = arriveBehavior


    // Start camera immediately centered on player
    worldCamera.position.set(playerBody.getPosition.x, playerBody.getPosition.y, 0)

    setupEnvironmentShader()
  }

  private def setupEnvironmentShader(): Unit = {
    ShaderProgram.pedantic = false

    environmentShader = new ShaderProgram(
      Gdx.files.internal("shaders/water_ripple.vert").readString(),
      Gdx.files.internal("shaders/water_ripple.frag").readString()
    )
    if (!environmentShader.isCompiled) {
      println(environmentShader.getLog)
    }

    // Environment shader can only overlap entities if we have a tilemap layer above them
    // Maybe make a transparent layer covering every map for effects like rain/snow?
    tileMap.updateShader(environmentShader)
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
    (x + Constants.MID_ENTITY_SIZE) >= minCamX &&
      (y + Constants.MID_ENTITY_SIZE) >= minCamY &&
      (x - Constants.MID_ENTITY_SIZE) <= maxCamX &&
      (y - Constants.MID_ENTITY_SIZE) <= maxCamY
  }


  /**********************
    * Screen Operations *
    **********************/
  override def render(delta: Float): Unit = {
    val startTime: Double = System.nanoTime()

    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    // Environment shader
    environmentShaderTime += delta
    if (environmentShaderTime > 1200) {
      environmentShaderTime = 0f
    }
    environmentShader.begin()
    environmentShader.setUniformf("u_time", environmentShaderTime)
    environmentShader.end()

    val frameTime: Float = Math.min(delta, 0.25f)
    accumulator += frameTime
    while (accumulator > timestep) {
      physicsWorld.update(timestep)
      accumulator -= timestep
    }

    // Camera operations
    updateCamera()
    centerCameraOnPlayer()
    entityBatch.setProjectionMatrix(worldCamera.combined)
    tileMap.updateCamera(worldCamera)

    // Main render operations
    tileMap.renderBaseLayer()
    entityBatch.begin()
    entityManager.update(delta)
    entityBatch.end()
    tileMap.renderOverlapLayer()

    physicsWorld.debugRender(worldCamera.combined)

    // stage.act()
    // stage.draw()

    if (steps == 300) {
      println(f"Average: ${totalRunTimes / 300}%1.3f ms")
      steps = 0
      totalRunTimes = 0f
    }
    totalRunTimes += (System.nanoTime() - startTime) / 1000000
    steps += 1
  }

  override def show(): Unit = {
    Gdx.input.setInputProcessor(stage)
    Controllers.clearListeners()
    Controllers.addListener(gameController)
  }

  override def resize(width: Int, height: Int): Unit = {
    super.resize(width, height)
    environmentShader.begin()
    environmentShader.setUniformf("u_resolution", width, height)
    environmentShader.end()
  }

  override def dispose(): Unit = {
    super.dispose()
    environmentShader.dispose()
    physicsWorld.dispose()
  }
}
