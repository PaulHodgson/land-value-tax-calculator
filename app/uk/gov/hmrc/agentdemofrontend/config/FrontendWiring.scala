package uk.gov.hmrc.agentdemofrontend.config

import uk.gov.hmrc.play.audit.http.config.LoadAuditingConfig
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector => Auditing}
import uk.gov.hmrc.play.config.{AppName, RunMode, ServicesConfig}
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.http.ws.{WSDelete, WSGet, WSPost, WSPut}

object FrontendAuditConnector extends Auditing with AppName with RunMode {
  override lazy val auditingConfig = LoadAuditingConfig(s"$env.auditing")
}

object WSHttp extends WSGet with WSPut with WSPost with WSDelete with AppName with RunMode {
  override val hooks = NoneRequired
}

class FrontendAuthConnector extends AuthConnector with ServicesConfig {
  val serviceUrl = baseUrl("auth")
  lazy val http = WSHttp
}