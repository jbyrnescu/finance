select BudgetCat,sum(amount) from BigTXView group by BudgetCat order by sum(amount) asc;
