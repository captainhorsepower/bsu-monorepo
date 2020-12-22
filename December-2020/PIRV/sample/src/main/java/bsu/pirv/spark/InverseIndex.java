package bsu.pirv.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InverseIndex {

//OUT="../inv-index-out"
//IN="../wiki"
//CORES=1
//SLICES=1
//rm -rf $OUT; time \
//../**/spark-submit   \
//  --master=local \
//  --total-executor-cores=$CORES \
//  --class=bsu.pirv.spark.InverseIndex \
//  target/*.jar $IN $OUT $SLICES

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("InverseIndex");
        JavaSparkContext sc = new JavaSparkContext(conf);

        System.out.println(List.of(args));

        var inPath = args[0];
        var outPath = args[1];
        var slices = args.length == 3 ? Integer.parseInt(args[2]) : 1;

        var fileContentPairRDD = sc.wholeTextFiles(inPath);

        fileContentPairRDD = fileContentPairRDD.repartition(slices);

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
            .collect(Collectors.toList())
          );

        invIndexRDD
          .filter(tuple -> tuple._2.size() > 10)
          .saveAsTextFile(outPath);

//        invIndexRDD.collect().forEach(tuple2 -> {
//            var word = tuple2._1;
//            var wikis = tuple2._2;
//            System.out.printf("%s found in %d wikis: \n", word, wikis.size());
//            wikis.forEach(wiki -> System.out.printf("\t%s%n", wiki));
//        });

    }

}
