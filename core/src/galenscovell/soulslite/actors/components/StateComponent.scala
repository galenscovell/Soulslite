package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import galenscovell.soulslite.processing.fsm.State


class StateComponent(var currentState: State) extends Component {
  var newState: State = _
}
