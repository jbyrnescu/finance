#!/bin/sh

stmt1="update $tble1 set BudgetCat="
stmt2="where Description like \"%"
stmt3="%\""

generatedDir=`ls -C1 -rtd ../Gcsvs* | tail -1`

#echo generatedDir = $generatedDir

tble1="CheckingStarOneTXs"
tble2="SavingsStarOneTXs"
tble3="VisaChaseTXs"
# not yet. "CheckingWellsFargoTXs"

#tble3="MastercardBarclaysJetsTXs"
#tble4="MastercardCitibankShellTXs"
#tble6="VisaChaseTXs"

#echo CHECK
#echo $stmt1 $stmt2 $stmt3

rm GMarkAsMandTXs.sql

for curBankAcct in $tble1 $tble2 $tble3
do
    cat MandatoryMap.csv | sed -E -e "s/^([^,]*),(.*)$/update $curBankAcct set Mandatory=\"\\2\" where Description like \"%\\1%\";/" >> ${generatedDir}/GMarkAsMandTXs.sql
    echo --------------------------------------------- >> ${generatedDir}/GMarkAsMandTXs.sql
done

# now do this inside the generated directory with the file you just GENERATED!
cat ${generatedDir}/GMarkAsMandTXs.sql | sqlite3 -echo -batch ${generatedDir}/TXs.db

