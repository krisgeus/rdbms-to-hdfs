package nl.krisgeus

import org.scalatest.FunSuite
import org.scalacheck.Prop.forAll
import com.holdenkarau.spark.testing.{ColumnGenerator, DataframeGenerator, DatasetSuiteBase}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.scalacheck.Gen
import org.scalatest.prop.Checkers

class SampleTest extends FunSuite with DatasetSuiteBase with Checkers {
  test("test initializing spark context") {
    val l = List(1, 2, 3, 4, 5, 6)
    val rdd = sc.parallelize(l)

    assert(rdd.count === l.length)
  }

  test("test something with a generated dataframe") {
    val schema = StructType(List(StructField("name", StringType), StructField("age", IntegerType)))

    val dataframeGen = DataframeGenerator.arbitraryDataFrame(sqlContext, schema)

    val property =
      forAll(dataframeGen.arbitrary) {
        dataframe => dataframe.schema === schema && dataframe.count >= 0
      }

    check(property)
  }

  test("test something with a generated dataframe with specific column values") {
    val schema = StructType(List(StructField("name", StringType), StructField("age", IntegerType)))
    val nameGenerator = new ColumnGenerator("name", Gen.oneOf("Holden", "Hanafy")) // name should be on of those
    val ageGenerator = new ColumnGenerator("age", Gen.choose(10, 100))

    val dataframeGen = DataframeGenerator.arbitraryDataFrameWithCustomFields(sqlContext, schema)(nameGenerator, ageGenerator)

    val property =
      forAll(dataframeGen.arbitrary) {
        dataframe => dataframe.schema === schema &&
          dataframe.filter("(name != 'Holden' AND name != 'Hanafy') OR (age > 100 OR age < 10)").count() == 0
      }

    check(property)
  }
}
