#!/bin/sh

# This program selects possible words to categorize transactions with
# It makes it easier to categorize based on a single word

# I'm thinking... These should be the filters:
# 1) break up words as seen in the first set of tr statements
# 2) delete words that start with &
# 3) delete words that start with #
# 4) delete words that start with 0-9 or end with 0-9
# 5) delete words that are 2 letters long
# 6) replace words that end with 4 numbers with just the word itself
# 7) filter out common words from the dictionary

categoryDir="${HOME}/finance/CategoryFiles"

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

    wordInLower=`echo $i | tr [:upper:] [:lower:]`

    wordInDictionary=`sed -n -e "/^$wordInLower$/ p" ${dictionaryFile}`
    #capsWordInDictionaryInUpper=`echo $wordInDictionary | tr [:lower:] [:upper:]`

    #     echo capsWordInDictionary = ${capsWordInDictionary}

    # checks to see if the whole word matches
    # if it doesn't this should be blank and the original word is in capsWordInDictionaryInUpper
#    capsWholeWordMatch=`echo $capsWordInDictionaryInUpper | sed -n -e "/^$i$/ p"`

#    if [[ "$capsWholeWordMatch" == "" ]] ; then
#	filterOut=${capsWholeWordMatch}
#    fi

    if expr "$wordInDictionary" != ""
    then
	filterOut=${wordInDictionary}
    else
	filterOut=""
    fi
     
    grepBadWord=`sed -n -e "/^$i\$/ p" ${badWordList} | tr [:lower:] [:upper:]`

    #     echo grepBadWord = $grepBadWord

    i=`echo $i | tr [:lower:] [:upper:]`

    if expr "$filterOut" == "" && expr "$grepBadWord" != "$i"
    then
	echo putting in final word list - $i
 	echo $i >> GfinalWordList.txt
    fi

    read junk 1
done

# get make sure there are no ^Ms in finalWord.txt
cat GfinalWordList.txt | tr "\r" "\n" > GfinalWordList_tmp.txt

mv -f GfinalWordList_tmp.txt GfinalWordList.txt

