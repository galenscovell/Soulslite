package galenscovell.soulslite.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.utils.Scaling
import galenscovell.soulslite.Main
import galenscovell.soulslite.util._


class LoadScreen(root: Main) extends AbstractScreen(root) {
  private var loadingImage: Image = _


  override def create(): Unit = {
    super.create()

    val loadingMain: Table = new Table
    loadingMain.setFillParent(true)

    val loadingTable: Table = new Table
    loadingImage = new Image(new Texture(Gdx.files.internal("clouds_1.png")))
    loadingImage.setScaling(Scaling.fillY)
    loadingTable.add(loadingImage).width(Constants.UI_X).height(Constants.UI_Y).expand.fill

    loadingMain.add(loadingTable).width(Constants.UI_X).height(Constants.UI_Y).expand.fill

    stage.addActor(loadingMain)
  }


  /**********************
    * Screen Operations *
    **********************/
  override def render(delta: Float): Unit = {
    super.render(delta)

    if (Resources.assetManager.update) {
      Resources.done()
      stage.getRoot.addAction(
        Actions.sequence(
          Actions.delay(0.5f),
          Actions.parallel(
            Actions.moveTo(Constants.UI_X * 1.4f, 0, 0.75f),
            Actions.fadeOut(0.75f)
          ),
          Actions.delay(0.25f),
          toMainMenuScreen
        )
      )
    }
  }

  override def show(): Unit = {
    Resources.load()
    create()
    stage.getRoot.getColor.a = 0
    stage.getRoot.addAction(
      Actions.sequence(
        Actions.moveTo(-Constants.UI_X * 1.4f, 0),
        Actions.parallel(
          Actions.moveTo(0, 0, 0.75f),
          Actions.fadeIn(0.75f)
        )
      )
    )
  }


  /***************************
    * Custom Scene2D Actions *
    ***************************/
  private[screens] var toMainMenuScreen: Action = (delta: Float) => {
    root.setMainMenuScreen()
    true
  }
}
