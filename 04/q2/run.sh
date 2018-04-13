#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"
cd "${this_dir}"

./parallelDFA.exe "${@}"
