package uk.gov.hmrc.agentdemofrontend.repository

import play.api.libs.json.Json
import play.modules.reactivemongo.MongoDbConnection
import reactivemongo.api.{DB, ReadPreference}
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.agentdemofrontend.model.BusinessRate
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats
import uk.gov.hmrc.mongo.{ReactiveRepository, Repository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait BusinessRatesRepository {
  def findBusinessRates(postCode: String): Future[Seq[BusinessRate]]
}

object AgentStateRepository extends MongoDbConnection {
  private lazy val repository = new BusinessRatesMongoRepository(db)
  def apply(): BusinessRatesRepository = repository
}

class BusinessRatesMongoRepository(mongo: () => DB)
  extends ReactiveRepository[BusinessRate, BSONObjectID]("businessrates", mongo, BusinessRate.format, ReactiveMongoFormats.objectIdFormats)
    with BusinessRatesRepository with Repository[BusinessRate, BSONObjectID] {
  override def findBusinessRates(postCode: String): Future[Seq[BusinessRate]] = {
    collection.find(Json.obj("postcode" -> postCode)).cursor[BusinessRate](ReadPreference.primary).collect[List]()
  }
}
