package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.systems.{MovementSystem, RenderSystem}
import galenscovell.soulslite.util.Resources


class EntityManager(engine: Engine, spriteBatch: SpriteBatch) {
  val cms: MovementSystem = new MovementSystem(
    Family.all(
      classOf[PositionComponent],
      classOf[VelocityComponent]
    ).get()
  )
  val rs: RenderSystem = new RenderSystem(
    Family.all(
      classOf[RenderableComponent],
      classOf[SpriteComponent],
      classOf[PositionComponent]
    ).get(),
    spriteBatch
  )

  engine.addSystem(cms)
  engine.addSystem(rs)

  val e: Entity = new Entity
  e
    .add(new PositionComponent(100, 100))
    .add(new SpriteComponent(Resources.atlas.createSprite("player-right0")))
    .add(new RenderableComponent)

  engine.addEntity(e)


  def update(delta: Float): Unit = {
    engine.update(delta)
  }
}
