#!/bin/bash

cd src/main/java

javadoc -cp ../../../target/finance-0.0.1-SNAPSHOT-jar-with-dependencies.jar --source-path . accounts db finance SqliteDBUtils

