package galenscovell.soulslite.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import galenscovell.soulslite.Main
import galenscovell.soulslite.util._


class LoadScreen(root: Main) extends AbstractScreen(root) {
  private var loadingBar: ProgressBar = _


  override def create(): Unit = {
    super.create()

    val loadingMain: Table = new Table
    loadingMain.setFillParent(true)
    val barTable: Table = new Table
    loadingBar = createBar
    barTable.add(loadingBar).width(400).expand.fill
    loadingMain.add(barTable).expand.fill

    stage.addActor(loadingMain)
  }

  private def createBar: ProgressBar = {
    val fill: TextureRegionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("loadingFill.png"))))
    val empty: TextureRegionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("loadingEmpty.png"))))
    val barStyle: ProgressBar.ProgressBarStyle = new ProgressBar.ProgressBarStyle(empty, fill)
    val bar: ProgressBar = new ProgressBar(0, 10, 1, false, barStyle)
    barStyle.knobBefore = fill
    bar.setValue(0)
    bar.setAnimateDuration(0.1f)
    bar
  }


  /**********************
    * Screen Operations *
    **********************/
  override def render(delta: Float): Unit = {
    super.render(delta)

    if (Resources.assetManager.update) {
      Resources.done()
      stage.getRoot.addAction(Actions.sequence(Actions.fadeOut(0.4f), toMainMenuScreen))
    }
    loadingBar.setValue(Resources.assetManager.getLoadedAssets)
  }

  override def show(): Unit = {
    Resources.load()
    create()
    stage.getRoot.getColor.a = 0
    stage.getRoot.addAction(Actions.sequence(Actions.fadeIn(0.4f)))
  }


  /***************************
    * Custom Scene2D Actions *
    ***************************/
  private[screens] var toMainMenuScreen: Action = (delta: Float) => {
    root.setScreen(root.mainMenuScreen)
    true
  }
}
