package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.util.Resources


class EntityManager(engine: Engine, spriteBatch: SpriteBatch, inputHandler: InputHandler) {
  val movementSystem: MovementSystem = new MovementSystem(
    Family.all(
      classOf[PositionComponent],
      classOf[VelocityComponent]
    ).get()
  )
  val renderSystem: RenderSystem = new RenderSystem(
    Family.all(
      classOf[RenderableComponent],
      classOf[SpriteComponent],
      classOf[PositionComponent]
    ).get(),
    spriteBatch
  )
  val inputSystem: InputSystem = new InputSystem(
    Family.all(
      classOf[PlayerComponent],
      classOf[PositionComponent],
      classOf[VelocityComponent]
    ).get(),
    inputHandler
  )

  engine.addSystem(inputSystem)
  engine.addSystem(movementSystem)
  engine.addSystem(renderSystem)

  val e: Entity = new Entity
  e
    .add(new PositionComponent(100, 100))
    .add(new VelocityComponent(0, 0))
    .add(new SpriteComponent(Resources.atlas.createSprite("player-right0")))
    .add(new RenderableComponent)
    .add(new PlayerComponent)

  engine.addEntity(e)


  def update(delta: Float): Unit = {
    engine.update(delta)
  }
}
