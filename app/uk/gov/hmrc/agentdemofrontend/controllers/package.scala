package uk.gov.hmrc.agentdemofrontend

import play.api.data.Forms.nonEmptyText
import play.api.data.Mapping
import uk.gov.hmrc.agentmtdidentifiers.model.{Arn, MtdItId}

package object controllers {

  object FieldMappings {

    def arn: Mapping[Arn] = nonEmptyText.transform[Arn](Arn.apply, _.value)
      .verifying("error.arn.invalid", arn => Arn.isValid(arn.value))

    def mtdItId: Mapping[MtdItId] = nonEmptyText.transform[MtdItId](MtdItId.apply, _.value)
      .verifying("error.mtdItId.invalid", mtdItId => MtdItId.isValid(mtdItId.value))
  }

}
