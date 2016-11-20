package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.ui.screens.GameScreen


class RenderSystem(family: Family, spriteBatch: SpriteBatch, gameScreen: GameScreen) extends SortedIteratingSystem(family, new ZComparator) {
  private val animationMapper: ComponentMapper[AnimationComponent] =
    ComponentMapper.getFor(classOf[AnimationComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val sizeMapper: ComponentMapper[SizeComponent] =
    ComponentMapper.getFor(classOf[SizeComponent])
  private val spriteMapper: ComponentMapper[SpriteComponent] =
    ComponentMapper.getFor(classOf[SpriteComponent])
  private val velocityMapper: ComponentMapper[VelocityComponent] =
    ComponentMapper.getFor(classOf[VelocityComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val velocityComponent: VelocityComponent = velocityMapper.get(entity)
    val body: Body = bodyMapper.get(entity).body
    val currentX: Float = body.getPosition.x
    val currentY: Float = body.getPosition.y

    if (gameScreen.inCamera(currentX, currentY)) {
      val size: Float = sizeMapper.get(entity).size
      var direction: Int = velocityComponent.direction

      // Draw animation if in motion, otherwise static sprite
      if (velocityComponent.inMotion) {
        var rotation = 0f
        if (velocityComponent.dashing) {
          direction = 4
          rotation = velocityComponent.angle
        }
        val animationComponent: AnimationComponent = animationMapper.get(entity)
        animationComponent.stateTime += deltaTime

        val currentAnimation: Animation = animationComponent.getCurrentAnimation(direction)
        val currentFrame = currentAnimation.getKeyFrame(animationComponent.stateTime, true)

        spriteBatch.draw(
          currentFrame, currentX - size / 2, currentY - size / 2, size / 2, size / 2, size, size, 1, 1, rotation
        )
      } else {
        val spriteComponent: SpriteComponent = spriteMapper.get(entity)
        val currentSprite: Sprite = spriteComponent.getCurrentSprite(direction)

        spriteBatch.draw(
          currentSprite, currentX - size / 2, currentY - size / 2, size / 2, size / 2, size, size, 1, 1, 0
        )
      }
    }
  }

  override def update(deltaTime: Float): Unit = {
    forceSort()
    super.update(deltaTime)
  }
}
