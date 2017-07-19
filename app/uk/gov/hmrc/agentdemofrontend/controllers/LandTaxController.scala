package uk.gov.hmrc.agentdemofrontend.controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, _}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Request}
import uk.gov.hmrc.agentdemofrontend.config.AppConfig
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.agentdemofrontend.controllers.FieldMappings._
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.agentdemofrontend.views.html

import scala.concurrent.Future

case class LandDetails(postcode: String, land: String)

class LandTaxController @Inject()(override val messagesApi: MessagesApi)
                                 (implicit appConfig: AppConfig)
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
    val calculatedAmount: Float = 123123

    Future successful Ok(html.calculation_complete(postcode, calculatedAmount))
  }

}
