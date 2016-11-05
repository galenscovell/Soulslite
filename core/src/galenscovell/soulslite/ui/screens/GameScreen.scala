package galenscovell.soulslite.ui.screens

import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{Gdx, _}
import galenscovell.soulslite.Main
import galenscovell.soulslite.util.Constants


class GameScreen(root: Main) extends Screen {
  private var entityStage: Stage = _
  private var interfaceStage: Stage = _

  private val input: InputMultiplexer = new InputMultiplexer

  private val timestep: Float = (1 / 60.0f) * 30
  private var accumulator: Float = 0
  private var paused: Boolean = false
  private var time: Float = 0f

  private var shader: ShaderProgram = _

  construct()


  /********************
    *       Init      *
    ********************/
  private def construct(): Unit = {
    val playerCamera: OrthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    val playerViewport: FitViewport = new FitViewport(Constants.EXACT_X, Constants.EXACT_Y, playerCamera)
    // entityStage = _

    val interfaceCamera: OrthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    val interfaceViewport: FitViewport = new FitViewport(Constants.EXACT_X, Constants.EXACT_Y, interfaceCamera)
    // interfaceStage = _

    enableInput()
    // GLProfiler.enable()
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

//    entityStage.getBatch.setShader(shader)
  }

  private def enableInput(): Unit = {
//    input.addProcessor(interfaceStage)
//    input.addProcessor(entityStage)
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

  def setPause(setting: Boolean): Unit = {
    paused = setting
  }


  /**********************
    * Screen Operations *
    **********************/
  override def render(delta: Float): Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    Gdx.gl.glClearColor(0, 0, 0, 1)

    // Shader
    time += delta
    if (time > 1200) {
      time = 0f
    }
    shader.begin()
    shader.setUniformf("u_time", time)
    shader.end()

    // Pause
    if (!paused) {
      if (accumulator > timestep) {
        accumulator -= timestep
      }
      accumulator += delta
    }

    //    println("Calls: " + GLProfiler.drawCalls + ", Bindings: " + GLProfiler.textureBindings)
    //    println("Draw Calls: " + GLProfiler.drawCalls)
    //    GLProfiler.reset()
  }

  override def show(): Unit = {
    enableInput()
  }

  override def resize(width: Int, height: Int): Unit = {
    if (entityStage != null) {
      entityStage.getViewport.update(width, height, true)
    }
    if (interfaceStage != null) {
      interfaceStage.getViewport.update(width, height, true)
    }
    shader.begin()
    shader.setUniformf("u_resolution", width, height)
    shader.end()
  }

  override def hide(): Unit = {
    Gdx.input.setInputProcessor(null)
  }

  override def dispose(): Unit = {
    if (entityStage != null) {
      entityStage.dispose()
    }
    if (interfaceStage != null) {
      interfaceStage.dispose()
    }
    shader.dispose()
  }

  override def pause(): Unit =  {}

  override def resume(): Unit =  {}
}
