package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.ui.screens.GameScreen


class RenderSystem(family: Family, spriteBatch: SpriteBatch, gameScreen: GameScreen) extends SortedIteratingSystem(family, new ZComparator) {
  private val bodyMapper: ComponentMapper[BodyComponent] = ComponentMapper.getFor(classOf[BodyComponent])
  private val animationMapper: ComponentMapper[AnimationComponent] = ComponentMapper.getFor(classOf[AnimationComponent])
  private val sizeMapper: ComponentMapper[SizeComponent] = ComponentMapper.getFor(classOf[SizeComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val body: Body = bodyMapper.get(entity).body

    val currentX: Float = body.getPosition.x
    val currentY: Float = body.getPosition.y

    if (gameScreen.inCamera(currentX, currentY)) {
      val size: Int = sizeMapper.get(entity).size
      val animation: AnimationComponent = animationMapper.get(entity)
      animation.stateTime += deltaTime
      val currentFrame: TextureRegion = animation.getCurrentAnimation.getKeyFrame(animation.stateTime, true)

      spriteBatch.draw(currentFrame,
        currentX - size / 2,
        currentY - size / 2,
        size / 2, size / 2,
        size, size,
        1, 1, 0)
    }
  }

  override def update(deltaTime: Float): Unit = {
    forceSort()
    super.update(deltaTime)
  }
}
