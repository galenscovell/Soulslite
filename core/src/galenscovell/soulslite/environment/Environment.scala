package galenscovell.soulslite.environment

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World

import scala.util.Random


class Environment(columns: Int, rows: Int, world: World, spriteBatch: SpriteBatch) {
  private val tiles: Array[Array[Tile]] = Array.ofDim[Tile](columns, rows)

  build()
  smooth(3)


  private def build(): Unit = {
    val random: Random = new Random()

    for (x <- 0 until columns) {
      for (y <- 0 until rows) {
        tiles(x)(y) = new Tile(x, y, world, columns, rows)
        val chance = random.nextInt(100)
        if (chance < 40) {
          tiles(x)(y).makeFloor()
        } else {
          tiles(x)(y).makeWall()
        }
      }
    }
  }

  def checkAdjacent(): Unit = {
    for (column <- tiles) {
      for (tile <- column) {
        var floorNeighbors: Int = 0
        val neighborPoints: Array[Point] = tile.getNeighborPoints
        for (p: Point <- neighborPoints) {
          if (tiles(p.x)(p.y).isFloor) {
            floorNeighbors += 1
          }
        }
        tile.floorNeighbors = floorNeighbors
      }
    }
  }

  def smooth(n: Int): Unit = {
    for (t <- 0 until n) {
      checkAdjacent()
      for (column <- tiles) {
        for (tile <- column) {
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
    for (column <- tiles) {
      for (tile <- column) {
        tile.draw(spriteBatch)
      }
    }
  }

  private def debugPrint(): Unit = {
    println()
    for (column <- tiles) {
      println()
      for (tile <- column) {
        print(tile.debugDraw)
      }
    }
  }
}
