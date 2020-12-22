echo "IN=$IN OUT=$OUT cores=$CORES slices=$SLICES"; \
#  ../**/spark-submit   \
rm -rf $OUT; /usr/bin/time \
../spark-3.0.1-bin-hadoop2.7/bin/spark-submit \
    --master='local[4]' \
    --executor-cores=$CORES \
    --class=bsu.pirv.spark.InverseIndex \
    target/*.jar "$IN" "$OUT" $SLICES

  #2&> "spark-logs/$CORES-$SLICES.log"


rm -rf $OUT; /usr/bin/time \
../spark-3.0.1-bin-hadoop2.7/bin/spark-submit \
    --master=spark://localhost:7077 \
    --total-executor-cores=1 \
    --class=bsu.pirv.spark.InverseIndex \
    target/*.jar "$IN" "$OUT" $SLICES