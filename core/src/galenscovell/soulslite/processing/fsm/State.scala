package galenscovell.soulslite.processing.fsm


trait State[AgentStateComponent] {
  def enter(stateComponent: AgentStateComponent): Unit
  def exit(stateComponent: AgentStateComponent): Unit
  def update(stateComponent: AgentStateComponent): Unit
  def getFrameRatio: Float
  def getName: String
}
