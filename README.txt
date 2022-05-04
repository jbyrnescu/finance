The first step to using this is to update this stuff to excel.
There is a file called output.csv under ~/finance/.  Import this file.
Import it into $A$2 of sheet 1 of financeTemplate.xlxt
This will also populate a graph which I term cash flow line.

The idea is, once you spend over your cash flow, you're not saving any more.
So, you need to stay underneath the cash flow line.  If it doesn't work
you'll probably need to work with the spreadsheet a little.  It's
kind of complicated.

I know, i know... "what am i even doing this for, I thought
that this program was supposed to do all of that!"  A lot of the
busy work has been taken out of this process.  enough said.

You just need to do this until it's trained or there needs to be some
training with new types of transactions.

anyway... find the transactions you don't want to include in a cash flow
calculation.  Ones that aren't constant.  Or that aren't part of your budget.

Then mark those and put them in the file: XcldFrmCshFlw.txt

For testing:
Then count with an aggregate function the ones that you marked. Make sure
it's the same number as the ones you have in Excel.  (Excel Mrkd = db mrkd).

Then you can run your cash flow stuff.

Then take on the budget calcs.  (Or do the budget calcs first... I don't
care).  There is an sql statement under Queries that can help with this.

There may in the future be enhancements with receipts in the database.
To do this (Possibly)  Otherwise writefile will need to be used somehow:

sqlite> insert into myblob values (readfile('README.txt'));
sqlite> .output READMETest.txt
sqlite> select * from myblob;
