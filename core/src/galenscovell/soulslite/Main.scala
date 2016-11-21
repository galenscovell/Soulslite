package galenscovell.soulslite

import com.badlogic.gdx.{Game, Gdx}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import galenscovell.soulslite.ui.screens._
import galenscovell.soulslite.util._


class Main extends Game {
  var spriteBatch: SpriteBatch = _
  var loadScreen: LoadScreen = _
  var mainMenuScreen: MainMenuScreen = _
  var gameScreen: GameScreen = _


  def create(): Unit =  {
    spriteBatch = new SpriteBatch
    mainMenuScreen = new MainMenuScreen(this)
    loadScreen = new LoadScreen(this, mainMenuScreen)
    setScreen(loadScreen)
  }

  override def dispose(): Unit =  {
    loadScreen.dispose()
    mainMenuScreen.dispose()
    if (gameScreen != null) {
      gameScreen.dispose()
    }
    Resources.dispose()
  }

  def createGameScreen(): Unit =  {
    if (gameScreen != null) {
      gameScreen.dispose()
    }
    gameScreen = new GameScreen(this)
  }

  def loadGame(): Unit = {
    // TODO: Not yet implemented
  }

  def quitGame(): Unit = {
    Gdx.app.exit()
  }
}

