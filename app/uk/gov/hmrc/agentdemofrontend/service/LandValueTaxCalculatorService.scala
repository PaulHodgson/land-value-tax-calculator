package uk.gov.hmrc.agentdemofrontend.service

import javax.inject.{Inject, Singleton}

import uk.gov.hmrc.agentdemofrontend.connectors.ZooplaScraperConnector
import uk.gov.hmrc.agentdemofrontend.model.LandType
import uk.gov.hmrc.agentdemofrontend.model.LandType.{Commercial, Residential}
import uk.gov.hmrc.agentdemofrontend.repository.AgentStateRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class LandValueTaxCalculatorService @Inject()(zooplaScraperConnector: ZooplaScraperConnector) {
  val repository = AgentStateRepository()

  def calc(postcode:String, land:LandType): Future[Double] = {
    val value: Future[Double] = land match {
      case Commercial => {
        repository.findBusinessRates(postcode).map(values => {
          println(values.map(_.rateable_value))
          values.map(_.rateable_value).sum / values.length
        })
      }
      case Residential => zooplaScraperConnector.getAverageValue(postcode)
      case _ => Future.failed(new Exception("Invalid land type"))
    }

    value.map(_ * land.tax)
  }

  def commercialValue: Future[Double] = {
    Future successful 100
  }

}
