package nl.krisgeus.jdbc.load.raw

import nl.krisgeus.Settings
import org.apache.spark.{SparkConf, SparkContext}

object JdbcSourceLoader {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Jdbc Source Loader")
    val sc = new SparkContext(conf)

    Settings().tableConfigs.foreach { table =>
      val name = table.getString("name")
      val incremental = table.getBoolean("increment")
      val primarykeyColumns = table.getStringList("primarykey")

    }
  }

  def getLoadQuery(tableName: String, incremental: Boolean = false, lastUpdateColumn: String = "", from: Long = -1, to: Long = -1): String = {
    incremental match {
      case true => s"select * from ${tableName} where ${lastUpdateColumn} between ${from} and ${to}"
      case false => s"select * from ${tableName}"
    }
  }

  def getLoadPrimarykeysQuery(tableName: String, primaryKeys: List[String]): String = {
    s"select ${primaryKeys.mkString(",")} from ${tableName}"
  }
}
