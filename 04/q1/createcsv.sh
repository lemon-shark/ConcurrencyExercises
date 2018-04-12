#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"
build_dir="${this_dir}/build"

echo "n,p,r,k,moves1,moves2,moves3,moves4" > results.csv

for p in 2 4 8
do
    ./run.sh "4" "${p}" "60" "8" >> results.csv
done
