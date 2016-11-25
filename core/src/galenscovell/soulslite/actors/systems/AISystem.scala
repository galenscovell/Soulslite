package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components.AIComponent
import galenscovell.soulslite.processing.ai.AISteerable


class AISystem(family: Family) extends IteratingSystem(family) {
  private val aiMapper: ComponentMapper[AIComponent] =
    ComponentMapper.getFor(classOf[AIComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val steering: AISteerable = aiMapper.get(entity).steering

    steering.update(deltaTime)
  }
}
