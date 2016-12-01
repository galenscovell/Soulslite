package galenscovell.soulslite.processing.generation

import com.badlogic.ashley.core.{Engine, Entity}
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.{JsonReader, JsonValue}
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.processing.BaseSteerable
import galenscovell.soulslite.processing.fsm._
import galenscovell.soulslite.util.Constants

import scala.collection.mutable.ArrayBuffer


class EntityCreator(engine: Engine, world: World) {
  private val dataSource: String = "data/entities.json"


  def makePlayer(etype: String, size: Float, posX: Float, posY: Float,
                 maxLinearSpeed: Float, maxLinearAcceleration: Float): Entity = {
    val attackShape = new Array[Vector2](4)
    attackShape(0) = new Vector2(0.75f, 0.75f)
    attackShape(1) = new Vector2(0.65f, 0.5f)
    attackShape(2) = new Vector2(0.5f, 0.65f)
    attackShape(3) = new Vector2(1.25f, 1.25f)

    val e: Entity = new Entity
    val bodyComponent: BodyComponent = new BodyComponent(e, world, posX, posY, size)
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
    e.add(new WeaponComponent(
      world, bodyComponent.body, attackShape, 90, 0, 5)
    )

    // Unique for player
    e.add(new PlayerComponent)
    e.add(new StateComponent(PlayerAgent.DEFAULT, steeringComponent))

    engine.addEntity(e)
    e
  }

  def fromJson(entityType: String, startX: Float, startY: Float,
               playerSteerable: BaseSteerable): Entity = {
    val fullJson: JsonValue = new JsonReader().parse(Gdx.files.internal(dataSource))
    val entityJson: JsonValue = fullJson.get(entityType)

    // Entity size
    val size: Float = entityJson.getString("size") match {
      case "small" => Constants.SMALL_ENTITY_SIZE
      case "mid" => Constants.MID_ENTITY_SIZE
      case "large" => Constants.LARGE_ENTITY_SIZE
    }

    // Entity agent/state type
    val agent = entityJson.getString("agent") match {
      case "ranged" => RangedAgent.DEFAULT
    }

    // Speed
    val maxLinearSpeed: Float = entityJson.getFloat("maxLinearSpeed")
    val maxLinearAcceleration: Float = entityJson.getFloat("maxLinearAcceleration")

    // Attack fixture
    val attackJson: JsonValue = entityJson.get("attack")
    val attackShape: ArrayBuffer[Vector2] = new ArrayBuffer[Vector2]()
    val shapeJson: JsonValue = attackJson.get("shape")
    for (v: Int <- 0 until shapeJson.size) {
      attackShape.append(new Vector2(
        shapeJson.get(v).getFloat(0),
        shapeJson.get(v).getFloat(1)
      ))
    }
    val startAngle: Float = attackJson.getFloat("startAngle")
    val endAngle: Float = attackJson.getFloat("endAngle")
    val attackSpeed: Float = attackJson.getFloat("speed")


    val e: Entity = new Entity
    val bodyComponent: BodyComponent = new BodyComponent(e, world, startX, startY, size)
    val steeringComponent: SteeringComponent = new SteeringComponent(
      bodyComponent.body, size, maxLinearSpeed, maxLinearAcceleration
    )

    e.add(new AnimationComponent(entityType))
    e.add(bodyComponent)
    e.add(new DirectionComponent)
    e.add(new RenderableComponent)
    e.add(new SizeComponent(size))
    e.add(new SpriteComponent(entityType))
    e.add(steeringComponent)
    e.add(new WeaponComponent(
      world, bodyComponent.body, attackShape.toArray, startAngle, 0, attackSpeed)
    )

    // Unique for non-player
    e.add(new StateComponent(agent, steeringComponent))
    e.add(new WhereIsPlayerComponent(playerSteerable))

    engine.addEntity(e)
    e
  }
}
