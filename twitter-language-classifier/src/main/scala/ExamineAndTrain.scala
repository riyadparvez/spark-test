import com.google.gson.{GsonBuilder, JsonParser}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object ExamineAndTrain {
  def main (args: Array[String]) {
    if (args.length < 3) {
      System.err.println("Usage: " + this.getClass.getSimpleName +
        " <tweetInput> <outputModelDir> <numClusters> <numIterations>")
      System.exit(1)
    }
    val Array(tweetInput, outputModelDir, Utils.IntParam(numClusters), Utils.IntParam(numIterations)) = args
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    
    val tweets = sc.textFile(tweetInput)
    val tweetTable = sqlContext.read.json(tweets).cache()
    tweetTable.registerTempTable("tweetTable")
    tweetTable.printSchema()
    val texts = sqlContext.sql("SELECT text from tweetTable").map(_.toString)
    val vectors = texts.map(Utils.featurize).cache()
    val model = KMeans.train(vectors, numClusters, numIterations)
    //sc.makeRDD(model.clusterCenters, numClusters).saveAsObjectFile(outputModelDir)
    val someTexts = texts.take(100)
    val predictions = someTexts.map(t => model.predict(Utils.featurize(t)) -> t).groupBy(_._1)
      //.map(t => (model.predict(Utils.featurize(t)), t)).collect()
    println(predictions.mkString)
  }
}
