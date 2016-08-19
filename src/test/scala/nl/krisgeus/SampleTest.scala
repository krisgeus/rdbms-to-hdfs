import org.scalatest.FunSuite
import com.holdenkarau.spark.testing.SharedSparkContext
 
class SampleTest extends FunSuite with SharedSparkContext {
    test("test initializing spark context") {
        val l = List(1,2,3,4,5,6)
        val rdd = sc.parallelize(l)
 
        assert(rdd.count === l.length)
    }
}