import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.Accumulator;
import org.apache.spark.AccumulatorParam;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.api.java.function.Function;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import scala.Tuple2;

public class SimpleApp {

  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) {
    Logger.getLogger("akka").setLevel(Level.WARN);
    Logger.getLogger("org").setLevel(Level.WARN);
    SparkConf conf = new SparkConf().setAppName("Simple Application");
    JavaSparkContext sc = new JavaSparkContext(conf);
    
/*

    lineLengths.cache();
    
    int totalLength = lineLengths.reduce((a, b) -> a + b);
    System.out.println(totalLength);
*/
    String logFile = "/home/riyad/dev-src/thesis/*.tex"; // Should be some file on your system
    JavaRDD<String> logData = sc.textFile(logFile).cache();
    JavaRDD<String> words = logData.flatMap(s -> Arrays.asList(SPACE.split(s)));
    words.cache();
    JavaPairRDD<String, Integer> pairs = words.mapToPair(s -> new Tuple2<String, Integer>(s, 1));
    JavaPairRDD<String, Integer> count = pairs.reduceByKey( (i1, i2) -> i1 + i2);
    
    System.out.println(count.collect());

/*
    Broadcast<Integer[]> broadcastVar = sc.broadcast(new Integer[] {1, 2, 3});
    //List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
    List<Integer> data = Stream.of(1, 2, 3, 4).collect(Collectors.toList());;
    
    Accumulator<Integer> counter = sc.accumulator(0);
    JavaRDD<Integer> rdd = sc.parallelize(data);

    JavaRDD<Boolean> brdd = rdd.map(x -> Arrays.asList(broadcastVar.value()).contains(x));

    System.out.println(brdd.collect());
*/
  }
}
