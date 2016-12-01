package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components.{SteeringComponent, _}
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.ui.screens.GameScreen


class EntityManager(engine: Engine,
                    spriteBatch: SpriteBatch,
                    controllerHandler: GameController,
                    world: World,
                    gameScreen: GameScreen) {
  setupSystems()


  private def setupSystems(): Unit = {
    // Handles states of entities
    val stateSystem: StateSystem = new StateSystem(
      Family.all(
        classOf[StateComponent]
      ).get()
    )

    // Handles entity position, velocity, and physics body
    val movementSystem: MovementSystem = new MovementSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[DirectionComponent]
      ).get()
    )

    // Lets every other entity know where the player is currently positioned
    val whereIsPlayerSystem: WhereIsPlayerSystem = new WhereIsPlayerSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[WhereIsPlayerComponent]
      ).get()
    )

    // Handles player input
    val playerSystem: PlayerSystem = new PlayerSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[PlayerComponent],
        classOf[StateComponent],
        classOf[WeaponComponent]
      ).get(), controllerHandler
    )

    // Handles AI control
    val aiSystem: SteeringSystem = new SteeringSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[StateComponent],
        classOf[SteeringComponent],
        classOf[WhereIsPlayerComponent]
      ).get()
    )

    // Handles combat collisions and effects
    val collisionSystem: CollisionSystem = new CollisionSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[WeaponComponent]
      ).get(), world
    )

    // Handles entity graphics
    val renderSystem: RenderSystem = new RenderSystem(
      Family.all(
        classOf[AnimationComponent],
        classOf[BodyComponent],
        classOf[DirectionComponent],
        classOf[RenderableComponent],
        classOf[SizeComponent],
        classOf[SpriteComponent],
        classOf[StateComponent]
      ).get(), spriteBatch, gameScreen
    )

    engine.addSystem(stateSystem)
    engine.addSystem(movementSystem)
    engine.addSystem(whereIsPlayerSystem)
    engine.addSystem(playerSystem)
    engine.addSystem(aiSystem)
    engine.addSystem(collisionSystem)
    engine.addSystem(renderSystem)
  }

  def update(delta: Float): Unit = {
    GdxAI.getTimepiece.update(delta)
    engine.update(delta)
  }

  def getEngine: Engine = {
    engine
  }
}
