lazy val root = (project in file(".")).
  settings(
    name := "spark-tweet-classifier",
    version := "1.0",
    scalaVersion := "2.10.6"
  )

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
}

libraryDependencies += "com.google.code.gson" % "gson" % "2.3"
libraryDependencies += "org.twitter4j" % "twitter4j-core" % "3.0.3"

libraryDependencies += "org.apache.spark" %  "spark-core_2.10"         % "1.6.1" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-mllib"  % "1.6.1"  % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql"    % "1.6.1"    % "provided"
libraryDependencies += "org.apache.spark" %  "spark-streaming-twitter_2.10" % "1.6.1"
