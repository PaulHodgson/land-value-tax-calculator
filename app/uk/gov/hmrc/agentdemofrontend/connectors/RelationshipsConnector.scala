package uk.gov.hmrc.agentdemofrontend.connectors

import java.net.URL
import javax.inject.{Inject, Named, Singleton}

import com.codahale.metrics.MetricRegistry
import com.kenshoo.play.metrics.Metrics
import play.api.libs.json.JsValue
import uk.gov.hmrc.agent.kenshoo.monitoring.HttpAPIMonitor
import uk.gov.hmrc.agentmtdidentifiers.model.{Arn, MtdItId}
import uk.gov.hmrc.play.http.logging.MdcLoggingExecutionContext._
import uk.gov.hmrc.play.http._

import scala.concurrent.Future

@Singleton
class RelationshipsConnector @Inject()(@Named("agent-client-relationships-baseUrl") baseUrl: URL,
                                       httpGet: HttpGet,
                                       httpDelete: HttpDelete,
                                       httpPut: HttpPut,
                                       metrics: Metrics) extends HttpAPIMonitor {

  override val kenshooRegistry: MetricRegistry = metrics.defaultRegistry

  private def url(arn: Arn, mtdItId: MtdItId) =
    new URL(baseUrl, s"/agent-client-relationships/agent/${arn.value}/service/HMRC-MTD-IT/client/MTDITID/${mtdItId.value}")

  private def cleanCopyStatusRecordUrl(arn: Arn, mtdItId: MtdItId) =
    new URL(baseUrl, s"/test-only/db/agent/${arn.value}/service/HMRC-MTD-IT/client/MTDITID/${mtdItId.value}")

  private def createRelationshipUrl(arn: Arn, mtdItId: MtdItId) =
    new URL(baseUrl, s"/agent-client-relationships/agent/${arn.value}/service/HMRC-MTD-IT/client/MTDITID/${mtdItId.value}")


  def checkRelationship(arn: Arn, mtdItId: MtdItId)(implicit hc: HeaderCarrier): Future[Boolean] = {
    monitor(s"ConsumedAPI-Digital-Relationships-CheckRelationship-GET") {
      httpGet.GET[HttpResponse](url(arn, mtdItId).toString).map { x =>
        x.status match {
          case 200 => true
          case _ => false
        }
      }.recover {
        case _ => false
      }
    }
  }

  def removeRelationship(arn: Arn, mtdItId: MtdItId)(implicit hc: HeaderCarrier): Future[Unit] = {
    monitor(s"ConsumedAPI-Digital-Relationships-RemoveRelationship-DELETE") {
      httpDelete.DELETE[HttpResponse](url(arn, mtdItId).toString).map(_ => ())
    }
  }

  def cleanCopyStatusRecord(arn: Arn, mtdItId: MtdItId)(implicit hc: HeaderCarrier): Future[Unit] = {
    monitor(s"ConsumedAPI-Digital-Relationships-CleanCopyStatusRecord-DELETE") {
      httpDelete.DELETE[HttpResponse](cleanCopyStatusRecordUrl(arn, mtdItId).toString).map(_ => ())
    }
  }

  def createRelationship(arn: Arn, mtdItId: MtdItId)(implicit  hc: HeaderCarrier) = {
    monitor(s"ConsumedAPI-Digital-Relationships-CreateRelationship-PUT") {
      httpPut.PUT[String, HttpResponse](createRelationshipUrl(arn, mtdItId).toString, "")
    }
  }
}
