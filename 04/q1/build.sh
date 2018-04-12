#!/usr/bin/env bash

this_dir="$(dirname $(readlink -f $0))"
build_dir="${this_dir}/build"
cd "${this_dir}"

if [ ! -d "${build_dir}" ]
then
    mkdir "${build_dir}"
fi

javac Main.java -sourcepath "${this_dir}" -d "${build_dir}"
