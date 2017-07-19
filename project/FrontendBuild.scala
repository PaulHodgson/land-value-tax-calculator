import sbt._

object FrontendBuild extends Build with MicroService {
  import scala.util.Properties.envOrElse

  val appName = "agent-demo-frontend"
  val appVersion = envOrElse("AGENT_DEMO_FRONTEND_VERSION", "999-SNAPSHOT")

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion

  private val playReactivemongoVersion = "5.2.0"


  val compile = Seq(
    "uk.gov.hmrc" %% "play-reactivemongo" % playReactivemongoVersion,

    ws,
    "uk.gov.hmrc" %% "frontend-bootstrap" % "7.26.0",
    "uk.gov.hmrc" %% "play-partials" % "5.4.0",
    "uk.gov.hmrc" %% "play-authorised-frontend" % "6.4.0",
    "uk.gov.hmrc" %% "play-config" % "4.3.0",
    "uk.gov.hmrc" %% "logback-json-logger" % "3.1.0",
    "uk.gov.hmrc" %% "govuk-template" % "5.2.0",
    "uk.gov.hmrc" %% "play-health" % "2.1.0",
    "uk.gov.hmrc" %% "play-ui" % "7.4.0",
    "uk.gov.hmrc" %% "agent-kenshoo-monitoring" % "2.3.0",
    "uk.gov.hmrc" %% "agent-mtd-identifiers" % "0.5.0"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "2.2.6" % "test,it",
    "org.pegdown" % "pegdown" % "1.4.2" % "test,it",
    "org.jsoup" % "jsoup" % "1.7.3" % "test,it",
    "uk.gov.hmrc" %% "hmrctest" % "2.3.0" % "test,it",
    "com.typesafe.play" %% "play-test" % PlayVersion.current % "test,it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test,it"
  )

  def apply() = compile ++ test
}
