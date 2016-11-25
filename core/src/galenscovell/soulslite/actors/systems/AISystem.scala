package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components.ai.ArrivalComponent
import galenscovell.soulslite.processing.ai.SteeringAI


class AISystem(family: Family) extends IteratingSystem(family) {
  private val aiMapper: ComponentMapper[ArrivalComponent] =
    ComponentMapper.getFor(classOf[ArrivalComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val steering: SteeringAI = aiMapper.get(entity).steeringAI

    steering.update(deltaTime)
  }
}
