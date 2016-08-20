package nl.krisgeus.jdbc.load.raw

import java.time.{LocalDateTime, Month}
import org.scalatest.{DiagrammedAssertions, FunSuite}

class JdbcSourceLoaderTest extends FunSuite with DiagrammedAssertions {

  test("determine load query based on tablename only") {
    assert(JdbcSourceLoader.getLoadQuery("table1") === "(select * from table1) table1")
  }

  test("determine load query based on tablename with increments") {
    assert(JdbcSourceLoader.getLoadQuery("table1", incremental = true, "lastUpdated", 10L, 20L) === "(select * from table1 where lastUpdated between 10 and 20) table1")
  }

  test("determine load query for keys") {
    assert(JdbcSourceLoader.getLoadPrimarykeysQuery("table1", List("col1","col2")) === "(select col1,col2 from table1) table1")
  }

  test("output path creation") {
    val dt: LocalDateTime = LocalDateTime.of(2016, Month.APRIL, 21, 0, 0, 0)
    val result: String = JdbcSourceLoader.getOutputPath(dt, "table1")
    assert(result === "/tmp/sourcedb/table1/2016/04/21/")
  }
}
