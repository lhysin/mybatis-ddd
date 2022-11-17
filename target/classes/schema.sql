CREATE SCHEMA ADM;

create table ADM.CUSTOMER
(
   CUST_NO varchar(255) not null,
   NAME varchar(255) ,
   AGE integer ,
   primary key(CUST_NO)
);