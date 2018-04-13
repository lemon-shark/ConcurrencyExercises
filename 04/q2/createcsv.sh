#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"

echo "numThreads,timeMillis" > results.csv

for n in 0 1 2 3
do
    for i in {1..10}
    do
        ./run.sh "${n}" >> results.csv
    done
done
