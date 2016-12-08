package galenscovell.soulslite.graphics

import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.math.Vector2
import galenscovell.soulslite.util.Resources


class EntityAnimation(entityName: String, animationName: String, animationFrames: Int, speed: Float, offset: Vector2) {
  private val animation: Animation = construct


  private def construct: Animation = {
    val textures: Array[TextureRegion] = new Array[TextureRegion](animationFrames)
    for (i: Int <- 0 until animationFrames) {
      textures(i) = Resources.atlas.findRegion(s"entity/$entityName/$animationName$i")
    }
    new Animation(speed, textures:_*)
  }

  def getAnimation: Animation = animation
  def getKeyFrame(stateTime: Float): TextureRegion = animation.getKeyFrame(stateTime, true)

  def draw(spriteBatch: SpriteBatch, stateTime: Float, currentX: Float, currentY: Float, size: Float): Unit = {
    spriteBatch.draw(
      getKeyFrame(stateTime),
      currentX - size / 2, currentY - size / 2,
      size / 2, size / 2,
      size, size, 1, 1, 0
    )
  }
}
