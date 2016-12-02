package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import galenscovell.soulslite.processing.fsm.State


class StateComponent(startState: State[StateComponent], val steeringComponent: SteeringComponent) extends Component {
  private var previousState: State[StateComponent] = _
  private var currentState: State[StateComponent] = _
  private var distanceFromPlayer: Float = 0f
  setState(startState)


  def update(): Unit = {
    currentState.update(this)
  }

  def setState(newState: State[StateComponent]): Unit = {
    if (currentState != null) {
      previousState = currentState
      currentState.exit(this)
    }

    currentState = newState
    currentState.enter(this)
  }

  def revertToPreviousState: Boolean = {
    if (previousState != null) {
      currentState.exit(this)
      currentState = previousState
      previousState = null
      currentState.enter(this)
      true
    } else {
      false
    }
  }

  def isInState(state: State[StateComponent]): Boolean = {
    currentState == state
  }

  def setDistanceFromPlayer(dst: Float): Unit = {
    distanceFromPlayer = dst
  }

  def getCurrentState: State[StateComponent] = currentState
  def getPreviousState: State[StateComponent] = previousState
  def getDistanceFromPlayer: Float = distanceFromPlayer
}
