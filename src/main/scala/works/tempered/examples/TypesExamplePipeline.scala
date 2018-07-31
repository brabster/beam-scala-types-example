package works.tempered.examples

import com.spotify.scio.ContextAndArgs

object TypesExamplePipeline {

  def main(cmdlineArgs: Array[String]): Unit = {

    val (sc, args) = ContextAndArgs(cmdlineArgs)

    sc.textFile(args("input"))
      .map(AccessLog.parseLine)
      .map(x => (x.userId, x.path, x.statusCode))
      .countByValue
      .saveAsTextFile(args("output"))

    sc.close().waitUntilFinish()

  }

}
