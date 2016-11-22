package galenscovell.soulslite.ui.screens

import com.badlogic.gdx.controllers._
import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import galenscovell.soulslite.Main
import galenscovell.soulslite.util._

import scala.collection.immutable.List


class MainMenuScreen(root: Main) extends AbstractScreen(root) {
  private val buttonTable: Table = new Table
  private var startPressed: Boolean = false
  private val buttonArray: Array[TextButton] = new Array(4)
  private var selection: Int = 0

  setupControllerInput()


  private def setupControllerInput(): Unit = {
    Controllers.addListener(
      new ControllerAdapter {
        override def buttonDown(controller: Controller, buttonCode: Int): Boolean = {
          if (startPressed && List(9, 1).contains(buttonCode)) {
            // Start has been pressed and selections are displayed
            virtualClick(buttonArray(selection))
          } else if (!startPressed && buttonCode == 9) {
            // Start has not been pressed
            buttonTable.addAction(
              Actions.sequence(
                Actions.fadeOut(0.25f),
                showButtons
              )
            )
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
    buttonArray(selection).setStyle(Resources.emptyButtonStyle)
    povDirection match {
      case PovDirection.north => selection -= 1
      case PovDirection.south => selection += 1
    }

    if (selection > 3) {
      selection = 0
    } else if (selection < 0) {
      selection = 3
    }

    buttonArray(selection).setStyle(Resources.blueButtonBarsStyle)
  }


  override def create(): Unit = {
    super.create()

    val mainTable: Table = new Table
    mainTable.setFillParent(true)

    val titleTable: Table = new Table
    val titleLabel: Label = new Label("Soulslite", Resources.labelXLargeStyle)
    titleLabel.setAlignment(Align.center, Align.center)
    titleTable.add(titleLabel).width(Constants.UI_X * 0.95f).height(Constants.UI_Y * 0.5f)

    val pressStartLabel: Label = new Label("Press Start", Resources.labelLargeStyle)
    pressStartLabel.setAlignment(Align.center, Align.center)
    buttonTable.add(pressStartLabel).expand.fill
    buttonTable.addAction(
      Actions.forever(
        Actions.sequence(
          Actions.fadeOut(1.5f),
          Actions.fadeIn(1.5f)
        )
      )
    )

    val detailTable: Table = new Table
    val detailLabel: Label = new Label(s"v1a 2016 Studio", Resources.labelSmallStyle)
    detailLabel.setAlignment(Align.center, Align.right)
    detailTable.add(detailLabel).width(Constants.UI_X * 0.95f).height(Constants.UI_Y * 0.1f)


    mainTable.add(titleTable).width(Constants.UI_X).height(Constants.UI_Y * 0.5f).expand.center.pad(2)
    mainTable.row
    mainTable.add(buttonTable).width(Constants.UI_X).height(Constants.UI_Y * 0.3f).expand.center.pad(2)
    mainTable.row
    mainTable.add(detailTable).width(Constants.UI_X).height(Constants.UI_Y * 0.1f).expand.center.bottom.pad(2)

    stage.addActor(mainTable)
    mainTable.addAction(
      Actions.sequence(
        Actions.fadeOut(0),
        Actions.fadeIn(0.25f)
      )
    )
  }

  private def showMenu(): Unit = {
    startPressed = true
    buttonTable.clearActions()
    buttonTable.clear()

    val startButton = new TextButton("Start", Resources.blueButtonBarsStyle)
    startButton.getLabel.setAlignment(Align.center, Align.center)
    startButton.getLabelCell.pad(24)
    startButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        root.createGameScreen()
        stage.getRoot.addAction(Actions.sequence(
          Actions.fadeOut(0.5f),
          toStartScreenAction)
        )
      }
    })

    val continueButton = new TextButton("Continue", Resources.emptyButtonStyle)
    continueButton.getLabel.setAlignment(Align.center, Align.center)
    continueButton.getLabelCell.pad(24)
    continueButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {

      }
    })

    val settingsButton = new TextButton("Settings", Resources.emptyButtonStyle)
    settingsButton.getLabel.setAlignment(Align.center, Align.center)
    settingsButton.getLabelCell.pad(24)
    settingsButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {

      }
    })

    val quitButton = new TextButton("Quit", Resources.emptyButtonStyle)
    quitButton.getLabel.setAlignment(Align.center, Align.center)
    quitButton.getLabelCell.pad(24)
    quitButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        stage.getRoot.addAction(Actions.sequence(
          Actions.fadeOut(0.25f),
          quitGameAction)
        )
      }
    })

    buttonArray(0) = startButton
    buttonArray(1) = continueButton
    buttonArray(2) = settingsButton
    buttonArray(3) = quitButton

    buttonTable.add(startButton).height(Constants.UI_Y * 0.05f).pad(12).center
    buttonTable.row
    buttonTable.add(continueButton).height(Constants.UI_Y * 0.05f).pad(12).center
    buttonTable.row
    buttonTable.add(settingsButton).height(Constants.UI_Y * 0.05f).pad(12).center
    buttonTable.row
    buttonTable.add(quitButton).height(Constants.UI_Y * 0.05f).pad(12).center

    buttonTable.addAction(
      Actions.sequence(
        Actions.fadeIn(0.25f)
      )
    )
  }


  /***************************
    * Custom Scene2D Actions *
    ***************************/
  private[screens] var showButtons: Action = (delta: Float) => {
    showMenu()
    true
  }
  private[screens] var toStartScreenAction: Action = (delta: Float) => {
    root.setScreen(root.gameScreen)
    true
  }
  private[screens] var quitGameAction: Action = (delta: Float) => {
    root.quitGame()
    true
  }
}
