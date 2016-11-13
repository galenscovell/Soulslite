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
  val EXACT_Y: Int = 720

  // Custom screen dimension units
  // Game runs at 360p (16:9, 640x360)
  val SCREEN_X: Int = 640
  val SCREEN_Y: Int = 360

  // Sprite sizes
  val SMALL_ENTITY_SIZE: Int = 16
  val MID_ENTITY_SIZE: Int = 32
  val LARGE_ENTITY_SIZE: Int = 64
  val TILE_SIZE: Int = 16

  // Camera give, if player is within this distance of environment bounds, camera will stop following
  val CAMERA_GIVE: Int = TILE_SIZE * 2 + (TILE_SIZE / 2)
}
