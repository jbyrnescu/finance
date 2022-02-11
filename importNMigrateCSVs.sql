
.mode csv

-- open the db --

.open TXs.db

.import "VisaChaseTXs.csv" FromCSV_VisaChaseTXs

--.import "MastercardBarclaysJetsTXs.csv" FromCSV_MastercardBarclaysJetsTXs

--.import "CheckingWellsFargoTXs.csv"  FromCSV_CheckingWellsFargoTXs

--.import "CheckingWellsFargoTXs.csv"  FromCSV_VisaWellsFargoTXs

.import "CheckingStarOneTXs.csv"  FromCSV_CheckingStarOneTXs

.import "SavingsStarOneTXs.csv"  FromCSV_SavingsStarOneTXs

--.import "MastercardCitibankShellTXs.csv"  FromCSV_MastercardCitibankShellTXs

-- We must create the tables, obviously, before we can migrate to them...
-- So... here we are... creating tables:


-- CREATE TABLE IF NOT EXISTS "CheckingWellsFargoTXs" (
-- 	"TransactionDate"	TEXT,
-- 	"Amount"	REAL,
-- 	"Unknown1"	TEXT,
-- 	"Unknown2"	TEXT,
-- 	"Description"	TEXT,
-- 	"BudgetCat"	TEXT,
--	"XclFrmCshFlw"	TEXT,
-- 	"Source"	TEXT
-- );

-- CREATE TABLE IF NOT EXISTS "VisaWellsFargoTXs" (
-- 	"TransactionDate"	TEXT,
-- 	"Amount"	REAL,
-- 	"Unknown1"	TEXT,
-- 	"Unknown2"	TEXT,
-- 	"Description"	TEXT,
-- 	"BudgetCat"	TEXT,
--	"XclFrmCshFlw"	TEXT,
-- 	"Source"	TEXT
-- );

CREATE TABLE IF NOT EXISTS "CheckingStarOneTXs" (
	"TransactionNumber"	INTEGER,
	"TransactionDate"	TEXT,
	"Memo"			TEXT,
	"Description"	TEXT,
	"DebitAmount"	REAL,
	"CreditAmount"	REAL,
	"Balance"	REAL,
	"CheckNumber"	TEXT,
	"Fees"		REAL,
	"BudgetCat"	TEXT,
	"Amount"	REAL,
	"XclFrmCshFlw"	TEXT,
	"Mandatory"	TEXT,
	"Source"	TEXT

);

CREATE TABLE IF NOT EXISTS "SavingsStarOneTXs" (
	"TransactionNumber"	INTEGER,
	"TransactionDate"	TEXT,
	"Memo"		TEXT,
	"Description"	TEXT,
	"DebitAmount"	REAL,
	"CreditAmount"	REAL,
	"Balance"	REAL,
	"CheckNumber"	TEXT,
	"Fees"		TEXT,
	"BudgetCat"	TEXT,
	"Amount"	REAL,
	"XclFrmCshFlw"	TEXT,
	"Mandatory"	TEXT,
	"Source"	TEXT
);
 

CREATE TABLE IF NOT EXISTS "VisaChaseTXs" (
	"TransactionDate"	TEXT,
	"PostDate"	TEXT,
	"Description"	TEXT,
	"Category"	TEXT,
	"TransactionType"		TEXT,
	"Amount"	REAL,
	"BudgetCat"	TEXT,
	"Memo"		TEXT,
	"XclFrmCshFlw"	TEXT,
	"Mandatory"	TEXT,
	"Source"	TEXT
);

-- CREATE TABLE IF NOT EXISTS "MastercardBarclaysJetsTXs" (
-- 	"TransactionDate"	TEXT,
-- 	"Description"	TEXT,
-- 	"Category"	TEXT,
-- 	"Amount"	REAL,
-- 	"BudgetCat"	TEXT,
-- 	"Source"	TEXT
-- );

-- CREATE TABLE IF NOT EXISTS "MastercardCitibankShellTXs" (
-- 	"TransactionDate"	TEXT,
-- 	"Amount"	REAL,
-- 	"Description"	TEXT,
-- 	"TransactionType" TEXT,
-- 	"BudgetCat"	TEXT,
-- 	"Source"	TEXT
-- );

--  alter table CheckingStarOneTXs add Amount REAL; 
-- has to happen after import
-- update CheckingStarOneTXs set Amount = CreditAmount-DebitAmount;

CREATE VIEW BigTXView as

-- select TransactionDate, Description, amount, BudgetCat, XclFrmCshFlw, source from MastercardBarclaysJetsTXs
-- union
select TransactionDate, Description, amount, BudgetCat, XclFrmCshFlw, Mandatory, source from VisaChaseTXs
union 
-- select TransactionDate, Description, amount, BudgetCat, XclFrmCshFlw, source from MastercardCitibankShellTXs
-- UNION
select TransactionDate, Description, amount, BudgetCat, XclFrmCshFlw, Mandatory, source from CheckingStarOneTXs
union
select TransactionDate, Description, amount, BudgetCat, XclFrmCshFlw, Mandatory, source from SavingsStarOneTXs
-- union
-- select TransactionDate, Description, Amount, BudgetCat, XclFrmCshFlw, source from CheckingWellsFargoTXs
-- union
-- select TransactionDate, Description, Amount, BudgetCat, XclFrmCshFlw, source from VisaWellsFargoTXs
;

