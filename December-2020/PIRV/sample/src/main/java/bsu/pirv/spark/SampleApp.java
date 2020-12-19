package bsu.pirv.spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class SampleApp {

//time ./bin/spark-submit  \
//      --master=local \
//      --class=bsu.pirv.spark.sample.SampleApp \
//      ../sample/target/sample-1.0-SNAPSHOT.jar ../test.txt

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SampleApp");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile(args[0]);

        JavaRDD<String> words = lines
          .flatMap(l -> List.of(l.split(" ")).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?,?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }

        sc.stop();
    }

}
