package galenscovell.soulslite.processing.fsm


trait StateAgent {
  def update(deltaTime: Float): Unit
}
