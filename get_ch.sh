#!/usr/bin/env bash

if [ $# -lt 2 ]; then
    echo "Usage: get_ch.sh classpath classname"
    exit 1
fi

CUR_DIR=`dirname "${BASH_SOURCE[0]}"`

CLASSPATH=$1
shift

echo "digraph G{"
echo "rankdir=\"LR\"";
java -cp $CUR_DIR/out/production/classhierarchy:$CLASSPATH ClassHierarchy $@

echo "}"
