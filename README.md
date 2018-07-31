A short project to demonstrate how Scala's types can make type signatures more useful when working with Beam pipelines.

Our story looks a little like this:

    As a ...
    I want to know how many times each user accesses each URL and the status code they received
    So that ...

Our log lines look like this:

    1.2.3.4,bob,2017-01-01T00:00:00.001Z,/,200

- [AccessLog.scala](src/main/scala/works/tempered/examples/AccessLog.scala) - a case class and parse function for access log lines.
- [TypesExamplePipeline.scala](src/main/scala/works/tempered/examples/TypesExamplePipeline.scala) - a short pipeline to calculate counts of accesses
- [TypesExamplePipelineTest.scala](src/test/scala/works/tempered/examples/TypesExamplePipelineTest.scala) - a test for the pipeline

Use your IDE's support to show the type signature at different points in the pipeline.

Without type aliases:

```scala
sc.textFile(args("input"))
  // SCollection[String]
  .map(AccessLog.parseLine)
  // SCollection[Entry]
  .map(x => (x.userId, x.path, x.statusCode))
  // SCollection[(String, String, Int)]
  .countByValue
  // SCollection[((String, String, Int), Long)]
```

With type aliases:

```scala
sc.textFile(args("input"))
  // SCollection[String]
  .map(AccessLog.parseLine)
  // SCollection[Entry]
  .map(x => (x.userId, x.path, x.statusCode))
  // SCollection[(UserId, Path, StatusCode)]
  .countByValue
  // SCollection[((UserId, Path, StatusCode), Long)]
```