----- ----- ----- ----- -----
-- Done... creating tables.
----- ----- ----- ----- -----

-- -- below is the MigrateAll CSVs structured query language file that migrates FromCSV_ files to the correct tables.

-- ----- ----- ----- ----- ----- 
-- -- Checking from Wells Fargo Account
-- ----- ----- ----- ----- ----- 

-- insert into CheckingWellsFargoTXs
--        (
--        TransactionDate,
--        Amount,
--        Unknown1,
--        Unknown2,
--        Description,
--        BudgetCat,
--        Source
--        )

-- select
-- 	TransactionDate, 
-- 	cast(Amount as real),
-- 	Unknown1,
-- 	Unknown2,
-- 	Description,
-- 	BudgetCat,
-- 	"CheckingWellsFargo"
-- 	from FromCSV_CheckingWellsFargoTXs;


-- insert into VisaWellsFargoTXs
--        (
--        TransactionDate,
--        Amount,
--        Unknown1,
--        Unknown2,
--        Description,
--        BudgetCat,
--        Source
--        )
-- select
-- 	TransactionDate, 
-- 	cast(Amount as real),
-- 	Unknown1,
-- 	Unknown2,
-- 	Description,
-- 	BudgetCat,
-- 	"VisaWellsFargoTXs,"
-- 	from FromCSV_VisaWellsFargoTXs;

----- ----- ----- ----- ----- 
-- Checking Account from Star One
----- ----- ----- ----- -----

insert into CheckingStarOneTXs (TransactionNumber,TransactionDate, Memo, Description, DebitAmount, CreditAmount, Balance, CheckNumber, Fees, BudgetCat, amount, source)
       select TransactionNumber, 
					TransactionDate, 
					Memo, 
					Description, 
					cast(DebitAmount as real), 
					cast(CreditAmount as real), 
					cast(Balance as real),
					CheckNumber, 
					Fees, 
					BudgetCat, 
					0,
					"CheckingStarOneTXs"
       	      from FromCSV_CheckingStarOneTXs;

update CheckingStarOneTXs 
set 
	CreditAmount = 0 
where creditamount is null;

update CheckingStarOneTXs 
set 
	DebitAmount = 0 
 where DebitAmount is null;

update CheckingStarOneTXs
  set amount = CreditAmount - DebitAmount
  where 1=1;

----- ----- ----- ----- ----- 
-- Savings account from Star One
----- ----- ----- ----- -----

insert into SavingsStarOneTXs
(
	TransactionNumber,
	TransactionDate,
	Description,
	Memo,
	DebitAmount,
	CreditAmount,
	Balance,
	CheckNumber,
	Fees,
	Amount,
	Source
	)
select 
  TransactionNumber,
  TransactionDate,
  Description,
  Memo,
  cast(DebitAmount as real),
  cast(CreditAmount as real),
  cast(Balance as real),
  CheckNumber,
  Fees,
  0,
  "SavingsStarOneTXs"
from FromCSV_SavingsStarOneTXs;


  update SavingsStarOneTXs 
	set 
		CreditAmount = 0 
 where creditamount is null;

update SavingsStarOneTXs 
	set 
		DebitAmount = 0 
 where DebitAmount is null;

update SavingsStarOneTXs
  set amount = CreditAmount - DebitAmount
  where 1=1;

----- ----- ----- ----- ----- 
-- Mastercard Barclays Jets Credit Card
----- ----- ----- ----- ----- 

-- insert into MastercardBarclaysJetsTXs
-- (
-- 	TransactionDate,
-- 	Description,
-- 	Category,
-- 	Amount,
-- 	BudgetCat,
-- 	Source
-- )
-- select
--   TransactionDate,
--   Description,
--   BudgetCat,
--   cast(Amount as real),
--   BudgetCat,
--   "MastercardBarclaysJetsTXs"
-- from FromCSV_MastercardBarclaysJetsTXs;

----- ----- ----- ----- ----- 
-- TXs from Visa Chase CC
----- ----- ----- ----- -----

insert into VisaChaseTXs (
	TransactionDate,
	PostDate,
	Description,
	Category,
	TransactionType,
	Amount,
	BudgetCat,
	Memo,
	Source
)
select 
  TransactionDate,
  PostDate,
  Description,
  Category,
  Type,
  cast(Amount as real),
  Category,
  Memo,
  "VisaChaseTXs"
from FromCSV_VisaChaseTXs;


----- ----- ----- ----- -----
-- migrate Shell transactions from CSV file
----- ----- ----- ----- -----
-- insert into MastercardCitibankShellTXs (
-- 	TransactionDate,
-- 	Amount,
-- 	Description,
-- 	TransactionType,
-- 	Source
-- )
-- select 
--        TransactionDate,
--        cast(Amount as real),
--        Description,
--        TransactionType,
--         "MastercardCitibankShellTXs"
-- from FromCSV_MastercardCitibankShellTXs;

-- ----- ----- ----- ----- -----
-- -- Done migrating Shell transactions from csv table
-- ----- ----- ----- ----- -----

-- These last two statements are added later by a script
--.save
--.quit
