package nl.krisgeus.jdbc.load.raw

import java.time.LocalDateTime

import nl.krisgeus.Settings
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}


object JdbcSourceLoader {

  def main(args: Array[String]): Unit = {
    val now = LocalDateTime.now()

    val database = args(0)
    val settings = Settings(database)

    val jdbcUrl = settings.jdbcUrl
    val driver = settings.dbDriver

    val conf = new SparkConf().setAppName("Jdbc Source Loader")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    settings.tableConfigs.foreach { table =>
      val name = table.getString("name")
      val incremental = table.getBoolean("incremental")
      val primarykeyColumns = table.getStringList("primarykey")

      val tableDF: DataFrame = getTableAsDataframe(sqlContext, name, jdbcUrl, driver)

      tableDF.write.parquet(getOutputPath(now, name))

    }
  }

  def getOutputPath(now: LocalDateTime, name: String): String = {
    f"/tmp/sourcedb/${name}/${now.getYear}/${now.getMonthValue}%02d/${now.getDayOfMonth}%02d/"
  }

  def getTableAsDataframe(sqlContext: SQLContext, tablename: String, jdbcUrl: String, driver: String): DataFrame = {
    sqlContext.read.format("jdbc").options(Map("url"->jdbcUrl, "dbtable"->s"${getLoadQuery(tablename)}", "driver"->driver)).load()
  }

  def getLoadQuery(tableName: String, incremental: Boolean = false, lastUpdateColumn: String = "", from: Long = -1, to: Long = -1): String = {
    incremental match {
      case true => s"(select * from ${tableName} where ${lastUpdateColumn} between ${from} and ${to}) ${tableName}"
      case false => s"(select * from ${tableName}) ${tableName}"
    }
  }

  def getLoadPrimarykeysQuery(tableName: String, primaryKeys: List[String]): String = {
    s"(select ${primaryKeys.mkString(",")} from ${tableName}) ${tableName}"
  }
}
