package galenscovell.soulslite.environment

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import galenscovell.soulslite.util.Constants

import scala.util.Random


class Environment(width: Int, height: Int, world: World, spriteBatch: SpriteBatch) {
  private val tiles: Array[Array[Tile]] = Array.ofDim[Tile](width, height)
  build()
  debugPrint()
//  smooth()
//  debugPrint()


  private def build(): Unit = {
    val random: Random = new Random()

    for (x <- 0 until width) {
      for (y <- 0 until height) {
        tiles(y)(x) = new Tile(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, world, width, height)
        val chance = random.nextInt(100)
        if (chance < 40) {
          tiles(y)(x).makeFloor()
        } else {
          tiles(y)(x).makeWall()
        }
      }
    }
  }

  def checkAdjacent(): Unit = {
    for (row <- tiles) {
      for (tile <- row) {
        var floorNeighbors: Int = 0
        val neighborPoints: Array[Point] = tile.getNeighborPoints
        for (p: Point <- neighborPoints) {
          if (tiles(p.y)(p.x).isFloor) {
            floorNeighbors += 1
          }
        }
        tile.floorNeighbors = floorNeighbors
      }
    }
  }

  def smooth(): Unit = {
    for (t <- 0 until 1) {
      checkAdjacent()
      for (row <- tiles) {
        for (tile <- row) {
          if (tile.floorNeighbors > 3) {
            tile.makeFloor()
          } else if (tile.floorNeighbors < 3) {
            tile.makeWall()
          }
        }
      }
    }
  }

  def render(): Unit = {
    for (row <- tiles) {
      for (tile <- row) {
        tile.draw(spriteBatch)
      }
    }
  }

  private def debugPrint(): Unit = {
    println()
    for (row <- tiles) {
      println()
      for (tile <- row) {
        print(tile.debugDraw)
      }
    }
  }
}
