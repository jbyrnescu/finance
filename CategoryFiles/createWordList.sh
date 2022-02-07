#!/bin/sh -x

# I'm thinking... These should be the filters:
# 1) break up words as seen in the first set of tr statements
# 2) Chop off the top with

categoryDir="/home/jbyrne/finance/CategoryFiles"

dictionaryFile="${categoryDir}/words_alpha.txt"
badWordList="${categoryDir}/knownBadKeyWordChoices.txt"

echo inside createWordList.sh

cat GsingleColDescr.txt | tr "\r" "\n" | tr "," " " | tr "/" " " | tr "." " " | tr -d "'" | tr "*" " " | tr -d "\"" | tr "@" " " | tr "&" " "| tr -s " " "\n" | sort | uniq > GmixedCaseWords.txt

sed -E -i -e '/^&.*$/ d' GmixedCaseWords.txt
sed -E -i -e '/^\#.*/ d' GmixedCaseWords.txt
sed -E -i -e '/^[0-9]+.*[0-9]+$/ d' GmixedCaseWords.txt
sed -E -i -e '/^.{1,2}$/ d' GmixedCaseWords.txt
# we don't want to delete all of them, we just want to get the root and squeeze them together
sed -E -i -e 's/^(.+)([0-9]{4,})$/\1/' GmixedCaseWords.txt
cat GmixedCaseWords.txt | sort | uniq > GmixedCaseWords2.txt

# elimate common words found in the dictionary

echo before made it here

rm -f GfinalWordList.txt

echo made it here

lineNum=0
numLines=`wc -l GmixedCaseWords2.txt | cut -d " " -f1`

echo numLines : $numLines
echo lineNum : $lineNum

while [ "$lineNum" -ne "$numLines" ]
do
    echo $i
    lineNum=`expr $lineNum + 1`
    echo $i
    i=`cat GmixedCaseWords2.txt | sed -n -e "$lineNum p"`

#     echo $i
    
#     echo lineNum : $lineNum
# done

#     lineNum=[[ lineNum + 1 ]]

    
#     echo made it inside for statement

     grepResult=`grep -i $i ${dictionaryFile}`
     grepResult2=`echo $grepResult | tr [:lower:] [:upper:]`
     grepresult3=`echo $grepResult2 | sed -n -e "/^$i\$/ p"`

     grepBadWord=`grep -i $i "${badWordList}" | tr [:lower:] [:upper:] | sed -n -e "/^$i\$/ p"`

     i=`echo $i | tr [:lower:] [:upper:]`

     if [[ "$grepResult" == "$i" ]] && [[ "$grepBadWord" == "$i" ]] ; then
 	echo $i >> GfinalWordList.txt
     fi

     read junk 1
done

# get make sure there are no ^Ms in finalWord.txt
cat GfinalWordList.txt | tr "\r" "\n" > GfinalWordList_tmp.txt

mv -f GfinalWordList_tmp.txt GfinalWordList.txt

