package galenscovell.soulslite.processing.fsm


trait State {
  def enter(): Unit
  def exit(): Unit
  def update(deltaTime: Float): Unit
  def getFrameRatio: Float
}
