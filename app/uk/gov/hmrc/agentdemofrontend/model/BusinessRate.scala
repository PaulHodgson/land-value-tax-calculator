package uk.gov.hmrc.agentdemofrontend.model

import play.api.libs.json.{Json, Reads, Writes}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}
import uk.gov.hmrc.domain.{SimpleObjectReads, SimpleObjectWrites}
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats

/**
  * Ref: fields described in doc https://voaratinglists.blob.core.windows.net/html/documents/2017%20Compiled%20List%20and%20SMV%20Data%20Specification.pdf
  */
case class BusinessRate(

                        incrementing_entry_number: Int,
                        billing_authority_code: Int,
                        ndr_community_code: Int,
                        ba_reference_number: Long,
                        primary_and_secondary_description_code: String,
                        primary_description_text: String,
                        unique_address_reference_number_uarn: Long,

                        full_property_identifier: String,
                        firms_name: String,
                        number_or_name: String,
                        street: String,
                        town: String,
                        postal_district: String,
                        county: String,
                        postcode: String,
                        effective_date: String,
                        composite_indicator: String,
                        rateable_value: Int,
//                        appeal_settlement_code: String,
//                        assessment_reference: Long,
//                        list_alteration_date: String,
//                        scat_code_and_suffix: String,
//                        sub_street_level_3: String,
//                        sub_street_level_2: String,
//                        sub_street_level_1: String,
//                        case_number: Long,
                        current_from_date: String,
                        current_to_date: String)

object BusinessRate {
  implicit val objectIdFormats = ReactiveMongoFormats.objectIdFormats

  implicit val format = Json.format[BusinessRate]
}