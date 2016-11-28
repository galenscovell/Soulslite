package galenscovell.soulslite.processing.fsm


trait State[StateComponent] {
  def enter(stateComponent: StateComponent): Unit
  def exit(stateComponent: StateComponent): Unit
  def update(stateComponent: StateComponent): Unit
  def getFrameRatio: Float
}
