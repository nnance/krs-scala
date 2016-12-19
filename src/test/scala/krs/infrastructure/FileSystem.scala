package krs.infrastructure

import org.scalatest._
import krs.infrastructure._

case class FileSystemTest() extends FileSystem

class FileSystemSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "readFile" should "should have a length" in {
    val source = FileSystemTest().readFile(fixtureData)
    source.length should be(405)
  }
}
