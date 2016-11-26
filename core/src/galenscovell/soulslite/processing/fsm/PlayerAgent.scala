package galenscovell.soulslite.processing.fsm

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.{DefaultStateMachine, StateMachine}


class PlayerAgent(entity: Entity) extends StateAgent {
  val stateMachine: StateMachine[PlayerAgent, PlayerState] =
    new DefaultStateMachine[PlayerAgent, PlayerState](this, PlayerState.NORMAL)


  override def update(deltaTime: Float): Unit = {

  }
}
