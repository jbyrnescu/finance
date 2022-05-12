select BudgetCat,sum(amount) from BigTXView where budgetCat not like "%ayment%" and budgetCat not like "%Income%" group by BudgetCat order by sum(amount) asc;
