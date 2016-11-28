package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components.StateComponent


class StateSystem(family: Family) extends IteratingSystem(family) {
  private val stateMapper: ComponentMapper[StateComponent] =
    ComponentMapper.getFor(classOf[StateComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val stateComponent: StateComponent = stateMapper.get(entity)
    stateComponent.update()
  }
}
