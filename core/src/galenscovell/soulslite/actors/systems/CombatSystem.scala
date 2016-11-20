package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import galenscovell.soulslite.actors.components.{BodyComponent, WeaponComponent}


class CombatSystem(family: Family) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] =
    ComponentMapper.getFor(classOf[WeaponComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    if (weaponComponent.attacking) {

    }
  }
}
