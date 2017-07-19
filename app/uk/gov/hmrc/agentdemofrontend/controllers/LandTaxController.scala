package uk.gov.hmrc.agentdemofrontend.controllers

import javax.inject.{Singleton,Inject}

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.agentdemofrontend.config.AppConfig
import uk.gov.hmrc.agentdemofrontend.controllers.FieldMappings._
import uk.gov.hmrc.agentdemofrontend.model.LandType
import uk.gov.hmrc.agentdemofrontend.service.LandValueTaxCalculatorService
import uk.gov.hmrc.agentdemofrontend.views.html
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future

case class LandDetails(postcode: String, land: String)

@Singleton
class LandTaxController @Inject()(override val messagesApi: MessagesApi)
                                 (implicit appConfig: AppConfig,
                                  calc: LandValueTaxCalculatorService )
  extends FrontendController with I18nSupport {


  private val landDetails = Form[LandDetails](
    mapping(
      "postcode" -> postcode,
      "land" -> nonEmptyText
    )(LandDetails.apply)(LandDetails.unapply)
  )

  def redirectToSalaryForm(): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(routes.LandTaxController.showSalaryForm()))
  }

  def showSalaryForm(): Action[AnyContent] = Action.async { implicit request =>
    Future successful Ok(html.salary(landDetails))
  }

  def explanation(): Action[AnyContent] = Action.async { implicit request =>
    Future successful Ok(html.explanation())
  }

  def reduction(): Action[AnyContent] = Action.async { implicit request =>
    Future successful Ok(html.reduction())
  }

  def submitSalaryForm(): Action[AnyContent] = Action.async {
    implicit request =>
      landDetails.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(
            Ok(html.salary(formWithErrors))
          ),
        form => {
          Future successful Redirect(routes.LandTaxController.calculateTax(form.postcode, form.land))
        }
      )
  }

  def calculateTax(postcode: String, land: String): Action[AnyContent] = Action.async { implicit request =>
    calc.calc(postcode,LandType.toLandType(land)).map { x =>
      Ok(html.calculation_complete(postcode, x))
    }.recover {
      case _=> BadRequest
    }
  }

}
