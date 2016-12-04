package galenscovell.soulslite.processing

import com.badlogic.ashley.core._
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components.{SteeringComponent, _}
import galenscovell.soulslite.actors.systems._
import galenscovell.soulslite.processing.pathfinding.AStarGraph
import galenscovell.soulslite.ui.screens.GameScreen


class EntityManager(spriteBatch: SpriteBatch, controllerHandler: ControllerHandler,
                    world: World, aStarGraph: AStarGraph, gameScreen: GameScreen) {
  private val engine: Engine = new Engine

  setupSystems()


  /********************
    *       Get       *
    ********************/
  def getEngine: Engine = engine


  /********************
    *      Update     *
    ********************/
  def update(delta: Float): Unit = {
    GdxAI.getTimepiece.update(delta)
    engine.update(delta)
  }


  /********************
    *    Creation     *
    ********************/
  private def setupSystems(): Unit = {
    // Lets every other entity know where the player is currently positioned
    val whereIsPlayerSystem: WhereIsPlayerSystem = new WhereIsPlayerSystem(
      Family.all(
        classOf[AgentStateComponent],
        classOf[BodyComponent],
        classOf[WhereIsPlayerComponent]
      ).get()
    )

    // Handles states of entities
    val stateSystem: StateSystem = new StateSystem(
      Family.all(
        classOf[AgentStateComponent],
        classOf[BodyComponent],
        classOf[MovementStateComponent]
      ).get()
    )

    // Handles player controls
    val playerSystem: PlayerSystem = new PlayerSystem(
      Family.all(
        classOf[AgentStateComponent],
        classOf[BodyComponent],
        classOf[MovementStateComponent],
        classOf[PlayerComponent],
        classOf[WeaponComponent]
      ).get(), controllerHandler
    )

    // Handles AI controls
    val aiSystem: AISystem = new AISystem(
      Family.all(
        classOf[AgentStateComponent],
        classOf[BodyComponent],
        classOf[PathComponent],
        classOf[SteeringComponent]
      ).get(), aStarGraph
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
        classOf[AgentStateComponent],
        classOf[AnimationComponent],
        classOf[BodyComponent],
        classOf[MovementStateComponent],
        classOf[RenderableComponent],
        classOf[SizeComponent]
      ).get(), spriteBatch, gameScreen
    )

    engine.addSystem(whereIsPlayerSystem)
    engine.addSystem(stateSystem)
    engine.addSystem(playerSystem)
    engine.addSystem(aiSystem)
    engine.addSystem(collisionSystem)
    engine.addSystem(renderSystem)
  }
}
