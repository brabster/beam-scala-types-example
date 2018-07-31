A short project to demonstrate Scala's types can make type signatures more useful when working with Beam pipelines.

Data pipelines in [Apache Beam](https://beam.apache.org/) have a distinctly functional flavour, whichever language you use.
That's because they can be distributed over a cluster of machines,
so careful management of state and side-effects is important.

[Spotify's Scio](https://github.com/spotify/scio) is an excellent Scala API for Beam.
Scala's functional ideas help to cut out much of the boilerplate present in the native Java API.

Scio makes good use of Scala's tuple types.
Its [PairSCollectionFunctions](https://spotify.github.io/scio/api/com/spotify/scio/values/PairSCollectionFunctions.html)
add some neat, expressive functionality to the standard [SCollection](https://spotify.github.io/scio/api/com/spotify/scio/values/SCollection.html)
to compute values based on pairs.
That capability lets you write really concise code, but can make it hard to make sense of types in the middle of your pipeline.

At this point, I think I need an example.
Let's say we're processing simple web server access logs.

Our story looks a little like this:

    As a ...
    I want to know how many times each user access each URL and the status code they received
    So that ...

Our log lines look like this:

    1.2.3.4,bob,2017-01-01T00:00:00.001Z,/,200

So we first write a case class and a parse function to turn these useless strings into something nicer to work with.

```scala
object AccessLog {

  case class Entry(clientIp: String, userId: String, timestamp: Instant, path: String, statusCode: Int)

  def parseLine(line: String): Entry = {
    val parts = line.split(",")
    Entry(parts(0), parts(1), new Instant(parts(2)), parts(3), parts(4).toInt)
  }

}
```

Now, we can build a pipeline starting with this parse function.
We'll build up the pipeline step by step, detailing the type signature at each point.

```scala
sc.textFile(args("input"))
  .map(AccessLog.parseLine)

type: SCollection[AccessLog.Entry]
```

So far so good. Now, let's map `AccessLog.Entry` onto the key we want to group by.

```scala
sc.textFile(args("input"))
  .map(AccessLog.parseLine)
  .map(x => (x.userId, x.path, x.statusCode))

type: SCollection[(String, String, Int)]
```

Yuk. Now we need to remember that the first `String` is the userId, the second is the path and the final `Int` is the statusCode.
It gets worse when we start aggregating, adding more complexity and numbers into the mix.

```scala
sc.textFile(args("input"))
  .map(AccessLog.parseLine)
  .map(x => (x.userId, x.path, x.statusCode))
  .countByValue

type: SCollection[((String, String, Int), Long)]
```

It's not too difficult to remember these things, but I'd rather not have to.
This is also a very simple pipeline - trying to keep track of what is what with more complex pipelines gets much harder!

OK, so let's back up, and use scala's `type` keyword to make the type signatures a bit more useful.

```scala
type ClientIp = String
type UserId = String
type Path = String
type StatusCode = Int
case class Entry(clientIp: ClientIp, userId: UserId, timestamp: Instant, path: Path, statusCode: StatusCode)
```

That's it. Everything still type-checks. Let's retrace our steps and see how these new types help us out.
This time, I'll comment the types at each step for brevity.

```scala
sc.textFile(args("input"))
  .map(AccessLog.parseLine)
  // SCollection[AccessLog.Entry]
  .map(x => (x.userId, x.path, x.statusCode))
  // SCollection[(AccessLog.UserId, AccessLog.Path, AccessLog.StatusCode)]
  .countByValue
  // SCollection[((AccessLog.UserId, AccessLog.Path, AccessLog.StatusCode), Long)]
```

An IDE like IntelliJ will tell you what the values you're dealing with as you code.
It's not a lot of extra thinking or code for a significant increase in the amount of information you have as you're writing
or debugging a pipeline.

