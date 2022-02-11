README.txt

This file is about this directory.  it explains how the categorization works
for this project.

Overall Summary:

Goal: Find single words that will create good budget category keys and use them
to put in SQL to categorize transactions.  This may not work for all
transactions, but hopefully it will work with 80-90% of them.

My Solution:
1) Create a one column list of words from all of the transactions that are
in the database
2) Use that column and remove the bad words
3) from that good list generate words that you can use to categorize with
 (The last step is manual)
 
