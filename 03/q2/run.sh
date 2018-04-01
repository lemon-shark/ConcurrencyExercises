#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"
cd "${this_dir}"

java -classpath "${this_dir}/build/" Main "${1}" "${2}" "${3}"
