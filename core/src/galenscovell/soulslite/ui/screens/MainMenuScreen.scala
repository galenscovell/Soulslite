package galenscovell.soulslite.ui.screens

import com.badlogic.gdx.controllers._
import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import galenscovell.soulslite.Main
import galenscovell.soulslite.util._


class MainMenuScreen(root: Main) extends AbstractScreen(root) {
  private val buttonArray: Array[TextButton] = new Array(4)
  private var selection: Int = 0
  setupControllerInput()


  private def setupControllerInput(): Unit = {
    Controllers.addListener(
      new ControllerAdapter {
        override def buttonDown(controller: Controller, buttonCode: Int): Boolean = {
          if (buttonCode == 9) {
            virtualClick(buttonArray(selection))
          }
          true
        }

        override def povMoved(controller: Controller, povCode: Int, value: PovDirection): Boolean = {
          if (value == PovDirection.north || value == PovDirection.south) {
            updateSelection(value)
          }
          true
        }
      }
    )
  }

  private def virtualClick(actor: Actor): Unit = {
    val clickEvent: InputEvent = new InputEvent
    clickEvent.setType(InputEvent.Type.touchDown)

    val listeners = actor.getListeners
    for (l: EventListener <- listeners.toArray) {
      if (l.isInstanceOf[ClickListener]) {
        l.asInstanceOf[ClickListener].clicked(null, 0, 0)
      }
    }

    actor.fire(clickEvent)
  }

  private def updateSelection(povDirection: PovDirection): Unit = {
    buttonArray(selection).setStyle(Resources.blueButtonStyle)
    povDirection match {
      case PovDirection.north => selection -= 1
      case PovDirection.south => selection += 1
    }

    if (selection > 3) {
      selection = 0
    } else if (selection < 0) {
      selection = 3
    }

    buttonArray(selection).setStyle(Resources.greenButtonStyle)
  }

  override def create(): Unit = {
    super.create()

    val mainTable: Table = new Table
    mainTable.setFillParent(true)

    val titleTable: Table = new Table
    val titleLabel: Label = new Label("Soulslite", Resources.labelXLargeStyle)
    titleLabel.setAlignment(Align.center, Align.center)
    titleTable.add(titleLabel).width(Constants.UI_X * 0.95f).height(Constants.UI_Y * 0.5f)

    val buttonTable: Table = new Table

    val startButton = new TextButton("Start", Resources.greenButtonStyle)
    startButton.getLabel.setAlignment(Align.center, Align.center)
    startButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        root.createGameScreen()
        stage.getRoot.addAction(Actions.sequence(
          Actions.fadeOut(0.5f),
          toStartScreenAction)
        )
      }
    })

    val continueButton = new TextButton("Continue", Resources.blueButtonStyle)
    continueButton.getLabel.setAlignment(Align.center, Align.center)
    continueButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {

      }
    })

    val settingsButton = new TextButton("Settings", Resources.blueButtonStyle)
    settingsButton.getLabel.setAlignment(Align.center, Align.center)
    settingsButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {

      }
    })

    val quitButton = new TextButton("Quit", Resources.blueButtonStyle)
    quitButton.getLabel.setAlignment(Align.center, Align.center)
    quitButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        stage.getRoot.addAction(Actions.sequence(
          Actions.fadeOut(0.5f),
          quitGameAction)
        )
      }
    })

    buttonArray(0) = startButton
    buttonArray(1) = continueButton
    buttonArray(2) = settingsButton
    buttonArray(3) = quitButton

    val detailTable: Table = new Table
    val detailLabel: Label = new Label(s"v1a 2016 Studio", Resources.labelSmallStyle)
    detailLabel.setAlignment(Align.center, Align.right)
    detailTable.add(detailLabel).width(Constants.UI_X * 0.95f).height(Constants.UI_Y * 0.1f)


    buttonTable.add(startButton).width(Constants.UI_X * 0.5f).height(Constants.UI_Y * 0.075f).pad(2).center
    buttonTable.row
    buttonTable.add(continueButton).width(Constants.UI_X * 0.5f).height(Constants.UI_Y * 0.075f).pad(2).center
    buttonTable.row
    buttonTable.add(settingsButton).width(Constants.UI_X * 0.5f).height(Constants.UI_Y * 0.075f).pad(2).center
    buttonTable.row
    buttonTable.add(quitButton).width(Constants.UI_X * 0.5f).height(Constants.UI_Y * 0.075f).pad(2).center

    mainTable.add(titleTable).width(Constants.UI_X).height(Constants.UI_Y * 0.5f).expand.center.pad(2)
    mainTable.row
    mainTable.add(buttonTable).width(Constants.UI_X).height(Constants.UI_Y * 0.3f).expand.center.pad(2)
    mainTable.row
    mainTable.add(detailTable).width(Constants.UI_X).height(Constants.UI_Y * 0.1f).expand.center.bottom.pad(2)

    stage.addActor(mainTable)
    mainTable.addAction(Actions.sequence(
      Actions.fadeOut(0),
      Actions.fadeIn(0.5f))
    )
  }


  /***************************
    * Custom Scene2D Actions *
    ***************************/
  private[screens] var toStartScreenAction: Action = (delta: Float) => {
    root.setScreen(root.gameScreen)
    true
  }
  private[screens] var quitGameAction: Action = (delta: Float) => {
    root.quitGame()
    true
  }
}
