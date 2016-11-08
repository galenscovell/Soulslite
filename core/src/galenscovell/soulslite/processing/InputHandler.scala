package galenscovell.soulslite.processing

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor


class InputHandler extends InputProcessor {
  var leftPressed: Boolean = false
  var rightPressed: Boolean = false
  var upPressed: Boolean = false
  var downPressed: Boolean = false


  override def keyTyped(character: Char): Boolean = {
    true
  }

  override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
    true
  }

  override def keyDown(keycode: Int): Boolean = {
    keycode match {
      case Keys.LEFT => leftPressed = true
      case Keys.RIGHT => rightPressed = true
      case Keys.UP => upPressed = true
      case Keys.DOWN => downPressed = true
    }
    true
  }

  override def keyUp(keycode: Int): Boolean = {
    keycode match {
      case Keys.LEFT => leftPressed = false
      case Keys.RIGHT => rightPressed = false
      case Keys.UP => upPressed = false
      case Keys.DOWN => downPressed = false
    }
    true
  }

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    true
  }

  override def scrolled(amount: Int): Boolean = {
    true
  }

  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    true
  }

  override def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = {
    true
  }
}
