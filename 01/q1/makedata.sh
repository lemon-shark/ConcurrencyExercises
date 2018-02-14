#!/usr/bin/env bash

javac q1.java

numCircles="100"

touch data.csv

for maxRadius in `seq 1 5 1000`
do
    echo "$maxRadius"
    millis=`java q1 $maxRadius $numCircles "true"`
    echo "$maxRadius,$millis" >> data.csv
done
