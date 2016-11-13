package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.ui.screens.GameScreen


class EntityManager(engine: Engine, spriteBatch: SpriteBatch, inputHandler: InputHandler, world: World, gameScreen: GameScreen) {
  setupSystems()


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
        classOf[BodyComponent],
        classOf[SizeComponent]
      ).get(),
      spriteBatch,
      gameScreen
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

  def makeEntity(etype: String, size: Int, posX: Float, posY: Float, idleFrames: Int, motionFrames: Int): Entity = {
    val e: Entity = new Entity
    e.add(new VelocityComponent(0, 0))
    e.add(new BodyComponent(world, posX, posY, size))
    e.add(new RenderableComponent)
    e.add(new AnimationComponent(etype, idleFrames, motionFrames))
    e.add(new SizeComponent(size))

    if (etype == "player") {
      e.add(new PlayerComponent)
    }

    engine.addEntity(e)

    e
  }

  def update(delta: Float): Unit = {
    engine.update(delta)
  }
}
