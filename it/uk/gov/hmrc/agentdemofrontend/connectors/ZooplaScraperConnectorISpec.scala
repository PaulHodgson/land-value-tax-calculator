package uk.gov.hmrc.agentdemofrontend.connectors

import uk.gov.hmrc.play.test.UnitSpec

class ZooplaScraperConnectorISpec extends UnitSpec{

  "getAverageValue"  should {
    "return 260000 for RH16 3UN" in {
      ZooplaScraperConnector.getAverageValue("RH16 3UN") shouldBe 260000
    }
  }

}
