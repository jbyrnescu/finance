select SUM(amount) from BigTXView where XclFrmCshFlw is null and amount < 0;
