package galenscovell.soulslite.environment

import com.badlogic.gdx.math.{Matrix4, Vector2}
import com.badlogic.gdx.physics.box2d._


class Physics {
  private val world: World = new World(new Vector2(0, 0), true)  // Gravity, whether to sleep or not
  private val debugWorldRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  debugWorldRenderer.setDrawVelocities(true)


  /********************
    *       Get       *
    ********************/
  def getWorld: World = world


  /********************
    *      Update     *
    ********************/
  def update(timestep: Float): Unit = {
    world.step(timestep, 8, 3)
  }


  /********************
    *      Render     *
    ********************/
  def debugRender(cameraMatrix: Matrix4): Unit = {
    debugWorldRenderer.render(world, cameraMatrix)
  }


  /********************
    *     Dispose     *
    ********************/
  def dispose(): Unit = {
    debugWorldRenderer.dispose()
    world.dispose()
  }
}
