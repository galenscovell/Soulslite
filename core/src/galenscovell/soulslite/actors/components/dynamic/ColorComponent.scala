package galenscovell.soulslite.actors.components.dynamic

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram


class ColorComponent(tint: String, frames: Int) extends Component {
  val shader: ShaderProgram = setupShader
  var currentFrame: Float = frames
  var time: Float = 0f


  private def setupShader: ShaderProgram = {
    val shader = new ShaderProgram(
      Gdx.files.internal(s"shaders/colors/basic.vert").readString(),
      Gdx.files.internal(s"shaders/colors/$tint.frag").readString()
    )
    if (!shader.isCompiled) {
      println(shader.getLog)
    }

    shader
  }

  def step(): Boolean = {
    currentFrame -= 1
    currentFrame <= 0
  }

  def dispose(): Unit = {
    shader.dispose()
  }
}
