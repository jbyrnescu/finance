.open TXs.db
.mode csv
.output GsingleColDescr.txt
select description from BigTXView order by description asc;
.quit

