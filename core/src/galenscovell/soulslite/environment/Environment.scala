package galenscovell.soulslite.environment

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import galenscovell.soulslite.util.Constants

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


class Environment(columns: Int, rows: Int, world: World, spriteBatch: SpriteBatch) {
  private val tiles: Array[Array[Tile]] = Array.ofDim[Tile](columns, rows)
  private val dimensions: Vector2 = new Vector2(columns * Constants.TILE_SIZE, rows * Constants.TILE_SIZE)

  build()
  findNeighborPoints()
  smooth(3)
  skin()


  def getDimensions: Vector2 = {
    dimensions
  }

  def render(): Unit = {
    for (column <- tiles) {
      for (tile <- column) {
        tile.draw(spriteBatch)
      }
    }
  }

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

  private def checkAdjacent(): Unit = {
    for (column <- tiles) {
      for (tile <- column) {
        var floorNeighbors: Int = 0
        val neighborPoints: Array[Point] = tile.neighborTilePoints
        for (p: Point <- neighborPoints) {
          if (tiles(p.x)(p.y).isFloor) {
            floorNeighbors += 1
          }
        }
        tile.floorNeighbors = floorNeighbors
      }
    }
  }

  private def smooth(n: Int): Unit = {
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

  private def skin(): Unit = {
    val bitmasker: Bitmasker = new Bitmasker
    for (column <- tiles) {
      for (tile <- column) {
        val mask: Int = bitmasker.findBitmask(tile, tiles)
        tile.setBitmask(mask)
        tile.skin()
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


  private def findNeighborPoints(): Unit = {
    for (column <- tiles) {
      for (tile <- column) {
        val points: ArrayBuffer[Point] = new ArrayBuffer[Point]()
        var sumX, sumY: Int = 0

        for (x <- -1 to 1) {
          for (y <- -1 to 1) {
            sumX = tile.tx + x
            sumY = tile.ty + y

            if (!(sumX == tile.tx && sumY == tile.ty) && !isOutOfBounds(sumX, sumY)) {
              points.append(new Point(sumX, sumY))
            }
          }
        }

        tile.neighborTilePoints = points.toArray
      }
    }
  }

  private def isOutOfBounds(x: Int, y: Int): Boolean = {
    (x < 0 || y < 0) || (x >= columns || y >= rows)
  }
}
