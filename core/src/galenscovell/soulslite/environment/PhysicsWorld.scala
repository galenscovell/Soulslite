package galenscovell.soulslite.environment

import com.badlogic.gdx.math.{Matrix4, Vector2}
import com.badlogic.gdx.physics.box2d._


class PhysicsWorld {
  private val world: World = new World(new Vector2(0, 0), true)  // Gravity, whether to sleep or not
  private val debugWorldRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  establishContactListener()


  private def establishContactListener(): Unit = {
    world.setContactListener(
      new ContactListener {
        override def beginContact(contact: Contact): Unit = {
          val fixtureA: Fixture = contact.getFixtureA
          val fixtureB: Fixture = contact.getFixtureB
          println(fixtureA.toString + " colliding with " + fixtureB.toString)
        }

        override def endContact(contact: Contact): Unit = {

        }

        override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {

        }

        override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {

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
