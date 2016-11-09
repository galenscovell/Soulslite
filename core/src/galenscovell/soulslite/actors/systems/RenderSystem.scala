package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.physics.box2d.Body
import galenscovell.soulslite.actors.components._


class RenderSystem(family: Family, spriteBatch: SpriteBatch) extends IteratingSystem(family) {
  private val spriteMapper: ComponentMapper[SpriteComponent] = ComponentMapper.getFor(classOf[SpriteComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] = ComponentMapper.getFor(classOf[BodyComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val sprite: Sprite = spriteMapper.get(entity).getSprite
    val body: Body = bodyMapper.get(entity).getBody
    // Alternative: sprite.setCenter(body.getPosition.x, body.getPosition.y)

    spriteBatch.draw(
      sprite,
      body.getPosition.x - sprite.getOriginX,
      body.getPosition.y - sprite.getOriginY,
      sprite.getWidth, sprite.getHeight
    )
  }
}
