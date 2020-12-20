

export OUT="../inv-index-out"
export IN="../wiki"

rm -rf timing.txt

for c in 1 2 3 4 8; do
#  for s in 1 4 12 24; do
  for s in 1 2 3 4 6 8 12 16 24; do
    export CORES=$c
    export SLICES=$s
    echo "$CORES cores $SLICES slices" >> timing.txt
    for repeat in {1..5}; do
      /usr/bin/time bash -c './run.sh' 2>> timing.txt
    done;
  done;
done;