#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"
cd "${this_dir}"

# with 2200000 nodes and 99000000 edges, program runs for ~20s
n=2200000
e=99000000


echo "this will take 1 hour"
echo "numNodes,numEdges,numThreads,milliseconds,maxNodeDegree,maxNodeColor" > results.csv
for t in 1 2 4 8 16 32 64 128 256 512
do
    for i in {1..5}
    do
        ./run.sh "${n}" "${e}" "${t}" >> results.csv
    done
done
