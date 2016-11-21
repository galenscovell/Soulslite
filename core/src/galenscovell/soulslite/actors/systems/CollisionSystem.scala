package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.{Contact, World}
import com.badlogic.gdx.utils.Array
import galenscovell.soulslite.actors.components.{BodyComponent, WeaponComponent}


class CollisionSystem(family: Family, world: World) extends IteratingSystem(family) {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] =
    ComponentMapper.getFor(classOf[WeaponComponent])


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    val bodyComponent: BodyComponent = bodyMapper.get(entity)
    val weaponComponent: WeaponComponent = weaponMapper.get(entity)

    val contactList: Array[Contact] = world.getContactList
  }
}
