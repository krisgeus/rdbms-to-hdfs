package nl.krisgeus.jdbc

import com.typesafe.config.Config
import nl.krisgeus.Settings
import nl.krisgeus.jdbc.ProcessStep.LOAD_TO_ARCHIVE
import nl.krisgeus.jdbc.load.raw.JdbcSourceLoader

object JdbcMain {
  def main(args: Array[String]): Unit = {

    verifyCommandlineParams(args) match {
      case Some(cp: CommandLineParams) =>
        val settings: Settings = Settings(cp.configName)

        cp.processStep match {
          case LOAD_TO_ARCHIVE =>
            JdbcSourceLoader(settings).run()
          case _ => println("Other runners not implemented yet")
        }

      case None =>
        //Do Nothing. Commandline parser found a problem.
    }
  }

  def verifyCommandlineParams(args: Array[String]): Option[CommandLineParams] = {
    val parser = CommandLineOptionParser
    parser.parse(args, CommandLineParams())
  }
}


