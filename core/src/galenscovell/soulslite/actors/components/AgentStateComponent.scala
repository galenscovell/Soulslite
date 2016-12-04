package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import galenscovell.soulslite.processing.fsm.State


class AgentStateComponent(startState: State[AgentStateComponent], val steeringComponent: SteeringComponent) extends Component {
  private var previousState: State[AgentStateComponent] = _
  private var currentState: State[AgentStateComponent] = _
  private var distanceFromPlayer: Float = 0f
  private val playerPosition: Vector2 = new Vector2(0, 0)

  setState(startState)


  def update(): Unit = {
    currentState.update(this)
  }

  def setState(newState: State[AgentStateComponent]): Unit = {
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

  def isInState(state: State[AgentStateComponent]): Boolean = {
    currentState == state
  }

  def setDistanceFromPlayer(dst: Float): Unit = {
    distanceFromPlayer = dst
  }

  def setPlayerPosition(pos: Vector2): Unit = {
    playerPosition.x = pos.x
    playerPosition.y = pos.y
  }

  def getCurrentState: State[AgentStateComponent] = currentState
  def getPreviousState: State[AgentStateComponent] = previousState
  def getDistanceFromPlayer: Float = distanceFromPlayer
  def getPlayerPosition: Vector2 = playerPosition
}
