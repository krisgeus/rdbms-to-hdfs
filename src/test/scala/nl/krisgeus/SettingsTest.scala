package nl.krisgeus

import org.scalatest.FunSuite

class SettingsTest  extends FunSuite {
  test("table config in application.conf file") {
    val test = Settings().tableConfigs
    test.foreach { config =>
      assert(List("employees", "departments").contains(config.getString("name")))
      assert(!config.getBoolean("incremental"))
      assert(!config.getStringList("primarykey").isEmpty)
    }

  }
}
