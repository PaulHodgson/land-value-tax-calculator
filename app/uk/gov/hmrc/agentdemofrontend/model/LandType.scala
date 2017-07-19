package uk.gov.hmrc.agentdemofrontend.model

sealed trait LandType {
  val tax: Double
}

object LandType {
  case object Residential extends LandType { val tax: Double = 0.005 }
  case object Commercial extends LandType { val tax: Double = 0.001 }
  case object Unknown extends LandType { val tax: Double = 0 }

  def toLandType(x: String): LandType =
    x.toUpperCase match {
      case "COMMERCIAL" => Commercial
      case "RESIDENTIAL" => Residential
      case _ => Unknown

    }

}

