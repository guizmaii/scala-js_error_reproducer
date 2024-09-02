package my.org.project

import my.org.project.MySharedCode.*

import scala.scalajs.js.annotation.JSExportTopLevel

object MyCode {

  @JSExportTopLevel(name = "myExample", moduleID = "my-code")
  def myExample(a: Int): Int = mult(a, 2)

}
