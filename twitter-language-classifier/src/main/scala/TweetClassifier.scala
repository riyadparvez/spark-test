import org.apache.spark._

object TweetClassifier {
  import org.apache.log4j.Logger
  import org.apache.log4j.Level
  
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  
  def main(args: Array[String]) = {
    val conf = new SparkConf().setAppName("streaming-hashgraph")
    val sc = new SparkContext(conf)
    val ssc = 
    
    if (args.length == 0) {
      println("Must specify an access logs file.")
      System.exit(-1)
    }
    
    sc.stop()
  }
}