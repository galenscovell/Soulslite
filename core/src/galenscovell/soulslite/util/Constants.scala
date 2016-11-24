package galenscovell.soulslite.util


object Constants {

  // Box2D masks
  // fixture filter category = "This is what I am"
  val WALL_CATEGORY: Short = 0x0001
  val ENTITY_CATEGORY: Short = 0x0002
  val ATTACK_CATEGORY: Short = 0x0004
  val NO_CATEGORY: Short = 0x0008

  // fixture filter mask = "This is what I collide with"
  val WALL_MASK: Short = ENTITY_CATEGORY
  val ENTITY_MASK: Short = (WALL_CATEGORY | ENTITY_CATEGORY | ATTACK_CATEGORY).toShort
  val ATTACK_ON_MASK: Short = ENTITY_CATEGORY
  val ATTACK_OFF_MASK: Short = -1
  val NO_MASK: Short = -2

  // Box2D dimensions conversion factor
  val PIXEL_PER_METER: Int = 16
  val MAX_NORMAL_VELOCITY: Int = 5
  val MAX_DASH_VELOCITY: Int = 800

  // Screen dimension units
  // Game runs at 270p (16:9, 480x270)
  val EXACT_X: Int = 1280
  val EXACT_Y: Int = 720
  val SCREEN_X: Int = 480 / PIXEL_PER_METER
  val SCREEN_Y: Int = 270 / PIXEL_PER_METER
  val UI_X: Int = 1280
  val UI_Y: Int = 720

  // Sprite sizes
  val SMALL_ENTITY_SIZE: Float = 16 / PIXEL_PER_METER
  val MID_ENTITY_SIZE: Float = 32 / PIXEL_PER_METER
  val LARGE_ENTITY_SIZE: Float = 64 / PIXEL_PER_METER
  val TILE_SIZE: Float = 16 / PIXEL_PER_METER
}
