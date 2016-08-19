package nl.krisgeus

import scala.collection.JavaConverters._
import com.typesafe.config.{Config, ConfigFactory}

class Settings(config: Config) {

  val tableConfigs: List[Config] = config.getConfigList("jdbc.sourcedb.tables").asScala.toList
}

object Settings {
  def apply(): Settings = new Settings(ConfigFactory.load())
}
