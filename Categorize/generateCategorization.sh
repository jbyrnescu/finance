#!/bin/sh -x

stmt1="update $tble1 set BudgetCat="
stmt2="where Description like \"%"
stmt3="%\""

generatedDir=`ls -C1 -rtd ${HOME}/Dropbox/finance/Gcsvs* | tail -1`

categoryFile="${HOME}/Dropbox/finance/Categorize/Categorized.csv"

echo generatedDir = $generatedDir

tble1="CheckingStarOneTXs"
tble2="SavingsStarOneTXs"
tble3="VisaChaseTXs"
# not yet. "CheckingWellsFargoTXs"

#tble3="MastercardBarclaysJetsTXs"
#tble4="MastercardCitibankShellTXs"
#tble6="VisaChaseTXs"

#echo CHECK
#echo $stmt1 $stmt2 $stmt3

rm ${generatedDir}/GCategorizeTXs.sql

for curBankAcct in $tble1 $tble2 $tble3
do
    cat ${categoryFile} | sed -E -e "s/^([^,]*),(.*)$/update $curBankAcct set BudgetCat=\"\\2\" where Description like \"%\\1%\";/" >> ${generatedDir}/GCategorizeTXs.sql
    echo --------------------------------------------- >> ${generatedDir}/GCategorizeTXs.sql
done

