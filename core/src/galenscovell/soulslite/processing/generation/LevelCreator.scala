package galenscovell.soulslite.processing.generation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.{JsonReader, JsonValue}


/**
  * LevelCreator generates the entities, items, and events necessary for a given level.
  * It pulls their names and starting locations from serialized JSON data and constructs them
  * where they need to be.
  */
class LevelCreator {
  private val dataSource: String = "data/levels.json"


  def fromJson(levelName: String): Unit = {
    val fullJson: JsonValue = new JsonReader().parse(Gdx.files.internal(dataSource))

  }
}
