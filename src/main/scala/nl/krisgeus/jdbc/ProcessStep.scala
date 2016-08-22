package nl.krisgeus.jdbc

object ProcessStep {
  sealed abstract class Step(
    val number: Int,
    val name: String,
    val description: String
  ) extends Ordered[Step] {

    def compare(that: Step) = this.number - that.number

    override def toString: String = name
  }

  object Step {
    def fromNumber(number:Int): Step = {
      number match {
        case LOAD_TO_ARCHIVE.number => LOAD_TO_ARCHIVE
        case COMBINE_INCREMENTS.number => COMBINE_INCREMENTS
        case CALCULATE_DELETES.number => CALCULATE_DELETES
        case LOAD_CURRENT.number => LOAD_CURRENT
        case CALCULATE_DELTAS.number => CALCULATE_DELTAS
        case _ => LOAD_TO_ARCHIVE
      }
    }
  }

  implicit val stepRead: scopt.Read[Step] =
    scopt.Read.reads {
      Step fromNumber _.toInt
    }

  case object LOAD_TO_ARCHIVE extends Step(0, "loadArchive", "Load tables form JDBC source into parquet format to HDFS archive directory.")
  case object COMBINE_INCREMENTS extends Step(1, "combineIncrements", "Combine all historic increments to determine current situation.")
  case object CALCULATE_DELETES extends Step(2, "calculateDeletedRecords", "Compare current situation with full load of primary keys to determine deleted records.")
  case object LOAD_CURRENT extends Step(3, "loadCurrent", "Combine current situation and deleted records to calculate exact copy of source")
  case object CALCULATE_DELTAS extends Step(4, "calculateDeltas", "Determine deltas from current and previous current")
}
