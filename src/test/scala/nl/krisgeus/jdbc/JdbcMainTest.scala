package nl.krisgeus.jdbc

import nl.krisgeus.jdbc.ProcessStep.{CALCULATE_DELETES, LOAD_TO_ARCHIVE}
import org.scalatest.FunSuite

class JdbcMainTest extends FunSuite {
  test("happy flow of commandline params with short names") {
    val params: Array[String] = Array("-s", "2", "-o", "output", "-c" , "confName")
    val result = JdbcMain.verifyCommandlineParams(params)

    assert(result === Some(CommandLineParams(CALCULATE_DELETES, "confName", "output")))
    assert(result.get.configName === "confName")
    assert(result.get.outputPath === "output")
    assert(result.get.processStep === CALCULATE_DELETES)
  }

  test("happy flow of commandline params with long names") {
    val params: Array[String] = Array("--step", "2", "--output", "output", "--config" , "confName")
    val result = JdbcMain.verifyCommandlineParams(params)

    assert(result === Some(CommandLineParams(CALCULATE_DELETES, "confName", "output")))
    assert(result.get.configName === "confName")
    assert(result.get.outputPath === "output")
    assert(result.get.processStep === CALCULATE_DELETES)
  }

  test("happy flow of commandline params with short and long names combined ") {
    val params: Array[String] = Array("--step", "2", "-o", "output", "--config" , "confName")
    val result = JdbcMain.verifyCommandlineParams(params)

    assert(result === Some(CommandLineParams(CALCULATE_DELETES, "confName", "output")))
    assert(result.get.configName === "confName")
    assert(result.get.outputPath === "output")
    assert(result.get.processStep === CALCULATE_DELETES)
  }

  test("commandline params with unknown process step uses defaults") {
    val params: Array[String] = Array("--step", "42", "-o", "output", "--config" , "confName")
    val result = JdbcMain.verifyCommandlineParams(params)

    assert(result === Some(CommandLineParams(LOAD_TO_ARCHIVE, "confName", "output")))
    assert(result.get.configName === "confName")
    assert(result.get.outputPath === "output")
    assert(result.get.processStep === LOAD_TO_ARCHIVE)
  }

  test("Don;t accept commandline params with extra unknown parameters") {
    val params: Array[String] = Array("--step", "2", "--test", "testvalue", "--output", "output", "--config" , "confName")
    val result = JdbcMain.verifyCommandlineParams(params)

    assert(result === None)
  }

  test("missing requires commandline params") {
    val params: Array[String] = Array("-s", "2", "-c" , "confName")
    val result = JdbcMain.verifyCommandlineParams(params)

    assert(result === None)
  }
}
