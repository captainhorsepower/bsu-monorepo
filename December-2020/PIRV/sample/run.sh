echo "IN=$IN OUT=$OUT cores=$CORES slices=$SLICES"
rm -rf $OUT
#  ../**/spark-submit   \
../spark-3.0.1-bin-hadoop2.7/bin/spark-submit \
    --master=local \
    --total-executor-cores=$CORES \
    --executor-cores=$CORES \
    --class=bsu.pirv.spark.InverseIndex \
    target/*.jar "$IN" "$OUT" $SLICES 2&> "spark-logs/$CORES-$SLICES.log"