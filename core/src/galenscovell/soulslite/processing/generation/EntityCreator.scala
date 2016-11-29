package galenscovell.soulslite.processing.generation

import com.badlogic.ashley.core.{Engine, Entity}
import com.badlogic.gdx.physics.box2d.World
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.BaseSteerable
import galenscovell.soulslite.processing.fsm.{EnemyState, PlayerState}


class EntityCreator(engine: Engine, world: World) {


  def makePlayer(etype: String, size: Float, posX: Float, posY: Float,
                 maxLinearSpeed: Float, maxLinearAcceleration: Float): Entity = {
    val e: Entity = new Entity

    val bodyComponent: BodyComponent = new BodyComponent(
      e, world, posX, posY, size
    )
    val steeringComponent: SteeringComponent = new SteeringComponent(
      bodyComponent.body, size, maxLinearSpeed, maxLinearAcceleration
    )

    e.add(new AnimationComponent(etype))
    e.add(bodyComponent)
    e.add(new DirectionComponent)
    e.add(new RenderableComponent)
    e.add(new SizeComponent(size))
    e.add(new SpriteComponent(etype))
    e.add(steeringComponent)
    e.add(new WeaponComponent(world, bodyComponent.body))

    // Unique to player
    e.add(new PlayerComponent)
    e.add(new StateComponent(PlayerState.NORMAL, steeringComponent))

    engine.addEntity(e)
    e
  }

  def makeEntity(etype: String, size: Float, posX: Float, posY: Float,
                 maxLinearSpeed: Float, maxLinearAcceleration: Float,
                 playerSteerable: BaseSteerable): Entity = {
    val e: Entity = new Entity

    val bodyComponent: BodyComponent = new BodyComponent(
      e, world, posX, posY, size
    )
    val steeringComponent: SteeringComponent = new SteeringComponent(
      bodyComponent.body, size, maxLinearSpeed, maxLinearAcceleration
    )

    e.add(new AnimationComponent(etype))
    e.add(bodyComponent)
    e.add(new DirectionComponent)
    e.add(new RenderableComponent)
    e.add(new SizeComponent(size))
    e.add(new SpriteComponent(etype))
    e.add(steeringComponent)
    e.add(new WeaponComponent(world, bodyComponent.body))

    // Unique to non-player
    e.add(new StateComponent(EnemyState.NORMAL, steeringComponent))
    e.add(new WhereIsPlayerComponent(playerSteerable))

    engine.addEntity(e)
    e
  }
}
