package uk.gov.hmrc.agentdemofrontend.controllers

import javax.inject.{Inject, Singleton}

import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.agentdemofrontend.config.AppConfig
import uk.gov.hmrc.agentdemofrontend.connectors.RelationshipsConnector
import uk.gov.hmrc.agentdemofrontend.views.html
import uk.gov.hmrc.agentmtdidentifiers.model.{Arn, MtdItId}
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future

@Singleton
class RootController @Inject()(override val messagesApi: MessagesApi)
                                       (implicit appConfig: AppConfig) extends FrontendController with I18nSupport {

  val root: Action[AnyContent] = Action { implicit request =>
    Ok(html.home_page())
  }

}
