package galenscovell.soulslite.environment

import com.badlogic.gdx.math.{Matrix4, Vector2}
import com.badlogic.gdx.physics.box2d._


class PhysicsWorld {
  private val world: World = new World(new Vector2(0, 0), true)  // Gravity, whether to sleep or not
  private val debugWorldRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()
  debugWorldRenderer.setDrawVelocities(true)

  establishContactListener()


  private def establishContactListener(): Unit = {
    world.setContactListener(
      new ContactListener {
        override def beginContact(contact: Contact): Unit = {
          val fixtureA: Fixture = contact.getFixtureA
          val fixtureB: Fixture = contact.getFixtureB
          println(s"${fixtureA.getUserData} collided with ${fixtureB.getUserData}")
        }

        override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
          // Called after collision detection, but before collision resolution
          // Can query impact velocities of two bodies that have collided here
          // Can disable contacts here (needs to be set every update) contact.setEnabled(false)
        }

        override def endContact(contact: Contact): Unit = {

        }

        override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
          // Can find info about applied impulse here eg. to check if size of collision response
          //  was over a given threshold (to check if object should break, etc.)
//          val contactNormal: Vector2 = contact.getWorldManifold.getNormal
//          val impulseAmplitude: Float = impulse.getNormalImpulses.array(0) * 100
//
//          val fixtureA: Fixture = contact.getFixtureA
//          val fixtureB: Fixture = contact.getFixtureB
//
//          val velocityA: Vector2 = fixtureA.getBody.getLinearVelocity
//          val velocityB: Vector2 = fixtureB.getBody.getLinearVelocity
//
//          fixtureB.getBody.setLinearVelocity(contactNormal.scl(impulseAmplitude))
        }
      }
    )
  }

  def getWorld: World = {
    world
  }

  def update(timestep: Float): Unit = {
    world.step(timestep, 8, 3)
  }

  def debugRender(cameraMatrix: Matrix4): Unit = {
    debugWorldRenderer.render(world, cameraMatrix)
  }
}
