package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.util.Resources


class EntityManager(engine: Engine, spriteBatch: SpriteBatch, inputHandler: InputHandler, world: World) {
  setup()


  private def setup(): Unit = {
    val movementSystem: MovementSystem = new MovementSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[VelocityComponent]
      ).get()
    )
    val renderSystem: RenderSystem = new RenderSystem(
      Family.all(
        classOf[RenderableComponent],
        classOf[SpriteComponent],
        classOf[BodyComponent]
      ).get(),
      spriteBatch
    )
    val inputSystem: InputSystem = new InputSystem(
      Family.all(
        classOf[PlayerComponent],
        classOf[BodyComponent],
        classOf[VelocityComponent]
      ).get(),
      inputHandler
    )

    engine.addSystem(inputSystem)
    engine.addSystem(movementSystem)
    engine.addSystem(renderSystem)

    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.DynamicBody
    bodyDef.position.set(96, 96)
    val body: Body = world.createBody(bodyDef)
    val shape: PolygonShape = new PolygonShape()
    shape.setAsBox(24, 24)
    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 1f
    val fixture: Fixture = body.createFixture(fixtureDef)

    val e: Entity = new Entity
    e
      .add(new VelocityComponent(0, 0))
      .add(new BodyComponent(body, fixture))
      .add(new SpriteComponent(Resources.atlas.createSprite("player-right0")))
      .add(new RenderableComponent)
      .add(new PlayerComponent)

    shape.dispose()

    engine.addEntity(e)
  }


  def update(delta: Float): Unit = {
    engine.update(delta)
  }
}
