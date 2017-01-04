package krs.common

import scala.io.Source
import com.twitter.util.{ Future, FuturePool }

trait FileSystem {
  def readFile(fileName: String): Future[String] = FuturePool.unboundedPool {
    val bufferedSource = Source.fromFile(fileName)
    val fileCache = bufferedSource.getLines.fold("")((x, y) => x + y)
    bufferedSource.close()
    fileCache
  }

}
