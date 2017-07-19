package uk.gov.hmrc.agentdemofrontend.controllers

import javax.inject.{Inject, Singleton}

import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.agentdemofrontend.config.AppConfig
import uk.gov.hmrc.agentdemofrontend.connectors.RelationshipsConnector
import uk.gov.hmrc.agentdemofrontend.views.html
import uk.gov.hmrc.agentmtdidentifiers.model.{Arn, MtdItId}
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.play.http.HeaderCarrier

import scala.concurrent.Future

@Singleton
class RelationshipsController @Inject()(override val messagesApi: MessagesApi,
                                        relationshipConnector: RelationshipsConnector)(implicit appConfig: AppConfig) extends FrontendController with I18nSupport {

  private val relationshipForm = Form[RelationshipDetails](
    mapping(
      "arn" -> FieldMappings.arn,
      "clientId" -> FieldMappings.mtdItId
    )(RelationshipDetails.apply)(RelationshipDetails.unapply)
  )

  val start: Action[AnyContent] = Action { implicit request =>
    Ok(html.check_relationship(relationshipForm))
  }

  val check: Action[AnyContent] = Action.async { implicit request =>
    relationshipForm.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(
          Ok(html.check_relationship(formWithErrors))
        ),
      form => {
        val (arn, mtdItId) = (form.arn, form.clientId)
        relationshipConnector.checkRelationship(arn, mtdItId).map {
          case true  => Ok(html.remove_relationship(
              relationshipForm.bind(Map("arn" -> arn.value, "clientId" -> mtdItId.value)))
            )
          case false =>  Ok(
            html.create_relationship(
              relationshipForm.bind(Map("arn" -> arn.value, "clientId" -> mtdItId.value)),
              Some(s"The relationship between Agent $arn and Client $mtdItId has not been found.")
            )
          )
        }
      }
    )
  }

  val remove: Action[AnyContent] = Action.async { implicit request =>
    relationshipForm.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(
          Ok(html.remove_relationship(formWithErrors))
        ),
      form => {
        val (arn, mtdItId) = (form.arn, form.clientId)
        (for {
          _ <- relationshipConnector.removeRelationship(arn, mtdItId)
        } yield Ok(
            html.clean_copy_status(
              relationshipForm.bind(Map("arn" -> arn.value, "clientId" -> mtdItId.value)),
              Some(s"The request for removing relationship between Agent $arn and Client $mtdItId has been sent.")
            )
          ))
          .recoverWith {
            case ex =>
              Future.successful(Redirect(routes.RelationshipsController.showErrors()).flashing("error" -> ex.getMessage))
          }
      }
    )
  }

  val cleanCopyStatusRecord: Action[AnyContent] = Action.async { implicit request =>
    relationshipForm.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(
          Ok(html.clean_copy_status(formWithErrors))
        ),
      form => {
        val (arn, mtdItId) = (form.arn, form.clientId)
        val nextStep = routes.RelationshipsController.start()

        run( s"The CESA copy status record for Agent $arn and Client $mtdItId has been successfully removed.", nextStep)(
          relationshipConnector.cleanCopyStatusRecord(arn, mtdItId))
      }
    )
  }

  val createForm: Action[AnyContent] = Action { implicit request =>
    Ok(html.create_relationship(relationshipForm))
  }

  val create: Action[AnyContent] = Action.async { implicit request =>
    relationshipForm.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(Ok(html.create_relationship(formWithErrors))),
      form => {
        val (arn, mtdItId) = (form.arn, form.clientId)
        val nextStep = routes.RelationshipsController.start()
        
        run(s"The CESA copy status record for Agent $arn and Client $mtdItId has been successfully created.", nextStep)(
          relationshipConnector.createRelationship(arn, mtdItId))
      }
    )
  }

  val showErrors: Action[AnyContent] = Action { implicit request =>
    Ok(html.error_template("", "", ""))
  }

  val success: Action[AnyContent] = Action { implicit request =>
    Ok(html.success_template("", "", ""))
  }

  private def run[A](message: String, nextStep: Call)(f: => Future[A])(implicit hc: HeaderCarrier): Future[Result] =
    f.map { _ =>
      Redirect(nextStep).flashing("message" -> message)
    }.recoverWith {
      case ex =>
        Future.successful(Redirect(routes.RelationshipsController.showErrors()).flashing("error" -> ex.getMessage))
    }
}

case class RelationshipDetails(arn: Arn, clientId: MtdItId)
