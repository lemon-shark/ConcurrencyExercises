#!/usr/bin/env bash

javac Main.java

echo p,q,n,synchTime,lockfreeTime > timing.csv

p=2
n=100
for q in 2 4 8 16
do
    for i in {1..10}
    do
        java Main $p $q $n --silent >> timing.csv
    done
done
