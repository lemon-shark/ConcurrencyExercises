#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"

javac Main.java -sourcepath "${this_dir}" -d "${this_dir}/build/"
