#!/bin/bash

if [ "$1" == "" ];
then
    echo "Month to run budget analysis on not specified.  Exiting."
    exit -1
fi

java -cp ${HOME}/Dropbox/eclipse-workspace/finance/target/finance-0.0.1-SNAPSHOT-jar-with-dependencies.jar finance.MonthlyBudget . $1 > log.txt
