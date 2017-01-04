package krs.common

import org.scalatest._
import com.twitter.util.{ Await }

case class FileSystemTest() extends FileSystem

class FileSystemSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "readFile" should "should have a length" in {
    val source = Await.result(FileSystemTest().readFile(fixtureData))
    source.length should be(405)
  }
}
