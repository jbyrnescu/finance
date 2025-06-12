#!/bin/bash -x

find . -name "*.java" -print | etags --language=java -o ETAGS -

