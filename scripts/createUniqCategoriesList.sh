#!/usr/bin/bash -x

FINANCE_HOME="${HOME}/Dropbox/eclipse-workspace/finance"

cat ${FINANCE_HOME}/Categorized.csv| cut -d, -f2 | sort | uniq > allCategories.csv
