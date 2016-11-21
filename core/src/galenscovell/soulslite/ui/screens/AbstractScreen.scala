package galenscovell.soulslite.ui.screens

import com.badlogic.gdx._
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import galenscovell.soulslite.Main
import galenscovell.soulslite.util.Constants


class AbstractScreen(root: Main) extends Screen {
  protected val camera: OrthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
  protected val viewport: FitViewport = new FitViewport(Constants.UI_X, Constants.UI_Y, camera)
  protected var stage: Stage = _


  protected def create(): Unit = {
    stage = new Stage(viewport, root.spriteBatch)
  }

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    stage.act(delta)
    stage.draw()
  }

  override def show(): Unit = {
    create()
    Gdx.input.setInputProcessor(stage)
  }

  override def resize(width: Int, height: Int): Unit = {
    if (stage != null) {
      stage.getViewport.update(width, height, true)
    }
  }

  override def hide(): Unit = {
    Gdx.input.setInputProcessor(null)
  }

  override def dispose(): Unit = {
    if (stage != null) {
      stage.dispose()
    }
  }

  override def pause(): Unit =  {}

  override def resume(): Unit =  {}
}
