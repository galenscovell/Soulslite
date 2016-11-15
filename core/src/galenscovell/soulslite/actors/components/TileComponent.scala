package galenscovell.soulslite.actors.components

import com.badlogic.ashley.core.Component
import galenscovell.soulslite.environment.TileType.TileType
import galenscovell.soulslite.environment.{Point, TileType}


class TileComponent extends Component {
  private var tileType: TileType = TileType.EMPTY
  var neighborTilePoints: Array[Point] = _
  var floorNeighbors: Int = 0
  var bitmask: Int = 0


  /**********************
    *       State       *
    **********************/
  def isEmpty: Boolean = {
    tileType == TileType.EMPTY
  }

  def isFloor: Boolean = {
    tileType == TileType.FLOOR
  }

  def isWall: Boolean = {
    tileType == TileType.WALL
  }

  def makeEmpty(): Unit = {
    tileType = TileType.EMPTY
  }

  def makeFloor(): Unit = {
    tileType = TileType.FLOOR
  }

  def makeWall(): Unit = {
    tileType = TileType.WALL
  }
}
