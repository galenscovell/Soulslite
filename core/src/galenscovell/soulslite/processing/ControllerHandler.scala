package galenscovell.soulslite.processing

import com.badlogic.gdx.controllers._
import com.badlogic.gdx.math.Vector2


class ControllerHandler extends ControllerAdapter {
  val leftAxis: Vector2 = new Vector2(0, 0)
  val rightAxis: Vector2 = new Vector2(0, 0)
  var pressed: Boolean = false


  override def connected(controller: Controller): Unit = {
    println("Connected controller %s".format(controller.getName))
  }

  override def disconnected(controller: Controller): Unit = {
    println("Disconnected controller %s".format(controller.getName))
  }

  override def xSliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean = {
    true
  }

  override def ySliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean = {
    true
  }

  override def povMoved(controller: Controller, povCode: Int, value: PovDirection): Boolean = {
    true
  }

  override def buttonUp(controller: Controller, buttonCode: Int): Boolean = {
    println("Button up %s".format(buttonCode))
    pressed = false
    true
  }

  override def buttonDown(controller: Controller, buttonCode: Int): Boolean = {
    println("Button down %s".format(buttonCode))
    pressed = true
    true
  }

  override def axisMoved(controller: Controller, axisCode: Int, value: Float): Boolean = {
    // 1, 1 is bottom right, -1, -1 is upper left
    if (Math.abs(value) > 0.05) {
      axisCode match {
        case 0 => rightAxis.y = -value // right-vertical    Ranged aiming
        case 1 => rightAxis.x = value  // right-horizontal
        case 2 => leftAxis.y = -value  // left-vertical     Movement
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
}
