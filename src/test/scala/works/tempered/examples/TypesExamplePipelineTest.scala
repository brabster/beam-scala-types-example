package works.tempered.examples

import com.spotify.scio.testing.{PipelineSpec, TextIO}

class TypesExamplePipelineTest extends PipelineSpec {

  val input = Seq(
    "1.2.3.4,bob,2017-01-01T00:00:00.001Z,/,200",
    "1.2.3.4,alice,2017-01-01T00:01:00.001Z,/,200",
    "1.2.3.4,bob,2017-01-01T00:02:00.001Z,/blogpost,404",
    "1.2.3.4,alice,2017-01-01T00:03:00.001Z,/blogpost,404",
    "1.2.3.4,bob,2017-01-01T00:04:00.001Z,/blogpost,200",
    "1.2.3.4,bob,2017-01-01T00:04:00.001Z,/blogpost,200",
    "1.2.3.4,bob,2017-01-01T00:04:00.001Z,/blogpost,200"
  )

  val expected = Seq(
    "((alice,/,200),1)",
    "((alice,/blogpost,404),1)",
    "((bob,/,200),1)",
    "((bob,/blogpost,200),3)",
    "((bob,/blogpost,404),1)"
  )

  "noTypesPipeline" should "work" in {
    JobTest[TypesExamplePipeline.type]
      .args("--input=in.csv", "--output=out.txt")
      .input(TextIO("in.csv"), input)
      .output(TextIO("out.txt"))(_ should containInAnyOrder(expected))
      .run()
  }

}
