package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components._


class RenderSystem(family: Family, spriteBatch: SpriteBatch) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] = ComponentMapper.getFor(classOf[BodyComponent])
  private val animationMapper: ComponentMapper[AnimationComponent] = ComponentMapper.getFor(classOf[AnimationComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val body: Body = bodyMapper.get(entity).getBody

    val animation: AnimationComponent = animationMapper.get(entity)
    animation.stateTime += deltaTime
    val currentFrame: TextureRegion = animation.getCurrentAnimation.getKeyFrame(animation.stateTime, true)

    spriteBatch.draw(currentFrame,
      body.getPosition.x - 24, body.getPosition.y - 24,
      24, 24, 48, 48,
      animation.direction, 1, 0)
  }
}
