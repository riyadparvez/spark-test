import java.io.File

import com.google.gson.Gson
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object Collect {
  private var numTweetsCollected = 0L
  private var gson = new Gson()

  def main(args: Array[String]) {
    if (args.length < 3) {
      System.err.println("Usage: " + this.getClass.getSimpleName +
        "<outputDirectory> <numTweetsToCollect> <intervalInSeconds> <partitionsEachInterval>")
      System.exit(1)
    }
    val Array(outputDirectory, Utils.IntParam(numTweetsToCollect),  Utils.IntParam(intervalSecs), Utils.IntParam(partitionsEachInterval)) =
      Utils.parseCommandLineWithTwitterCredentials(args)
    val outputDir = new File(outputDirectory.toString)
    if (outputDir.exists()) {
      System.err.println("ERROR - %s already exists: delete or specify another directory".format(
        outputDirectory))
      System.exit(1)
    }
    outputDir.mkdirs()
    
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(intervalSecs))
    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth).map(gson.toJson(_))
    tweetStream.foreachRDD((rdd, time) => {
      val count = rdd.count()
      if (count > 0) {
        val outputRDD = rdd.repartition(partitionsEachInterval)
        outputRDD.saveAsTextFile(outputDirectory + "/tweets_" + time.milliseconds.toString)
        numTweetsCollected = numTweetsCollected + count
        if (numTweetsCollected > numTweetsToCollect) {
          System.exit(0)
        }
      }
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
