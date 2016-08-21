package nl.krisgeus

import org.scalatest.FunSuite

class SettingsTest  extends FunSuite {
  test("table config in employees.conf file") {
    val test = Settings("employees").tableConfigs
    test.foreach { config =>
      assert(List("employees", "departments").contains(config.getString("name")))
      assert(!config.getBoolean("incremental"))
      assert(!config.getStringList("primarykey").isEmpty)
    }
  }

  test("table config in testdatabase.conf file") {
    val test = Settings("testdatabase").tableConfigs
    test.foreach { config =>
      assert(List("testtable1", "testtable2").contains(config.getString("name")))
      assert(List(true, false).contains(config.getBoolean("incremental")))
      assert(!config.getStringList("primarykey").isEmpty)
    }

  }

}
