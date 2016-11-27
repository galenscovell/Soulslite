package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.components.dynamic.ColorComponent
import galenscovell.soulslite.ui.screens.GameScreen


class RenderSystem(family: Family, spriteBatch: SpriteBatch, gameScreen: GameScreen) extends SortedIteratingSystem(family, new ZComparator) {
  private val animationMapper: ComponentMapper[AnimationComponent] =
    ComponentMapper.getFor(classOf[AnimationComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val colorMapper: ComponentMapper[ColorComponent] =
    ComponentMapper.getFor(classOf[ColorComponent])
  private val directionMapper: ComponentMapper[DirectionComponent] =
    ComponentMapper.getFor(classOf[DirectionComponent])
  private val sizeMapper: ComponentMapper[SizeComponent] =
    ComponentMapper.getFor(classOf[SizeComponent])
  private val spriteMapper: ComponentMapper[SpriteComponent] =
    ComponentMapper.getFor(classOf[SpriteComponent])
//  private val stateMapper: ComponentMapper[StateComponent] =
//    ComponentMapper.getFor(classOf[StateComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    // TODO: Check player state, graphics depends on it
    // val stateComponent: StateComponent = stateMapper.get(entity)

    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val currentX: Float = bodyComponent.body.getPosition.x
    val currentY: Float = bodyComponent.body.getPosition.y

    if (gameScreen.inCamera(currentX, currentY)) {
      // Handle dynamic entity color effects
      if (colorMapper.has(entity)) {
        val colorComponent: ColorComponent = colorMapper.get(entity)
        spriteBatch.setShader(colorComponent.shader)

        if (colorComponent.step()) {
          colorComponent.dispose()
          entity.remove(classOf[ColorComponent])
        }
      }

      val size: Float = sizeMapper.get(entity).size
      var direction: Int = directionMapper.get(entity).direction

      // Draw animation if in motion, otherwise static sprite
      if (bodyComponent.inMotion) {
        var rotation = 0f
        if (false) {
          direction = 4
          rotation = bodyComponent.body.getAngle
        }
        val animationComponent: AnimationComponent = animationMapper.get(entity)
        animationComponent.stateTime += deltaTime

        val currentAnimation: Animation = animationComponent.getCurrentAnimation(direction)
        val currentFrame = currentAnimation.getKeyFrame(animationComponent.stateTime, true)

        spriteBatch.draw(
          currentFrame,
          currentX - size / 2, currentY - size / 2,
          size / 2, size / 2,
          size, size, 1, 1, rotation
        )
      } else {
        val spriteComponent: SpriteComponent = spriteMapper.get(entity)
        val currentSprite: Sprite = spriteComponent.getCurrentSprite(direction)

        spriteBatch.draw(
          currentSprite,
          currentX - size / 2, currentY - size / 2,
          size / 2, size / 2,
          size, size, 1, 1, 0
        )
      }
      spriteBatch.setShader(null)
    }
  }

  override def update(deltaTime: Float): Unit = {
    forceSort()
    super.update(deltaTime)
  }
}
