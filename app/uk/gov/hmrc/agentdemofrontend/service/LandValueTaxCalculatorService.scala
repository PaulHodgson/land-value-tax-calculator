package uk.gov.hmrc.agentdemofrontend.service

import javax.inject.{Singleton,Inject}

import uk.gov.hmrc.agentdemofrontend.connectors.ZooplaScraperConnector
import uk.gov.hmrc.agentdemofrontend.model.LandType
import uk.gov.hmrc.agentdemofrontend.model.LandType.{Commercial, Residential}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

@Singleton
class LandValueTaxCalculatorService @Inject()(zooplaScraperConnector: ZooplaScraperConnector) {

  def calc(postcode:String, land:LandType): Future[Double] = {
    val value = land match {
      case Commercial => commercialValue
      case Residential => zooplaScraperConnector.getAverageValue(postcode)
      case _ => Future.failed(new Exception("Invalid land type"))
    }

    value.map(_ * land.tax)
  }

  def commercialValue: Future[Double] = {
    Future successful 100
  }

}
