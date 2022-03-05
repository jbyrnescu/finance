#!/bin/sh -x

filename=$1
baseDir=${HOME}/Dropbox/finance


cat $filename | cut -d, -f 1,2 | tr "/" "~" > ${filename}_1
cat $filename | cut -d, -f 3- > ${filename}_2

#This is done with tr.  Don't need sed
#cat ${filename}_1 | sed -f ${baseDir}/Massage/sedChaseForSlash.sed > ${filename}_1_1

paste -d, ${filename}_1 ${filename}_2 > $filename
