package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components.{SteeringComponent, _}
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.processing.pathfinding.AStarGraph
import galenscovell.soulslite.ui.screens.GameScreen


class EntityManager(spriteBatch: SpriteBatch,
                    controllerHandler: GameController,
                    world: World,
                    aStarGraph: AStarGraph,
                    gameScreen: GameScreen) {
  private val engine: Engine = new Engine

  setupSystems()


  private def setupSystems(): Unit = {
    // Lets every other entity know where the player is currently positioned
    val whereIsPlayerSystem: WhereIsPlayerSystem = new WhereIsPlayerSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[StateComponent],
        classOf[WhereIsPlayerComponent]
      ).get()
    )

    // Handles states of entities
    val stateSystem: StateSystem = new StateSystem(
      Family.all(
        classOf[StateComponent]
      ).get()
    )

    // Handles player controls
    val playerSystem: PlayerSystem = new PlayerSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[PlayerComponent],
        classOf[StateComponent],
        classOf[WeaponComponent]
      ).get(), controllerHandler
    )

    // Handles AI controls
    val aiSystem: AISystem = new AISystem(
      Family.all(
        classOf[BodyComponent],
        classOf[PathComponent],
        classOf[StateComponent],
        classOf[SteeringComponent]
      ).get(), aStarGraph
    )

    // Determines entity direction based on body velocity
    val directionSystem: DirectionSystem = new DirectionSystem(
      Family.all(
        classOf[BodyComponent],
        classOf[DirectionComponent]
      ).get()
    )

    // Handles specialized collisions
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

    engine.addSystem(whereIsPlayerSystem)
    engine.addSystem(stateSystem)
    engine.addSystem(playerSystem)
    engine.addSystem(aiSystem)
    engine.addSystem(directionSystem)
    engine.addSystem(collisionSystem)
    engine.addSystem(renderSystem)
  }

  def update(delta: Float): Unit = {
    GdxAI.getTimepiece.update(delta)
    engine.update(delta)
  }

  def getEngine: Engine = engine
}
