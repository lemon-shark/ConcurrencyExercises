#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"
build_dir="${this_dir}/build"
cd "${build_dir}"

java Main "${@}"
