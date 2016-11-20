package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import galenscovell.soulslite.util.Resources


class SpriteComponent(etype: String) extends Component {
  private val up: Sprite = Resources.atlas.createSprite("entity/" + etype + "-up-idle")
  private val down: Sprite = Resources.atlas.createSprite("entity/" + etype + "-down-idle")
  private val left: Sprite = Resources.atlas.createSprite("entity/" + etype + "-left-idle")
  private val right: Sprite = Resources.atlas.createSprite("entity/" + etype + "-right-idle")


  def getCurrentSprite(direction: Int): Sprite = {
    direction match {
      case 0 => right
      case 1 => up
      case 2 => left
      case 3 => down
    }
  }
}
