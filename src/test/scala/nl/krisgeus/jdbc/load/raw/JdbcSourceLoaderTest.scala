package nl.krisgeus.jdbc.load.raw

import org.scalatest.{DiagrammedAssertions, FunSuite}

class JdbcSourceLoaderTest extends FunSuite with DiagrammedAssertions {

  test("determine load query based on tablename only") {
    assert(JdbcSourceLoader.getLoadQuery("table1") === "select * from table1")
  }

  test("determine load query based on tablename with increments") {
    assert(JdbcSourceLoader.getLoadQuery("table1", incremental = true, "lastUpdated", 10L, 20L) === "select * from table1 where lastUpdated between 10 and 20")
  }

  test("determine load query for keys") {
    assert(JdbcSourceLoader.getLoadPrimarykeysQuery("table1", List("col1","col2")) === "select col1,col2 from table1")
  }
}
