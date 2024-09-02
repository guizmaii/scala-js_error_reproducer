import _root_.org.typelevel.sbt.tpolecat.TpolecatPlugin.autoImport.*
import com.typesafe.sbt.packager.Keys.*
import org.typelevel.scalacoptions.ScalacOptions
import sbt.*
import sbt.Keys.*

object BuildHelper {

  private val javaTarget = "21"

  def env(v: String): Option[String] = sys.env.get(v)
  def unsafeEnv(v: String): String   = sys.env(v)

  lazy val stdSettings =
    noDoc ++ Seq(
      javacOptions ++= Seq("-source", javaTarget, "-target", javaTarget),
      scalacOptions ++= Seq("-no-indent"),                          // See https://x.com/ghostdogpr/status/1706589471469425074
      scalacOptions ++= Seq(s"-release:$javaTarget"),
      // scalacOptions -= "-Ykind-projector", // Scala 3.5+
      // scalacOptions += "-Xkind-projector", // Scala 3.5+
      scalacOptions --= (if (insideCI.value) Nil else Seq("-Xfatal-warnings")),
      // format: off
      tpolecatScalacOptions ++= Set(
        ScalacOptions.privateBackendParallelism(), // See https://github.com/typelevel/sbt-tpolecat/blob/main/plugin/src/main/scala/io/github/davidgregory084/ScalacOptions.scala#L409-L424
      ),
      // format: on
    )

  lazy val noDoc = Seq(
    (Compile / doc / sources)                := Seq.empty,
    (Compile / packageDoc / publishArtifact) := false,
  )

}
