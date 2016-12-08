package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.components.dynamic.ColorComponent
import galenscovell.soulslite.graphics.EntityAnimation
import galenscovell.soulslite.ui.screens.GameScreen


class RenderSystem(family: Family, spriteBatch: SpriteBatch, gameScreen: GameScreen) extends SortedIteratingSystem(family, new ZComparator) {
  private val agentStateMapper: ComponentMapper[AgentStateComponent] =
    ComponentMapper.getFor(classOf[AgentStateComponent])
  private val animationMapper: ComponentMapper[AnimationComponent] =
    ComponentMapper.getFor(classOf[AnimationComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val colorMapper: ComponentMapper[ColorComponent] =
    ComponentMapper.getFor(classOf[ColorComponent])
  private val movementStateMapper: ComponentMapper[MovementStateComponent] =
    ComponentMapper.getFor(classOf[MovementStateComponent])
  private val sizeMapper: ComponentMapper[SizeComponent] =
    ComponentMapper.getFor(classOf[SizeComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val currentX: Float = bodyComponent.body.getPosition.x
    val currentY: Float = bodyComponent.body.getPosition.y

    if (gameScreen.inCamera(currentX, currentY)) {
      val agentStateComponent: AgentStateComponent = agentStateMapper.get(entity)
      val movementStateComponent: MovementStateComponent = movementStateMapper.get(entity)

      // Handle dynamic entity color effects
      if (colorMapper.has(entity)) {
        val colorComponent: ColorComponent = colorMapper.get(entity)
        spriteBatch.setShader(colorComponent.shader)

        if (colorComponent.step()) {
          colorComponent.dispose()
          entity.remove(classOf[ColorComponent])
        }
      }

      // Draw animation
      val size: Float = sizeMapper.get(entity).size
      val animationComponent: AnimationComponent = animationMapper.get(entity)
      animationComponent.stateTime += deltaTime

      val currentAnimation: EntityAnimation = animationComponent.getCurrentAnimation(
        agentStateComponent.getCurrentState.getName,
        movementStateComponent.getCurrentState,
        movementStateComponent.isIdle
      )
      currentAnimation.draw(spriteBatch, animationComponent.stateTime, currentX, currentY, size)
      spriteBatch.setShader(null)
    }
  }

  override def update(deltaTime: Float): Unit = {
    forceSort()
    super.update(deltaTime)
  }
}
