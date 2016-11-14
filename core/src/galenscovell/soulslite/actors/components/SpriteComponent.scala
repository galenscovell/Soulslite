package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import galenscovell.soulslite.util.Resources


class SpriteComponent(etype: String) extends Component {
  private val up: Sprite = Resources.atlas.createSprite("player/" + etype + "-up-idle")
  private val down: Sprite = Resources.atlas.createSprite("player/" + etype + "-down-idle")
  private val left: Sprite = Resources.atlas.createSprite("player/" + etype + "-left-idle")
  private val right: Sprite = Resources.atlas.createSprite("player/" + etype + "-right-idle")


  def getCurrentSprite(direction: Int): Sprite = {
    direction match {
      case 0 => up
      case 1 => right
      case 2 => down
      case 3 => left
    }
  }
}
