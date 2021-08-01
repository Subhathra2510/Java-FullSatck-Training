

create database customer;
use customer;

create table Accounts(

customer_id int primary key auto_increment,
customer_name varchar(256),
customer_ActNum long,
Act_balance int

);