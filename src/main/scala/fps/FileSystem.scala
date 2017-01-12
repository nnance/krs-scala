package fps

import scala.io.Source

trait FileSystem {
  def readFile(fileName: String): String = {
    val bufferedSource = Source.fromFile(fileName)
    val fileCache = bufferedSource.getLines.fold("")((x, y) => x + y)
    bufferedSource.close()
    fileCache
  }

}
