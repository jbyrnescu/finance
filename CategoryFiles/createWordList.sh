#!/bin/sh -x

# I'm thinking... These should be the filters:
# 1) break up words as seen in the first set of tr statements
# 2) Chop off the top with

categoryDir="/home/jbyrne/finance/CategoryFiles"

dictionaryFile="${categoryDir}/words_alpha.txt"
badWordList="${categoryDir}/GknownBadKeyWordChoices.txt"

echo inside createWordList.sh

cat GsingleColDescr.txt | tr "\r" "\n" | tr "," " " | tr "@" " " | tr "/" " " | tr "." " " | tr -d "'" | tr "*" " " | tr -d "\"" | tr -s " " "\n" | sort | uniq > GmixedCaseWords.txt

sed -E -i -e '/^&.*$/ d' GmixedCaseWords.txt
sed -E -i -e '/^\#.*/ d' GmixedCaseWords.txt
sed -E -i -e '/^[0-9]+.*[0-9]+$/ d' GmixedCaseWords.txt
sed -E -i -e '/^.{1,2}$/ d' GmixedCaseWords.txt
# we don't want to delete all of them, we just want to get the root and squeeze them together
sed -E -i -e 's/^(.+)([0-9]{4,})$/\1/' GmixedCaseWords.txt
cat GmixedCaseWords.txt | sort | uniq > GmixedCaseWords2.txt

# elimate common words found in the dictionary

rm -f GfinalWordList.txt

for i in `cat GmixedCaseWords2.txt`
do
    grepResult=`grep -i $i "${dictionaryFile}"`
    grepBadWord=`grep -i $i "${badWordList}"`

    if [ "$grepResult" == "" ] && [ "$grepBadWord" == "" ]
    then
       echo $i >> GfinalWordList.txt
    fi
done

# get make sure there are no ^Ms in finalWord.txt
cat GfinalWordList.txt | tr "\r" "\n" > GfinalWordList_tmp.txt

mv -f GfinalWordList_tmp.txt GfinalWordList.txt

