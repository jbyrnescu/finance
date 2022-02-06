#!/bin/sh -x

# find the correct directory and cd there to get the right db
# assuming this shell is run underneath the main shell program in dir above
# All the files will be inside the Generated directory

thisDir=`pwd`

dbDir=`ls -d -rt -C1 ../Gcsvs* | tail -1`

cd $dbDir

# we're going to do this later
cat ${thisDir}/getBagOfWords.sql | sqlite3 -bail -echo &2>> errors.txt &

${thisDir}/createWordList.sh

#sed -f ${thisDir}/createCategoryTableListing.sh

