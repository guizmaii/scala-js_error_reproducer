import BuildHelper.*
import org.scalajs.linker.interface.ModuleSplitStyle

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization      := "my.org"
ThisBuild / version           := "0.0.0-SNAPSHOT"
ThisBuild / scalaVersion      := "3.4.2"
ThisBuild / scalafmtCheck     := true
ThisBuild / scalafmtSbtCheck  := true
ThisBuild / scalafmtOnCompile := !insideCI.value
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision // use Scalafix compatible version
//ThisBuild / usePipelining := true // Scala 3.5+
ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("snapshots")

// ### Aliases ###

addCommandAlias("tc", "Test/compile")
addCommandAlias("ctc", "clean; Test/compile")
addCommandAlias("rctc", "reload; clean; Test/compile")
addCommandAlias("start", "~main/reStart")
addCommandAlias("stop", "reStop")
addCommandAlias("restart", "stop;start")
addCommandAlias("rst", "restart")

// ### App Modules ###

lazy val root =
  Project(id = "myproject", base = file("."))
    .disablePlugins(RevolverPlugin)
    .settings(noDoc *)
    .settings(publish / skip := true)
    .settings(crossScalaVersions := Nil) // https://www.scala-sbt.org/1.x/docs/Cross-Build.html#Cross+building+a+project+statefully,
    .aggregate(
      `scala-and-js`.jvm,
      `scala-and-js`.js,
    )

lazy val `scala-and-js` =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Full) // https://github.com/portable-scala/sbt-crossproject?tab=readme-ov-file#crosstypes
    .in(file("modules/scala-and-js"))
    .disablePlugins(RevolverPlugin)
    .enablePlugins(ScalaJSPlugin)
    .jvmSettings(stdSettings *)
    .settings(
      // ScalaJS settings
      // See https://www.scala-js.org/doc/tutorial/scalajs-vite.html#introducing-scalajs

      // Tell Scala.js that this is not an application with a main method
      scalaJSUseMainModuleInitializer := false,
      scalaJSLinkerConfig ~= {
        _.withModuleKind(ModuleKind.ESModule) // See https://www.scala-js.org/doc/project/module.html
          .withModuleSplitStyle(ModuleSplitStyle.FewestModules)
      },
    )