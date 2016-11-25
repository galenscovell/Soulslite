package galenscovell.soulslite.actors.systems

import com.badlogic.ashley.core._
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d._
import galenscovell.soulslite.actors.components._
import galenscovell.soulslite.actors.components.dynamic.ColorComponent


class CollisionSystem(family: Family, world: World) extends IteratingSystem(family) with ContactListener {
  private val bodyMapper: ComponentMapper[BodyComponent] =
    ComponentMapper.getFor(classOf[BodyComponent])
  private val weaponMapper: ComponentMapper[WeaponComponent] =
    ComponentMapper.getFor(classOf[WeaponComponent])

  world.setContactListener(this)

  // These are grabbed from the world contact listener methods
  private var collisionEntityA: Entity = _
  private var collisionEntityB: Entity = _


  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    if (collisionEntityB != null && entity == collisionEntityB) {
      val bodyComponent: BodyComponent = bodyMapper.get(entity)
      val weaponComponent: WeaponComponent = weaponMapper.get(entity)

      entity.add(new ColorComponent("white", 5))

      collisionEntityB = null
    }
  }


  /********************
    *      Box2D      *
    ********************/
  override def beginContact(contact: Contact): Unit = {
    val fixtureA: Fixture = contact.getFixtureA
    val fixtureB: Fixture = contact.getFixtureB

    // Check that instigator of contact is a weapon
    val userData = fixtureA.getUserData
    if (userData.isInstanceOf[WeaponComponent]) {
      // Check that collided fixture is not a weapon
      if (!fixtureB.getUserData.isInstanceOf[WeaponComponent]) {
        val collided = fixtureB.getBody.getUserData
        if (collided.isInstanceOf[Entity]) {
          collisionEntityB = collided.asInstanceOf[Entity]
        }
      }
    }
  }

  override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
    // Called after collision detection, but before collision resolution
    // Can query impact velocities of two bodies that have collided here
    // Can disable contacts here (needs to be set every update) contact.setEnabled(false)
  }

  override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
    // Can find info about applied impulse here eg. to check if size of collision response
    //  was over a given threshold (to check if object should break, etc.)
//        val contactNormal: Vector2 = contact.getWorldManifold.getNormal
//        val impulseAmplitude: Float = impulse.getNormalImpulses.array(0) * 100
//
//        val fixtureA: Fixture = contact.getFixtureA
//        val fixtureB: Fixture = contact.getFixtureB
//
//        val velocityA: Vector2 = fixtureA.getBody.getLinearVelocity
//        val velocityB: Vector2 = fixtureB.getBody.getLinearVelocity
//
//        fixtureB.getBody.setLinearVelocity(contactNormal.scl(impulseAmplitude))
  }

  override def endContact(contact: Contact): Unit = {

  }
}
