package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite


class SpriteComponent(sprite: Sprite) extends Component {


  def getSprite: Sprite = {
    sprite
  }
}
