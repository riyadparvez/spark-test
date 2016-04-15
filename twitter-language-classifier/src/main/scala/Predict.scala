import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter._

object Predict {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: " + this.getClass.getSimpleName + " <modelDirectory>  <clusterNumber>")
      System.exit(1)
    }
    val Array(modelFile, Utils.IntParam(clusterNumber)) =
      Utils.parseCommandLineWithTwitterCredentials(args)

    val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
    val ssc = new StreamingContext(conf, Seconds(10))

    val model = new KMeansModel(ssc.sparkContext.objectFile[Vector](modelFile.toString).collect())

    val tweets = TwitterUtils.createStream(ssc, Utils.getAuth)
    val predictions = tweets.map(t => model.predict(Utils.featurize(t.getText)))
    predictions.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
