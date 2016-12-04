package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import galenscovell.soulslite.actors.components._


class WhereIsPlayerSystem(family: Family) extends IteratingSystem(family) {
  private val agentStateMapper: ComponentMapper[AgentStateComponent] =
    ComponentMapper.getFor(classOf[AgentStateComponent])
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val whereIsPlayerMapper: ComponentMapper[WhereIsPlayerComponent] =
    ComponentMapper.getFor(classOf[WhereIsPlayerComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val agentStateComponent: AgentStateComponent = agentStateMapper.get(entity)
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val whereIsPlayerComponent: WhereIsPlayerComponent = whereIsPlayerMapper.get(entity)

    val currentPosition: Vector2 = bodyComponent.body.getPosition
    val currentPlayerPosition: Vector2 = whereIsPlayerComponent.getPlayerPosition

    agentStateComponent.setPlayerPosition(currentPlayerPosition)
    agentStateComponent.setDistanceFromPlayer(currentPosition.dst2(currentPlayerPosition))
  }
}
