package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component


class MovementStateComponent(val isFourWay: Boolean) extends Component {
  private var idle: Boolean = true
  private var currentState: String = "down"
  private var previousState: String = _


  def setState(newState: String): Unit = {
    if (currentState != null) {
      previousState = currentState
    }
    currentState = newState
  }

  def setIdle(state: Boolean): Unit = {
    idle = state
  }

  def revertToPreviousState: Boolean = {
    if (previousState != null) {
      currentState = previousState
      previousState = null
      true
    } else {
      false
    }
  }

  def isInState(state: String): Boolean = {
    currentState == state
  }

  def isIdle: Boolean = idle
  def getCurrentState: String = currentState
  def getPreviousState: String = previousState
}
