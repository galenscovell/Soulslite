package galenscovell.soulslite.processing

import com.badlogic.gdx.controllers._
import com.badlogic.gdx.math.Vector2


class GameController extends ControllerAdapter {
  val leftAxis: Vector2 = new Vector2(0, 0)
  val rightAxis: Vector2 = new Vector2(0, 0)
  var dashPressed: Boolean = false
  var attackPressed: Boolean = false


  override def buttonUp(controller: Controller, buttonCode: Int): Boolean = {
    true
  }

  override def buttonDown(controller: Controller, buttonCode: Int): Boolean = {
    buttonCode match {
      case 0 => dashPressed = true
      case 1 => attackPressed = true
      case 2 =>
      case 3 =>
      case _ =>
    }
    true
  }

  override def povMoved(controller: Controller, povCode: Int, value: PovDirection): Boolean = {
    true
  }

  override def axisMoved(controller: Controller, axisCode: Int, value: Float): Boolean = {
    // 1, 1 is bottom right, -1, -1 is upper left
    if (Math.abs(value) > 0.1) {
      axisCode match {
        case 0 => rightAxis.y = -value // right-vertical     Ranged aiming
        case 1 => rightAxis.x = value  // right-horizontal
        case 2 => leftAxis.y = -value  // left-vertical      Movement
        case 3 => leftAxis.x = value   // left-horizontal
      }
    } else {
      axisCode match {
        case 0 => rightAxis.y = 0
        case 1 => rightAxis.x = 0
        case 2 => leftAxis.y = 0
        case 3 => leftAxis.x = 0
      }
    }
    true
  }

  override def connected(controller: Controller): Unit = {
    println("Connected controller %s".format(controller.getName))
  }

  override def disconnected(controller: Controller): Unit = {
    println("Disconnected controller %s".format(controller.getName))
  }
}
