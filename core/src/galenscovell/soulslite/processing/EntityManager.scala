package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.ui.screens.GameScreen


class EntityManager(engine: Engine, spriteBatch: SpriteBatch, controllerHandler: ControllerHandler, world: World, gameScreen: GameScreen) {
  setupSystems()


  private def setupSystems(): Unit = {
    val movementSystem: MovementSystem = new MovementSystem(
      Family.all(classOf[BodyComponent], classOf[VelocityComponent]).get()
    )
    val renderSystem: RenderSystem = new RenderSystem(
      Family.all(
        classOf[RenderableComponent],
        classOf[AnimationComponent],
        classOf[SpriteComponent],
        classOf[BodyComponent],
        classOf[SizeComponent],
        classOf[VelocityComponent]
      ).get(), spriteBatch, gameScreen
    )
    val inputSystem: InputSystem = new InputSystem(
      Family.all(
        classOf[PlayerComponent],
        classOf[VelocityComponent],
        classOf[BodyComponent],
        classOf[WeaponComponent]
      ).get(), controllerHandler
    )

    engine.addSystem(inputSystem)
    engine.addSystem(movementSystem)
    engine.addSystem(renderSystem)
  }

  def makeEntity(player: Boolean, etype: String, size: Float, posX: Float, posY: Float): Entity = {
    val e: Entity = new Entity
    val entityBodyComponent: BodyComponent = new BodyComponent(world, posX, posY, size)
    e.add(new VelocityComponent)
    e.add(entityBodyComponent)
    e.add(new RenderableComponent)
    e.add(new AnimationComponent(etype))
    e.add(new SpriteComponent(etype))
    e.add(new SizeComponent(size))
    e.add(new WeaponComponent(world, entityBodyComponent.body))

    if (player) {
      e.add(new PlayerComponent)
    }

    engine.addEntity(e)

    e
  }

  def update(delta: Float): Unit = {
    engine.update(delta)
  }
}
