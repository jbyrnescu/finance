#!/bin/bash

if [ "$1" == "" ];
then
    echo "Month to run budget analysis on not specified.  Exiting."
    echo "Month is 1 based and June would be 6"
    exit -1
fi

java -cp ${HOME}/Dropbox/eclipse-workspace/finance/target/finance-0.0.1-SNAPSHOT-jar-with-dependencies.jar finance.MonthlyBudget . $1 > log.txt
