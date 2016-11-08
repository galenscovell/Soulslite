package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import galenscovell.soulslite.actors.components.{PositionComponent, SpriteComponent}


class RenderSystem(family: Family, spriteBatch: SpriteBatch) extends IteratingSystem(family) {
  private val spriteMapper: ComponentMapper[SpriteComponent] = ComponentMapper.getFor(classOf[SpriteComponent])
  private val positionMapper: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val spriteComponent: SpriteComponent = spriteMapper.get(entity)
    val positionComponent: PositionComponent = positionMapper.get(entity)

    spriteBatch.draw(spriteComponent.getSprite, positionComponent.x, positionComponent.y, 48, 48)
  }
}
