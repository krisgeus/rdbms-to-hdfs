package nl.krisgeus

import org.scalatest.FunSuite
import com.holdenkarau.spark.testing.{DatasetSuiteBase}
 
class SampleTest extends FunSuite with DatasetSuiteBase {
    test("test initializing spark context") {
        val l = List(1,2,3,4,5,6)
        val rdd = sc.parallelize(l)
 
        assert(rdd.count === l.length)
    }
}
