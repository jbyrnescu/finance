#!/bin/sh -x

# we change directory to where the csv files have been copied

echo Ive been given a directory to change to where the csv files are:
echo Changing directory to: $1
cd $1

echo massageCSVs.sh

curDir=`pwd`

sed -i -e '1 d' CheckingStarOneTXs.csv

sed -i -e '1 i \
TransactionNumber,TransactionDate,Memo,Description,DebitAmount,CreditAmount,Balance,CheckNumber,Fees,BudgetCat\
' CheckingStarOneTXs.csv

sed -i -e '1 d' SavingsStarOneTXs.csv

sed -i -e '1 i \
TransactionNumber,TransactionDate,Memo,Description,DebitAmount,CreditAmount,Balance,CheckNumber,Fees,BudgetCat' SavingsStarOneTXs.csv

# remove 1st 4 lines of Barclays because it's header info
sed -i -e "1,4 d" MastercardBarclaysJetsTXs.csv

sed -i -e 's/Transaction Date/TransactionDate/' MastercardBarclaysJetsTXs.csv
sed -i -e 's/Category/BudgetCat/' MastercardBarclaysJetsTXs.csv

# change the Visa cc headers

sed -i -e 's/Transaction Date/TransactionDate/' VisaChaseTXs.csv
sed -i -e 's/Post Date/PostDate/' VisaChaseTXs.csv


#add column labels/names to WellsFargo
sed -i -e '1 i \
TransactionDate,Amount,Unknown1,Unknown2,Description,BudgetCat\
' CheckingWellsFargoTXs.csv

#add column labels/names to WellsFargo (CreditCard)
sed -i -e '1 i \
TransactionDate,Amount,Unknown1,Unknown2,Description,BudgetCat\
' VisaWellsFargoTXs.csv

dateSuffix=`date "+%Y%m%d%H%M"`

# we need to modify the _template_ import statement in order to reflect where our .csv files are
echo pwd is: `pwd`
cp ../importNMigrateCSVs.sql GimportCSVs${dateSuffix}.sql
sed -i -e 's/\/Users\/jbyrne\/TeamRex Dropbox\/John Byrne\/ScannedDocs\/2021_05_04\/taxes2020\///' GimportCSVs${dateSuffix}.sql

# Shell cc header stuff
# now... add the header so the friggin' database import prog. knows
# header: TransactionDate, Amount, Description, TransactionType

sed -i -e '1 i \
TransactionDate,Amount,Description,TransactionType
' MastercardCitibankShellTXs.csv

cat GimportCSVs${dateSuffix}.sql | sqlite3 -echo -batch

