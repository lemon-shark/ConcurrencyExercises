#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"
cd "${this_dir}"

javac Main.java

csv_header="static,static_volatile,synchronized_static,"
csv_header+="synchronized_static_volatile_2thread,static_2thread"
echo "${csv_header}" > results.csv

for i in {1..7}
do
    java Main "${@}" >> results.csv
done
