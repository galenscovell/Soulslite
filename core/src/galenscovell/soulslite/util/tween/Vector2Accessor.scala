package galenscovell.soulslite.util.tween

import aurelienribon.tweenengine.TweenAccessor
import com.badlogic.gdx.math.Vector2


class Vector2Accessor extends TweenAccessor[Vector2] {
  val TYPE_XY: Int = 1


  override def getValues(target: Vector2, tweenType: Int, returnValues: Array[Float]): Int = {
    tweenType match {
      case TYPE_XY =>
        returnValues(0) = target.x
        returnValues(1) = target.y
        2
      case _ =>
        -1
    }
  }

  override def setValues(target: Vector2, tweenType: Int, newValues: Array[Float]): Unit = {
    tweenType match {
      case TYPE_XY =>
        target.x = newValues(0)
        target.y = newValues(1)
      case _ =>
    }
  }
}
