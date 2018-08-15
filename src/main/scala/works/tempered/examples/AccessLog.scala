package works.tempered.examples

import org.joda.time.Instant

object AccessLog {

  type ClientIp = String
  type UserId = String
  type Path = String
  type StatusCode = Int

  case class Entry(clientIp: ClientIp, userId: UserId, timestamp: Instant, path: Path, statusCode: StatusCode)

  def parseLine(line: String): Entry = line.split(",") match {
    case Array(clientIp, userId, timestamp, path, statusCode) => Entry(clientIp, userId, new Instant(timestamp), path, statusCode.toInt)
  }

}
