CREATE SCHEMA ADM;

create table ADM.CUSTOMER
(
   CUST_NO varchar(255) not null,
   NAME varchar(255) ,
   AGE integer ,
   primary key(CUST_NO)
);

create table ADM.TORDER
(
    CUST_NO varchar(255) not null,
    ORDER_NO varchar(255) not null,
    NAME varchar(255) ,
    primary key(CUST_NO, ORDER_NO)
);
