package nl.krisgeus

import scala.collection.JavaConverters._
import com.typesafe.config.{Config, ConfigFactory}

class Settings(config: Config) {

  val dbName = config.getString("jdbc.sourcedb.db.name")
  val dbUser = config.getString("jdbc.sourcedb.db.user")
  val dbPwd  = config.getString("jdbc.sourcedb.db.pwd")
  val dbDriver = config.getString("jdbc.sourcedb.db.driver")
  val dbType = config.getString("jdbc.sourcedb.db.type")
  val dbHost = config.getString("jdbc.sourcedb.db.host")
  val dbPort = config.getInt("jdbc.sourcedb.db.port")


  val jdbcUrl = s"jdbc:${dbType}://${dbHost}:${dbPort}/${dbName}?user=${dbUser}&password=${dbPwd}&serverTimezone=UTC"

  val tableConfigs: List[Config] = config.getConfigList("jdbc.sourcedb.tables").asScala.toList
}

object Settings {
  def apply(name: String): Settings = new Settings(ConfigFactory.load(name))
}
