package nl.krisgeus.jdbc

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.types._
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.scalatest.FunSuite

class TestDataGenerator extends FunSuite with DataFrameSuiteBase {

  val fields: List[StructField] = List(
    StructField("id", DataTypes.LongType, false),
    StructField("name", DataTypes.StringType, false),
    StructField("description", DataTypes.StringType, true),
    StructField("lastUpdated", DataTypes.TimestampType, false)
  )
  val schema: StructType = StructType(fields)

  ignore("Generate data for incremental use case") {
    generateIncrements()
  }

  def generateIncrements() :Unit = {
      generateFirstIncrement()
      generateIncrementWithUpdatedRecord()
      generateIncrementWithInsertedRecord()
  }

  def generateFirstIncrement() {
    val year = 2016
    val month = 8
    val day = 1

    val rows : Seq[Row] = Seq(
      Row(1L, "row1", null, Timestamp.valueOf(LocalDateTime.of(year, month, day,14,21,32,123000000))),
      Row(2L, "row2", "row2 description in increment1", Timestamp.valueOf(LocalDateTime.of(year, month, day,14,21,32,124000000))),
      Row(3L, "row3", null, Timestamp.valueOf(LocalDateTime.of(year, month, day,14,21,32,125000000))),
        Row(4L, "row4", "row4 description in increment1", Timestamp.valueOf(LocalDateTime.of(year, month, day,14,21,32,126000000))),
      Row(5L, "row5", "row5 description in increment1", Timestamp.valueOf(LocalDateTime.of(year, month, day,14,21,32,127000000)))
    )
    val rdd : RDD[Row] = sc.parallelize(rows)
    val etl_load_time: LocalDateTime = LocalDateTime.of(year, month, day+1,0,0,0,0)

    saveAsParquetFile(rdd, etl_load_time)
  }

  def generateIncrementWithUpdatedRecord() {
    val year = 2016
    val month = 8
    val day = 3

    val rows: Seq[Row] = Seq(
      Row(1L, "row1", "added row1 description in increment2", Timestamp.valueOf(LocalDateTime.of(year, month, day, 16, 2, 3, 763000000)))
    )

    val rdd: RDD[Row] = sc.parallelize(rows)
    val etl_load_time: LocalDateTime = LocalDateTime.of(year, month, day + 1, 0, 0, 0, 0)

    saveAsParquetFile(rdd, etl_load_time)
  }

  def generateIncrementWithInsertedRecord() {
    val year = 2016
    val month = 8
    val day = 5

    val rows: Seq[Row] = Seq(
      Row(6L, "row6", null, Timestamp.valueOf(LocalDateTime.of(year, month, day,2,11,59,552000000)))
    )

    val rdd: RDD[Row] = sc.parallelize(rows)
    val etl_load_time: LocalDateTime = LocalDateTime.of(year, month, day + 1, 0, 0, 0, 0)

    saveAsParquetFile(rdd, etl_load_time)
  }

  def saveAsParquetFile(rdd: RDD[Row], loadTime: LocalDateTime): Unit = {
     val df: DataFrame = sqlContext.createDataFrame(rdd, schema)

      val dfWithEtlColumns = df.withColumn("etl_processing_time", lit(loadTime.toEpochSecond(ZoneOffset.UTC)))
        .withColumn("etl_year", lit(loadTime.getYear))
        .withColumn("etl_month", lit(loadTime.getMonthValue))
        .withColumn("etl_day", lit(loadTime.getDayOfMonth))

      dfWithEtlColumns.repartition(1).write.mode(SaveMode.Append).save("src/test/resources/increments.parquet")
  }
}
