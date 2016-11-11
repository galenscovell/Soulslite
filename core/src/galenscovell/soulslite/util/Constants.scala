package galenscovell.soulslite.util


object Constants {

  // Box2D masks
  // fixture filter category = "This is what I am"
  val WALL_CATEGORY: Short = 0x0001
  val ENTITY_CATEGORY: Short = 0x0002
  val EMPTY_CATEGORY: Short = 0x0004
  // fixture filter mask = "This is what I collide with"
  val WALL_MASK: Short = ENTITY_CATEGORY
  val ENTITY_MASK: Short = (WALL_CATEGORY | ENTITY_CATEGORY).toShort
  val NO_MASK: Short = -1  // Collides with nothing

  // Exact pixel dimensions
  val EXACT_X: Int = 1280
  val EXACT_Y: Int = 960

  // Custom screen dimension units
  val SCREEN_X: Int = 200
  val SCREEN_Y: Int = 120

  // Environment tile size
  val TILE_SIZE: Int = 48
}
