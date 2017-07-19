package uk.gov.hmrc.agentdemofrontend.model

sealed trait LandType {
  val tax: Double
}

object LandType {
  case object Residential extends LandType { val tax = 0.5 }
  case object Commercial extends LandType { val tax = 1 }
}

