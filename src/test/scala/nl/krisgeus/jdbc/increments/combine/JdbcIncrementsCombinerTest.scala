package nl.krisgeus.jdbc.increments.combine

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.scalatest.FunSuite
import org.apache.spark.sql.functions._

class JdbcIncrementsCombinerTest extends FunSuite with DataFrameSuiteBase {

  test("test windowing functions") {

    val increments = sqlContext.read.parquet("src/test/resources/increments.parquet")

    increments.registerTempTable("increments")

    //    DataFrame result = sqlContext().sql("select inc.* FROM (SELECT *, dense_rank() over (PARTITION BY id ORDER BY etl_processing_time DESC) as rank  FROM increments) inc WHERE rank = 1");
    val w: WindowSpec = Window.partitionBy("id").orderBy(increments.col("etl_processing_time").desc)
    val result: DataFrame = increments.withColumn("rank", dense_rank.over(w)).where("rank = 1")

    assert(result.count === 6)
  }
}
