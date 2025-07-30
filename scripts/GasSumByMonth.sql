select STRFTIME("%m",transactiondate) month, sum(amount) from BigTXView where budgetcat = 'GAS' group by month;
