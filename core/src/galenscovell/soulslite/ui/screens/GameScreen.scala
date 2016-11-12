package galenscovell.soulslite.ui.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{Box2DDebugRenderer, World}
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.{Gdx, _}
import galenscovell.soulslite.Main
import galenscovell.soulslite.environment.Environment
import galenscovell.soulslite.processing._


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

  private var time: Float = 0f

  create()


  /********************
    *       Init      *
    ********************/
  override def create(): Unit = {
    stage = new Stage(viewport, root.spriteBatch)

    world = new World(new Vector2(0, 0), true)  // Gravity, whether to sleep or not
    entityManager = new EntityManager(new Engine, entityBatch, inputHandler, world)
    environment = new Environment(20, 20, world, entityBatch)

    entityManager.makeEntity("player", 200, 200, 9, 6)
    entityManager.makeEntity("rat", 400, 400, 4, 4)
    entityManager.makeEntity("rat", 600, 600, 4, 4)
    entityManager.makeEntity("rat", 800, 800, 4, 4)

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

    camera.update()

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
