package uk.gov.hmrc.agentdemofrontend.connectors

import org.jsoup.Jsoup

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.util.Try

class ZooplaScraperConnector {
  def getAverageValue(postcode:String): Future[Double] = {
    def redirectUrl = Try {
      val doc =  Jsoup.connect(s"${"https://www.zoopla.co.uk"}/search/?q=$postcode").get()
      doc.location()
    }

    def  houseValue(url: String) = Try {
      val doc =  Jsoup.connect(url).get()

      doc.getElementsByAttributeValue("class", "market-panel-stat-element-value js-market-stats-average-price")
        .first().text().replace("£", "").replace(",", "").toDouble
    }

    Future {
      blocking {
        (for {
          url <- redirectUrl
          price <- houseValue(url)
        } yield price).get
      }
    }
  }
}
