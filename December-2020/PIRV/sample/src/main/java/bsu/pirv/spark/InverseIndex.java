package bsu.pirv.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Usage:
 * ${SPARK_HOME}/bin/spark-submit \
 * --class=bsu.pirv.spark.InverseIndex \
 * path-to-jar path-to-wiki-folder
 */
public class InverseIndex {

//time ./bin/spark-submit  \
//      --master=local \
//      --class=bsu.pirv.spark.InverseIndex \
//      ../sample/target/*.jar ../wiki -Dio.netty.tryReflectionSetAccessible=true

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("InverseIndex");
        JavaSparkContext sc = new JavaSparkContext(conf);

        var fileContentPairRDD = sc.wholeTextFiles(args[0]);
//        fileContentPairRDD.foreach(tuple2 -> {
//            var ind = tuple2._2.indexOf('\n');
//            System.out.printf("%s : %s%n",
//                              tuple2._2.substring(0, ind),
//                              tuple2._2.substring(ind + 1, ind + 30));
//        });

        var nameContentPairRDD = fileContentPairRDD
          .mapToPair(tuple2 -> {
              var ind = tuple2._2.indexOf('\n');
              var name = tuple2._2.substring(0, ind);
              var content = tuple2._2.substring(ind + 1);
              return new Tuple2<>(name, content);
          });

        var wordNamePairRDD = nameContentPairRDD
          .flatMapToPair(tuple2 -> {
              var words = tuple2._2.split(" ");
              return Arrays.stream(words)
                .distinct()
                .map(w -> new Tuple2<>(w, tuple2._1))
                .iterator();
          });

        var invIndexRDD = wordNamePairRDD
          .groupByKey()
          .mapValues(iterable -> StreamSupport
            .stream(iterable.spliterator(), false)
            .collect(Collectors.toList()));

        invIndexRDD.saveAsTextFile("../inv-index-out");

//        invIndexRDD.collect().forEach(tuple2 -> {
//            var word = tuple2._1;
//            var wikis = tuple2._2;
//            System.out.printf("%s found in %d wikis: \n", word, wikis.size());
//            wikis.forEach(wiki -> System.out.printf("\t%s%n", wiki));
//        });


    }

}
