package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import galenscovell.soulslite.actors.components.{PositionComponent, SpriteComponent}


class RenderSystem(family: Family, spriteBatch: SpriteBatch) extends IteratingSystem(family) {
  private val sm: ComponentMapper[SpriteComponent] = ComponentMapper.getFor(classOf[SpriteComponent])
  private val pm: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val sc: SpriteComponent = sm.get(entity)
    val pc: PositionComponent = pm.get(entity)

    spriteBatch.draw(sc.getSprite, pc.x, pc.y, 48, 48)
  }
}
