package uk.gov.hmrc.agentdemofrontend.controllers

import org.scalatestplus.play.OneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.UnitSpec

class RelationshipsControllerISpec extends UnitSpec with OneAppPerSuite {
 override implicit lazy val app: Application = appBuilder.build()

  protected def appBuilder: GuiceApplicationBuilder =
    new GuiceApplicationBuilder().configure()

  private lazy val controller = app.injector.instanceOf[RelationshipsController]

  "start" should {
    "return 200 with check relationship form" in {
      val result = await(controller.start()(FakeRequest()))

      status(result) shouldBe 200
      contentAsString(result) should include("Arn")
      contentAsString(result) should include("Client ID")
    }
  }

}
