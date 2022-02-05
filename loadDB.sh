#!/bin/sh -x

# Chase Visa - Chase1791_Activity20210410_20210630_20210707.CSV
# Wells Fargo - CreditCard2-2.csv
# StarOne Savings - statement_starone_1_04_10_2021_to_06_30_2021.csv
# StarOne Checking - statement_starone_2_04_10_2021_to_06_30_2021.csv

# files that are generated are marked in the name with 'G'

# /home is for Linux (Ubuntu)
downloadLocation="/home/jbyrne/Downloads"

dateSuffix=`date "+%Y%m%d%H%M"`

rootTaxesFolder='/home/jbyrne/finance/'
#rootTaxesFolder='/Users/jbyrne/icloud/2021_05_04/taxes2020/'
subfolderLoc="Gcsvs_${dateSuffix}"

# testing will automatically delete old Gcsvs folders
testing=1

echo inside loadDB.sh

# Rename files to standard names

# rename format: <type><Institution><Brand>
# valid types: Visa, Mastercard, Checking, Savings
# brand can be: Jets, Shell etc.

# find Chase file (always starts with Chase)
chaseDownloadFilename=`ls -lrt -C1 ${downloadLocation}/Chase* | tail -1`

# We're not doing Wells Fargo anymore.  Maybe later when we get rich
# not exactly in the cards right now
#wellsFargoVisaDownloadFilename=`ls -lrt -C1 ${downloadLocation}/CreditCard* | egrep -e 'CreditCard[0-9]{1,2}.*' | tail -1`
#wellsFargoCheckingDownloadFilename=`ls -rt -C1 ${downloadLocation}/Checking*.csv | tail -1`

#barclaysDownloadFilename=`ls -lrt -C1 ${downloadLocation}/CreditCard_* | tail -1`
starOneSavings=`ls -lrt -C1 ${downloadLocation}/statement_starone_1* | tail -1`
starOneChecking=`ls -lrt -C1 ${downloadLocation}/statement_starone_2* | tail -1`

if [ "$testing" = '1' ]
then
    echo removing `ls Gcsvs*`
    rm -r Gcsvs*
#    read myvar 1
fi

mkdir $subfolderLoc

# now do the actual renaming (and moving to the subfolderLoc)

# we have 2 versions because I want to keep a copy for reference purposes if needed later
# I don't want to blow away something I may need later
# this is for DB redundancy in subfolderLoc

cp $chaseDownloadFilename ${subfolderLoc}/VisaChaseTXs.csv
#cp $wellsFargoVisaDownloadFilename ${subfolderLoc}/VisaWellsFargoTXs.csv
#cp $wellsFargoCheckingDownloadFilename ${subfolderLoc}/CheckingWellsFargoTXs.csv
#cp $barclaysDownloadFilename ${subfolderLoc}/MastercardBarclaysJetsTXs.csv
#cp $mastercardCitibankShellTXs ${subfolderLoc}/MastercardCitibankShellTXs.csv
cp $starOneSavings ${subfolderLoc}/SavingsStarOneTXs.csv
cp $starOneChecking ${subfolderLoc}/CheckingStarOneTXs.csv

cp $chaseDownloadFilename VisaChaseTXs.csv
#cp $wellsFargoVisaDownloadFilename VisaWellsFargoTXs.csv
#cp $wellsFargoCheckingDownloadFilename CheckingWellsFargoTXs.csv
#cp $barclaysDownloadFilename MastercardBarclaysJetsTXs.csv
#cp $mastercardCitibankShellTXs ${subfolderLoc}/MastercardCitibankShellTXs.csv
cp $starOneSavings SavingsStarOneTXs.csv
cp $starOneChecking CheckingStarOneTXs.csv

# unfortunately, Shell credit card csvs come in chunks of a month and
# need to be combined.
# so the strategy is to find all matching files from the template/regex
# and then combine them.

# now that I think about it... the order doesn't matter
# ls -C1 -rt ${downloadLocation}/ | tail -25 | egrep "^[0-9]{1,2}-[0-9]{1,2}-[0-9]{1,4}\.csv$" > /tmp/ShellTXFiles.txt
# cat /tmp/ShellTXFiles.txt | sort > /tmp/ShellTXFiles2.txt

# We're not working with Citibank Shell any more - Canceled credit card
# for i in `ls -C1 -rt ${downloadLocation}/ | tail -25 | egrep "^[0-9]{1,2}-[0-9]{1,2}-[0-9]{1,4}\.csv$"`
# do
#     # change the tabs into commas
#     # and append the info into a big file
#     echo adding: $i to MastercardCitibankShellTXS.csv
#     cat ${downloadLocation}/$i | tr -d "$" | tr -d "'" | tr "\t" "," | tr -s " " >> ${subfolderLoc}/MastercardCitibankShellTXs.csv
# done

./massageCSVs.sh $subfolderLoc

# No arguments passable to .sql file?  So... we're going to put them in the root
# as well as down under after massaged

cp $chaseDownloadFilename ${rootTaxesFolder}VisaChaseTXs.csv
#cp $wellsFargoVisaDownloadFilename ${rootTaxesFolder}VisaWellsFargoTXs.csv
#cp $wellsFargoCheckingDownloadFilename ${rootTaxesFolder}CheckingWellsFargoTXs.csv
#cp $barclaysDownloadFilename ${rootTaxesFolder}MastercardBarclaysJetsTXs.csv
cp $starOneSavings ${rootTaxesFolder}SavingsStarOneTXs.csv
cp $starOneChecking ${rootTaxesFolder}CheckingStarOneTXs.csv


#mv TXs.db TXs_${dateSuffix}.db

# make sure there's a database
dateSuffix=`date "+%Y%m%d%H%M"`
# This doesn't work on the command line
#echo ".quit" | sqlite3 TXs.db

# mark non cash flow/"exclusions" out of generating the cash flow
for i in "VisaChaseTXs" "CheckingStarOneTXs" "SavingsStarOneTXs"
do
    sed -r -e "s/(^.*$)/update $i set XclFrmCshFlw=\'y\' where description like \'%\1%\';/" XcldFrmCshFlw.txt >> ${subfolderLoc}/GMrkNnCshFlwTXs.sql
done

#echo ".schema VisaChaseTXs" >> ${subfolderLoc}/GimportCSVs${dateSuffix}.sql

cat ${subfolderLoc}/GMrkNnCshFlwTXs.sql >> ${subfolderLoc}/GimportCSVs${dateSuffix}.sql

echo .save >> ${subfolderLoc}/GimportCSVs${dateSuffix}.sql
echo .quit >> ${subfolderLoc}/GimportCSVs${dateSuffix}.sql

cd ${subfolderLoc}

cat GimportCSVs${dateSuffix}.sql | sqlite3 -echo -batch &2> Gerrors.txt

# BEFOE
# sqlite3 -bail -batch -init getBagOfWords.sql

# we're going to do this later
#AFTA
#cat getBagOfWords.sql | sqlite3 -bail -echo &2>> errors.txt

#./createWordList.sh

#./createCategoryTableListing.sh



