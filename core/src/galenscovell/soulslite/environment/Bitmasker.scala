package galenscovell.soulslite.environment


/**
  * Generates bitmask for evironment tile skinning.
  *    1         1       Total = (Sum of occupied values)
  *  8 * 2       * 2     ex total = (1 + 2) = 3
  *    4
  * Bitmask value range: 0, 15 (None occupied, all occupied)
  * Bitmask value determines sprite of Tile.
  */
class Bitmasker {

  def findBitmask(tile: Tile, tiles: Array[Array[Tile]]): Int = {
    var result: Int = 0
    val neighborPoints: Array[Point] = tile.getNeighborPoints

    // If analyzed tile is wall, checks if neighbors are walls
    // If analyzed tile is floor, checks if neighbors are walls or water
    // If analyzed tile is water, checks if neighbors are floors or walls
    for (p <- neighborPoints) {
      val neighborTile: Tile = tiles(p.x)(p.y)
      if (neighborTile != null && neighborTile.isWall) {
        val diffX: Int = tile.tx - neighborTile.tx
        val diffY: Int = tile.ty - neighborTile.ty

        if (diffX == -1 && diffY == 0) {
          result += 2
        } else if (diffX == 0) {
          if (diffY == -1) {
            result += 4
          } else if (diffY == 1) {
            result += 1
          }
        } else if (diffX == 1 && diffY == 0) {
          result += 8
        }
      }
    }

    result
  }
}
