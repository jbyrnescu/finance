The first step to using this is to update this stuff to excel.
I know, i know... "what am i even doing this for, I thought
that this program was supposed to do all of that!"

You just need to do this until it's trained or there needs to be some
training with new types of transactions.

anyway... find the transactions you don't want to include in a cash flow
calculation.  Ones that aren't constant.  Or that aren't part of your budget.

Then mark those and put them in the file: XcldFrmCshFlw.txt

Then count with an aggregate function the ones that you marked make sure
it's the same number as the ones you have in Excel.  (Excel Mrkd = db mrkd).

Then you can run your cash flow stuff.

Then take on the budget calcs.  (Or do the budget calcs first... I don't
care)


There may in the future be enhancements with receipts in the database.
To do this (Possibly)  Otherwise writefile will need to be used somehow:

sqlite> insert into myblob values (readfile('README.txt'));
sqlite> .output READMETest.txt
sqlite> select * from myblob;
