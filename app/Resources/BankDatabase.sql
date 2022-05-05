create table BankAccounts ( 
	account_id int primary key, -- one to many, each account can have many transactions
	email varchar (128) references users(email),  -- foreign key, 
	password varchar (128),
	account_type varchar(64),
	balance float
);

create table users ( 
	email varchar (128) primary key, -- one to many, each customer can have many acounts
	name_first varchar (64),
	name_last varchar (64),
	address varchar (256),
	social_number bigint,
	phone_number bigint
);

create table transactions (
	account_id int references BankAccounts(account_id), -- foreign key,
	transaction_id int primary key,
	transaction_type varchar (64),
	recipient_id int,
	transac_amt float, 
	cur_date date,
	inactive int
);

create table pending (
	account_id int,
	email varchar (128) primary key,
	password varchar (128),
	account_type varchar(64),
	balance float
);

select * from users;
select * from BankAccounts;
select * from pending;
select * from transactions;

SELECT account_id, users.email, users.name_first, users.name_last, pending.balance, account_type FROM pending INNER JOIN users ON pending.email = users.email;

drop table BankAccounts;
drop table users cascade;
drop table transactions;

truncate BankAccounts cascade;
truncate users cascade;
truncate pending;
truncate transactions;

update transactions set account_id = null, inactive = 608310 where account_id = 608310;
delete from BankAccounts where account_id = 329170;

INSERT INTO transactions (account_id, transaction_id, transaction_type, recipient_id, transac_amt, cur_date) VALUES (123123, 123123, 'deposit', 123123, 33, '2022-05-04');
insert into BankAccounts (account_id) values (123123);




