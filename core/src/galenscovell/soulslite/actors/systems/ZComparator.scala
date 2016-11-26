package galenscovell.soulslite.actors.systems

import java.util.Comparator

import com.badlogic.ashley.core.{ComponentMapper, Entity}
import galenscovell.soulslite.actors.components.BodyComponent


class ZComparator extends Comparator[Entity] {
  private val bodyMapper: ComponentMapper[BodyComponent] = ComponentMapper.getFor(classOf[BodyComponent])


  override def compare(e1: Entity, e2: Entity): Int = {
    val p1: Float = bodyMapper.get(e1).body.getPosition.y
    val p2: Float = bodyMapper.get(e2).body.getPosition.y

    if (p1 > p2) {
      -1
    } else if (p1 < p2) {
      1
    } else {
      0
    }
  }
}
