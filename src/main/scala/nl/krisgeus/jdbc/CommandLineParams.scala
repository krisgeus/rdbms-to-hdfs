package nl.krisgeus.jdbc

import nl.krisgeus.jdbc.ProcessStep.Step

case class CommandLineParams(
  processStep: Step = Step.fromNumber(0),
  configName: String = "",
  outputPath: String = "")

object CommandLineOptionParser extends scopt.OptionParser[CommandLineParams]("JDBC source loader") {
  head("JDBC source loader", "0.1")

  opt[Step]('s', "step").required().valueName("<stepNumber>").action( (x, c) =>
    c.copy(processStep = x) ).text("processStep. An integer property with the step number")

  opt[String]('c', "config").required().valueName("<configName>").
    action( (x, c) => c.copy(configName = x) ).
    text("Determines the config to use")

  opt[String]('o', "output").required().action( (x, c) => c.copy(outputPath = x) ).
    text("output path")

  help("help").text("Project to load JDBC source into HDFS")

  //  note("some notes.")

}
