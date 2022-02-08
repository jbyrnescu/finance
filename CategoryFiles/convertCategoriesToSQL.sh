#!/bin/sh

stmt1="update $tble1 set BudgetCat="
stmt2="where Description like \"%"
stmt3="%\""

tble1="CheckingStarOneTXs"
tble2="CheckingWellsFargoTXs"
tble3="MastercardBarclaysJetsTXs"
tble4="MastercardCitibankShellTXs"
tble5="SavingsStarOneTXs"
tble6="VisaChaseTXs"

#echo CHECK
#echo $stmt1 $stmt2 $stmt3

#head -n 2 Categorized.csv 
#echo
#echo
cat Categorized.csv | sed -E -e 's/^([^,]*),(.*)$/ update CheckingWellsFargoTXs set BudgetCat="\2" where Description like "%\1%"; /' > categorizeTXs.sql
echo --------------------------------------------- >> categorizeTXs.sql
cat Categorized.csv | sed -E -e 's/^([^,]*),(.*)$/ update CheckingWellsFargoTXs set BudgetCat="\2" where Description like "%\1%"; /' >> categorizeTXs.sql
echo --------------------------------------------- >> categorizeTXs.sql
cat Categorized.csv | sed -E -e 's/^([^,]*),(.*)$/ update MastercardBarclaysJetsTXs set BudgetCat="\2" where Description like "%\1%"; /' >> categorizeTXs.sql
echo --------------------------------------------- >> categorizeTXs.sql
cat Categorized.csv | sed -E -e 's/^([^,]*),(.*)$/ update MastercardCitibankShellTXs set BudgetCat="\2" where Description like "%\1%"; /' >> categorizeTXs.sql
echo --------------------------------------------- >> categorizeTXs.sql
cat Categorized.csv | sed -E -e 's/^([^,]*),(.*)$/ update SavingsStarOneTXs set BudgetCat="\2" where Description like "%\1%"; /' >> categorizeTXs.sql
echo --------------------------------------------- >> categorizeTXs.sql
cat Categorized.csv | sed -E -e 's/^([^,]*),(.*)$/ update VisaChaseTXs set BudgetCat="\2" where Description like "%\1%"; /' >> categorizeTXs.sql






