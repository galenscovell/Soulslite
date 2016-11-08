package galenscovell.soulslite.processing

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import galenscovell.soulslite.util.Constants


class EnvironmentRenderer {
  private val spriteBatch: SpriteBatch = new SpriteBatch()

  private var camera: OrthographicCamera = _
  private var viewport: FitViewport = _

  private val lerpPos: Vector3 = new Vector3(0, 0, 0)
  private var minCamX, minCamY, maxCamX, maxCamY: Float = 0.0f

  create()


  private def create(): Unit = {
    camera = new OrthographicCamera(Constants.EXACT_X, Constants.EXACT_Y)
    viewport = new FitViewport(Constants.EXACT_X, Constants.EXACT_Y, camera)
    camera.setToOrtho(true, Constants.EXACT_X, Constants.EXACT_Y)
  }


  /********************
    *     Render      *
    ********************/
  def render(interpolation: Double): Unit = {
    findCameraBounds()

//    if (cameraFollow) {
//      centerOnPlayer()
//    }

    spriteBatch.setProjectionMatrix(camera.combined)
    spriteBatch.begin()

    // Render environment, iterate through tiles
    // Render entities, iterate through them
    // Render effects/lighting

    spriteBatch.end()
  }


  /********************
    *     Camera      *
    ********************/
  def resize(width: Int, height: Int): Unit = {
    viewport.update(width, height, true)
    //    camera.position.set(hero.getCurrentX(), hero.getCurrentY(), 0)
  }

  private def getCamera: OrthographicCamera = {
    camera
  }

  private def centerOnPlayer(): Unit = {
//    lerpPos.set(hero.getCurrentX(), hero.getCurrentY(), 0);
    camera.position.lerp(lerpPos, 0.1f);
  }

  private def findCameraBounds(): Unit = {
    // Find camera upper left coordinates
    minCamX = camera.position.x - (camera.viewportWidth / 2) * camera.zoom;
    minCamY = camera.position.y - (camera.viewportHeight / 2) * camera.zoom;
    // Find camera lower right coordinates
    maxCamX = minCamX + camera.viewportWidth * camera.zoom;
    maxCamY = minCamY + camera.viewportHeight * camera.zoom;
    camera.update();
  }

  private def inViewport(x: Int, y: Int): Boolean = {
    (x + 48) >= minCamX &&
      x <= maxCamX &&
      (y + 48) >= minCamY &&
      y <= maxCamY
  }
}
