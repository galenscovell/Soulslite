package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.util.Constants


class EntityManager(engine: Engine, spriteBatch: SpriteBatch, inputHandler: InputHandler, world: World) {
  setupSystems()
  makeEntity("player", 200, 200, 9, 6)
  makeEntity("rat", 400, 400, 4, 4)
  makeEntity("rat", 600, 600, 4, 4)


  private def setupSystems(): Unit = {
    val movementSystem: MovementSystem = new MovementSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[VelocityComponent]
      ).get()
    )
    val renderSystem: RenderSystem = new RenderSystem(
      Family.all(
        classOf[RenderableComponent],
        classOf[AnimationComponent],
        classOf[BodyComponent]
      ).get(),
      spriteBatch
    )
    val inputSystem: InputSystem = new InputSystem(
      Family.all(
        classOf[PlayerComponent],
        classOf[BodyComponent],
        classOf[VelocityComponent],
        classOf[AnimationComponent]
      ).get(),
      inputHandler
    )

    engine.addSystem(inputSystem)
    engine.addSystem(movementSystem)
    engine.addSystem(renderSystem)
  }

  private def makeEntity(etype: String, x: Float, y: Float, idleFrames: Int, motionFrames: Int): Unit = {
    val bodyDef: BodyDef = new BodyDef
    bodyDef.`type` = BodyType.DynamicBody
    // bodyDef.fixedRotation = true
    bodyDef.angularDamping = 1f
    bodyDef.position.set(x, y)
    val body: Body = world.createBody(bodyDef)
    val shape: PolygonShape = new PolygonShape()
    shape.setAsBox(24, 24)
    val fixtureDef: FixtureDef = new FixtureDef
    fixtureDef.shape = shape
    fixtureDef.density = 1f
    fixtureDef.friction = 1f
    fixtureDef.filter.categoryBits = Constants.ENTITY_CATEGORY
    fixtureDef.filter.maskBits = Constants.ENTITY_MASK
    val fixture: Fixture = body.createFixture(fixtureDef)

    val e: Entity = new Entity
    e.add(new VelocityComponent(0, 0))
    e.add(new BodyComponent(body, fixture))
    e.add(new RenderableComponent)
    e.add(new AnimationComponent(etype, idleFrames, motionFrames))

    if (etype == "player") {
      e.add(new PlayerComponent)
    }

    shape.dispose()
    engine.addEntity(e)
  }

  def update(delta: Float): Unit = {
    engine.update(delta)
  }
}
