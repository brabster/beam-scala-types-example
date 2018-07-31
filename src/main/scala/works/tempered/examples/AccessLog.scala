package works.tempered.examples

import org.joda.time.Instant

object AccessLog {

  type ClientIp = String
  type UserId = String
  type Path = String
  type StatusCode = Int
  case class Entry(clientIp: ClientIp, userId: UserId, timestamp: Instant, path: Path, statusCode: StatusCode)

  def parseLine(line: String): Entry = {
    val parts = line.split(",")
    Entry(parts(0), parts(1), new Instant(parts(2)), parts(3), parts(4).toInt)
  }

}
