package galenscovell.soulslite.ui.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.{Gdx, _}
import galenscovell.soulslite.Main
import galenscovell.soulslite.processing.{EntityManager, EnvironmentRenderer}


class GameScreen(root: Main) extends AbstractScreen(root) {
  private val entityBatch: SpriteBatch = new SpriteBatch()
  private var entityManager: EntityManager = _
  private val renderer: EnvironmentRenderer = new EnvironmentRenderer
  private val input: InputMultiplexer = new InputMultiplexer

  private var time: Float = 0f
  private var shader: ShaderProgram = _

  create()


  /********************
    *       Init      *
    ********************/
  override def create(): Unit = {
    stage = new Stage(viewport, root.spriteBatch)
    entityManager = new EntityManager(new Engine, entityBatch)
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
    input.addProcessor(stage)
    Gdx.input.setInputProcessor(input)
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

    // renderer.render(accumulator / timestep)

    camera.update()
    entityBatch.setProjectionMatrix(camera.combined)
    entityBatch.begin()
    entityManager.update(delta)
    entityBatch.end()

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
